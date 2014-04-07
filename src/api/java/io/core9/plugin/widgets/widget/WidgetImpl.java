package io.core9.plugin.widgets.widget;

import io.core9.plugin.database.repository.AbstractCrudEntity;
import io.core9.plugin.widgets.datahandler.DataHandler;

public class WidgetImpl extends AbstractCrudEntity implements Widget {
	
	private String name;
	private String templateName;
	private DataHandler<?> dataHandler;
	private String template;
	
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
	public DataHandler<?> getDataHandler() {
		return this.dataHandler;
	}
	
	public Widget setDataHandler(DataHandler<?> dataHandler) {
		this.dataHandler = dataHandler;
		return this;
	}
	
	public Widget setTemplate(String template) {
		this.template = template;
		return this;
	}

	@Override
	public String getTemplate() {
		return this.template;
	}

}
