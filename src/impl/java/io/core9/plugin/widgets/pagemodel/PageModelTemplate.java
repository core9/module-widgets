package io.core9.plugin.widgets.pagemodel;

import io.core9.plugin.widgets.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class PageModelTemplate {
	
	private String templateName;
	private List<Widget> widgets;

	public void setName(String name) {
		this.templateName = name;
	}
	
	public void addWidget(Widget widget) {
		this.widgets.add(widget);
	}
	
	@Override
	public String toString() {
		String body = "{namespace io.core9}\n";
		body += "/**\n"
			  + " * @param data\n"
			  + " */\n"
			  + "{template ." + templateName + "}\n";
		for(Widget widget : widgets) {
			  body += "{call " + widget.getTemplateName() + " data=\"$data." + widget.getName() + "\"/}\n";
		}
		body += "{/template}\n";
		return body;
	}
	
	public String getFilename() {
		return "io.core9.pagemodels." + this.templateName + ".soy";
	}
	
	public PageModelTemplate() {
		this.widgets = new ArrayList<Widget>();
	}

}
