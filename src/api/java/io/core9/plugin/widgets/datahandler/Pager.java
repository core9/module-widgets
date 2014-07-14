package io.core9.plugin.widgets.datahandler;

public class Pager {

	private int resultsPerPage = 0;

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
		if(resultsPerPage == 0) {
			return 0;
		}
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
		if(resultsPerPage == 0) {
			return size;
		}
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
}
