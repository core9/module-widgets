package io.core9.plugin.widgets.datahandler;

import java.util.List;

import io.core9.plugin.widgets.Core9HiddenField;
import io.core9.plugin.widgets.datahandler.factories.CustomVariable;

public class DataHandlerDefaultConfig implements DataHandlerFactoryConfig {
	
	@Core9HiddenField
	private String componentName;
	
	private List<CustomVariable> customVariables;


	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

	public List<CustomVariable> getCustomVariables() {
		return customVariables;
	}

	public void setCustomVariables(List<CustomVariable> customVariables) {
		this.customVariables = customVariables;
	}
}
