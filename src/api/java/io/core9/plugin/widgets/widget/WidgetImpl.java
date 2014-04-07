package io.core9.plugin.widgets.widget;

import java.util.Map;

import io.core9.plugin.database.repository.AbstractCrudEntity;
import io.core9.plugin.database.repository.Collection;
import io.core9.plugin.widgets.datahandler.DataHandler;

@Collection("configuration")
public class WidgetImpl extends AbstractCrudEntity implements Widget {
	
	private String name;
	private String templateName;
	private DataHandler<?> dataHandler;
	private String template;
	private String handler;
	private Map<String,Object> handlerOptions;
	
	public Widget setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public Widget setTemplateName(String templateName) {
		this.templateName = templateName;
		return this;
	}

	@Override
	public String getTemplateName() {
		return this.templateName;
	}
	
	@Override
	public Widget setDataHandler(DataHandler<?> dataHandler) {
		this.dataHandler = dataHandler;
		return this;
	}

	@Override
	public DataHandler<?> getDataHandler() {
		return this.dataHandler;
	}
	
	public Widget setTemplate(String template) {
		this.template = template;
		return this;
	}

	@Override
	public String getTemplate() {
		return this.template;
	}
	
	public void setHandler(String handler) {
		this.handler = handler;
	}

	@Override
	public String getHandler() {
		return handler;
	}
	
	public void setHandlerOptions(Map<String,Object> handlerOptions) {
		this.handlerOptions = handlerOptions;
	}

	@Override
	public Map<String, Object> getHandlerOptions() {
		return handlerOptions;
	}

}
