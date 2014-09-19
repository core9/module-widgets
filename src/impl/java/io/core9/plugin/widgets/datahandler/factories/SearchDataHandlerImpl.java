package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.database.mongodb.MongoDatabase;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class SearchDataHandlerImpl implements SearchDataHandler<SearchDataHandlerConfig> {

	@InjectPlugin
	public MongoDatabase database;
	
	@Override
	public String getName() {
		return "Search";
	}

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return SearchDataHandlerConfig.class;
	}

	@Override
	public DataHandler<SearchDataHandlerConfig> createDataHandler(final DataHandlerFactoryConfig options) {
		final SearchDataHandlerConfig config = (SearchDataHandlerConfig) options;
		return new DataHandler<SearchDataHandlerConfig>(){

			@Override
			public Map<String, Object> handle(Request req) {
				Map<String,Object> result = new HashMap<String, Object>();
				Map<String,Object> query = CustomGlobal.convertToQuery(config.getFields(), req, options.getComponentName());
				Map<String,Object> inner = new HashMap<String,Object>();
				inner.put("$search", config.getQuery(req));
				query.put("$text", inner);
				Map<String,Object> firstResult = null;
				if(config.isMultipleResults()) {
					List<Map<String,Object>> list = database.getMultipleResults(
							(String) req.getVirtualHost().getContext("database"), 
							req.getVirtualHost().getContext("prefix") + config.getContentType(), 
							query);
					if(config.getPager() != null) {
						int size = list.size();
						String pageStr = (String) req.getParams().get("page");
						int page;
						try {
							page = Integer.parseInt(pageStr);
						} catch (NullPointerException | NumberFormatException e) {
							page = 1;
						}
						list = list.subList(config.getPager().retrievePageStartIndex(size, page), 
												config.getPager().retrievePageEndIndex(size, page));
						Map<String,Object> pager = new HashMap<String,Object>();
						pager.put("total", config.getPager().retrieveNumberOfPages(size));
						pager.put("page", page);
						result.put("pager", pager);
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
			public SearchDataHandlerConfig getOptions() {
				return (SearchDataHandlerConfig) options;
			}
		};
	}

}
