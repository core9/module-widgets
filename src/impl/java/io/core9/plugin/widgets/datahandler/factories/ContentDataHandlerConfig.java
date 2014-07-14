package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;
import io.core9.plugin.widgets.datahandler.Pager;

import java.util.List;


public class ContentDataHandlerConfig extends DataHandlerDefaultConfig {
	
	private String contentType;
	private List<CustomGlobal<Object>> fields;
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

	public List<CustomGlobal<Object>> getFields() {
		return fields;
	}

	public void setFields(List<CustomGlobal<Object>> fields) {
		this.fields = fields;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}
}
