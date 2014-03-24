package io.core9.plugin.widgets.widget;

import io.core9.plugin.widgets.datahandler.DataHandler;

public interface Widget {
	
	String getName();
	
	String getTemplateName();
	
    DataHandler<?> getDataHandler();
    
    String getTemplate();
    
}