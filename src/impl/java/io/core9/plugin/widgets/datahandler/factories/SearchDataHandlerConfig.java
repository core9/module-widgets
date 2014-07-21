package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobal;
import io.core9.plugin.widgets.datahandler.Pager;


public class SearchDataHandlerConfig extends DataHandlerDefaultConfig {
	
	private String contentType;
	private DataHandlerGlobal<String> query;
	private boolean multipleResults;
	private Pager pager;

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isMultipleResults() {
		return multipleResults;
	}

	public void setMultipleResults(boolean multipleResults) {
		this.multipleResults = multipleResults;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

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
