package io.core9.plugin.widgets.datahandler;

public class DataHandlerDefaultConfig implements DataHandlerFactoryConfig {
	
	private String componentId;

	@Override
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Override
	public String getComponentId() {
		return componentId;
	}

}
