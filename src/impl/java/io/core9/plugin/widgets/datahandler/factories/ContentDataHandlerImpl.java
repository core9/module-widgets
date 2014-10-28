package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.database.mongodb.MongoDatabase;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * TODO: Cleanup (remove price field)
 * @author mark
 *
 * @param <T>
 */
@PluginImplementation
public class ContentDataHandlerImpl<T extends ContentDataHandlerConfig> implements ContentDataHandler<T> {

	@InjectPlugin
	public MongoDatabase database;
	
	@Override
	public String getName() {
		return "Content";
	}

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return ContentDataHandlerConfig.class;
	}

	@Override
	public DataHandler<T> createDataHandler(final DataHandlerFactoryConfig options) {
		@SuppressWarnings("unchecked")
		final T config = (T) options;
		return new DataHandler<T>(){

			@Override
			public Map<String, Object> handle(Request req) {
				
				Map<String,Object> result = new HashMap<String, Object>();
				Map<String,Object> query = CustomGlobal.convertToQuery(config.getFields(), req, options.getComponentName());
				Map<String,Object> firstResult = null;
				if(config.isMultipleResults()) {
					VirtualHost vhost = req.getVirtualHost();
					DBCollection coll = database.getCollection(vhost.getContext("database"), vhost.getContext("prefix") + config.getContentType());
					DBCursor cursor = coll.find(new BasicDBObject(query));
					if(req.getParams().get("price") != null) {
						cursor.sort(new BasicDBObject("price", Integer.parseInt((String) req.getParams().get("price"))));
					}
					if(config.getPager() != null) {
						int size = cursor.size();
						String pageStr = (String) req.getParams().get("page");
						int page;
						try {
							page = Integer.parseInt(pageStr);
						} catch (NullPointerException | NumberFormatException e) {
							page = 1;
						}
						cursor.skip(config.getPager().retrievePageStartIndex(size, page));
						cursor.limit(config.getPager().getResultsPerPage());
						Map<String,Object> pager = new HashMap<String,Object>();
						pager.put("total", config.getPager().retrieveNumberOfPages(size));
						pager.put("page", page);
						result.put("pager", pager);
					}
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					while(cursor.hasNext()) {
						list.add(cursor.next().toMap());
					}
					result.put("content", list);
					if(list.size() > 0) {
						firstResult = list.get(0);
					}
				} else {
					firstResult = database.getSingleResult(
							(String) req.getVirtualHost().getContext("database"), 
							req.getVirtualHost().getContext("prefix") + config.getContentType(), 
							query);
					result.put("content", firstResult);
				}
				if(config.getCustomVariables() != null) {
					for(CustomVariable var : config.getCustomVariables()) {
						if(var.isManual()) {
							req.getResponse().addGlobal(var.getKey(), var.getValue());
						} else {
							if(firstResult != null) {
								req.getResponse().addGlobal(var.getKey(), firstResult.get(var.getValue()));
							}
						}
					}
				}
				return result;
			}

			@Override
			public T getOptions() {
				return config;
			}
		};
	}

}
