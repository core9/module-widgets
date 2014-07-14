package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobal;

public class IdentifierDataHandlerConfig extends DataHandlerDefaultConfig {
	
	private DataHandlerGlobal<String> id;

	/**
	 * @return the productID
	 */
	public DataHandlerGlobal<String> getId() {
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
	public void setId(DataHandlerGlobal<String> id) {
		this.id = id;
	}

}
