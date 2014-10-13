package io.core9.plugin.widgets.datahandler;

import io.core9.plugin.server.request.Request;

import java.util.HashMap;
import java.util.Map;

public abstract class ContextualDataHandler<T extends DataHandlerFactoryConfig> implements DataHandler<T> {
	
	@Override
	public Map<String,Object> handle(Request req) {
		return handle(req, new HashMap<String, Object>());
	}

	public abstract Map<String,Object> handle(Request req, Map<String,Object> context);
}
