package io.core9.plugin.widgets.datahandler;

import io.core9.plugin.widgets.Core9HiddenField;

public class DataHandlerDefaultConfig implements DataHandlerFactoryConfig {
	
	@Core9HiddenField
	private String componentName;

	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
