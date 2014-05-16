package io.core9.plugin.widgets.datahandler.factories;


public class ContentDataHandlerConfig extends IdentifierDataHandlerConfig {
	
	private String contentType;
	private String fieldName;
	private boolean multipleResults;

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

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isMultipleResults() {
		return multipleResults;
	}

	public void setMultipleResults(boolean multipleResults) {
		this.multipleResults = multipleResults;
	}
}
