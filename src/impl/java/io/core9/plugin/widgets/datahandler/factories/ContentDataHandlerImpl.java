package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.database.mongodb.MongoDatabase;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class ContentDataHandlerImpl implements ContentDataHandler<ContentDataHandlerConfig> {

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
	public DataHandler<ContentDataHandlerConfig> createDataHandler(final DataHandlerFactoryConfig options) {
		final String fieldname = ((ContentDataHandlerConfig) options).getFieldName();
		return new DataHandler<ContentDataHandlerConfig>(){

			@Override
			public Map<String, Object> handle(Request req) {
				Map<String,Object> result = new HashMap<String, Object>();
				Map<String,Object> query = new HashMap<String, Object>();
				if(fieldname != null && !fieldname.equals("")) {
					query.put(fieldname, ((ContentDataHandlerConfig) options).getId(req));
				} else {
					query.put("_id", ((ContentDataHandlerConfig) options).getId(req));
				}
				result.put("content", database.getSingleResult(
						(String) req.getVirtualHost().getContext("database"), 
						req.getVirtualHost().getContext("prefix") + ((ContentDataHandlerConfig) options).getContentType(), 
						query)
				);
				return result;
			}

			@Override
			public ContentDataHandlerConfig getOptions() {
				return (ContentDataHandlerConfig) options;
			}
		};
	}

}
