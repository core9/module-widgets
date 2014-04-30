package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.database.mongodb.MongoDatabase;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class ReferenceDataHandlerImpl implements ReferenceDataHandler<ReferenceDataHandlerConfig> {
	
	@InjectPlugin
	public MongoDatabase database;

	@Override
	public String getName() {
		return "Reference";
	}

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return ReferenceDataHandlerConfig.class;
	}

	@Override
	public DataHandler<ReferenceDataHandlerConfig> createDataHandler(final DataHandlerFactoryConfig options) {
		final ReferenceDataHandlerConfig config = (ReferenceDataHandlerConfig) options;
		return new DataHandler<ReferenceDataHandlerConfig>(){
			
			@Override
			public Map<String, Object> handle(Request req) {
				Map<String, Object> result = new HashMap<>();
				
				// Build the query
				Map<String, Object> query = new HashMap<>();
				if(config.getFieldName() == null || config.getFieldName().equals("")) {
					query.put("_id", config.getId(req));
				} else {
					query.put(config.getFieldName(), config.getId(req));
				}
				Map<String,Object> content = database.getSingleResult(
						(String) req.getVirtualHost().getContext("database"), 
						req.getVirtualHost().getContext("prefix") + config.getContentType(), 
						query);
				query.clear();
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> refs = (List<Map<String, Object>>) content.get(config.getReferencingField());
				List<String> ids = new ArrayList<String>();
				for(Map<String,Object> ref : refs) {
					ids.add((String) ref.get("value"));
				}
				Map<String,Object> inner = new HashMap<String,Object>();
				inner.put("$in", ids);
				query.put("_id", inner);
				List<Map<String,Object>> contents = database.getMultipleResults(
						(String) req.getVirtualHost().getContext("database"), 
						req.getVirtualHost().getContext("prefix") + config.getReferencedContentType(), 
						query);
				
				int size = contents.size();
				String pageStr = (String) req.getParams().get("page");
				int page;
				try {
					page = Integer.parseInt(pageStr);
				} catch (NullPointerException | NumberFormatException e) {
					page = 1;
				}
				
				contents = contents.subList(config.retrievePageStartIndex(size, page), 
										config.retrievePageEndIndex(size, page));
				Map<String,Object> pager = new HashMap<String,Object>();
					pager.put("total", config.retrieveNumberOfPages(size));
					pager.put("page", page);
				result.put("pager", pager);
				result.put("content", content);
				result.put("contents", contents);
				return result;
			}

			@Override
			public ReferenceDataHandlerConfig getOptions() {
				return (ReferenceDataHandlerConfig) options;
			}
		};
	}
}
