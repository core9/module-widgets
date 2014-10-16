package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobal;

public class SearchDataHandlerConfig extends ContentDataHandlerConfig {
	
	private DataHandlerGlobal<String> query;

	public DataHandlerGlobal<String> getQuery() {
		return query;
	}
	
	public String getQuery(Request req) {
		if(query.isGlobal()) {
			return req.getContext(getComponentName() + ".query", query.getValue());
		}
		return query.getValue();
	}

	public void setQuery(DataHandlerGlobal<String> query) {
		this.query = query;
	}
}
