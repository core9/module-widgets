package io.core9.plugin.widgets.widget;

import io.core9.plugin.widgets.datahandler.DataHandler;

public interface Widget {
	
	/**
	 * Return the widget id
	 * @return
	 */
	String getId();
	
	/**
	 * Returns the widget name
	 * @return
	 */
	String getName();
	
	/**
	 * Returns the template name
	 * @return
	 */
	String getTemplateName();
	
	/**
	 * Returns the datahandler
	 * @return
	 */
    DataHandler<?> getDataHandler();
    
    /**
     * Returns the template
     * @return
     */
    String getTemplate();
    
}