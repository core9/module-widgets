package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobalString;

public class IdentifierDataHandlerConfig extends DataHandlerDefaultConfig {
	
	private DataHandlerGlobalString id;

	/**
	 * @return the productID
	 */
	public DataHandlerGlobalString getId() {
		return id;
	}
	
	public String getId(Request req) {
		if(id.isGlobal()) {
			return req.getContext(this.getComponentName() + ".id", id.getValue());
		}
		return id.getValue();
	}

	/**
	 * @param id
	 */
	public void setId(DataHandlerGlobalString id) {
		this.id = id;
	}
}
