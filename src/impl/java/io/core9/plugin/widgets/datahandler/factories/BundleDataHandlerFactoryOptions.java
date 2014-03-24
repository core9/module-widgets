package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.widgets.Component;
import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;

import java.util.List;

public class BundleDataHandlerFactoryOptions extends DataHandlerDefaultConfig {
	private List<Component> components;

	/**
	 * @return the components
	 */
	public List<Component> getComponents() {
		return components;
	}

	/**
	 * @param components the components to set
	 */
	public void setComponents(List<Component> components) {
		this.components = components;
	}

}
