package io.core9.plugin.widgets.datahandler.factories;


public class ReferenceDataHandlerConfig extends ContentDataHandlerConfig {
	
	private String referencingField;
	private String referencedContentType;
	private int resultsPerPage = 1;

	/**
	 * @return the referenceField
	 */
	public String getReferencingField() {
		return referencingField;
	}

	/**
	 * @param referenceField the referenceField to set
	 */
	public void setReferencingField(String referencingField) {
		this.referencingField = referencingField;
	}
	
	/**
	 * @return the resultsPerPage
	 */
	public int getResultsPerPage() {
		return resultsPerPage;
	}

	/**
	 * @param resultsPerPage the resultsPerPage to set
	 */
	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
	
	/**
	 * Return the starting index for a page
	 * @param size
	 * @param page
	 * @return
	 */
	public int retrievePageStartIndex(int size, int page) {
		int result;
		return (result = (page - 1) * resultsPerPage) < size ? result : size;
	}
	
	/**
	 * Return the end index for a page
	 * @param size
	 * @param page
	 * @return
	 */
	public int retrievePageEndIndex(int size, int page) {
		int result;
		return (result = page * resultsPerPage) < size ? result : size;
	}
	
	/**
	 * Return the numer of pages
	 * @param size
	 * @return
	 */
	public int retrieveNumberOfPages(int size) {
		int pages = 0;
		try {
			pages = (int) Math.ceil((double) size / resultsPerPage);
        } catch (Exception e) {
	        e.printStackTrace();
        }
		return pages;
	}

	public String getReferencedContentType() {
		return referencedContentType;
	}

	public void setReferencedContentType(String referencedContentType) {
		this.referencedContentType = referencedContentType;
	}
}
