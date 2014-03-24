package io.core9.plugin.widgets.datahandler;

import io.core9.plugin.server.request.Request;

import java.util.Map;

public interface DataHandler<T extends DataHandlerFactoryConfig> {
	
	/**
	 * Handle the request
	 * normally sets a data on the request params
	 * @param req
	 * @return a map with values to be used in the widget
	 */
	Map<String,Object> handle(Request req);
	
	/**
	 * Return the options
	 * @return
	 */
	T getOptions();
}
