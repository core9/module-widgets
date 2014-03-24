package io.core9.plugin.widgets.datahandler;

import io.core9.core.PluginRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.apache.commons.lang3.ClassUtils;

@PluginImplementation
public class DataHandlerFactoryRegistryImpl implements DataHandlerFactoryRegistry {
	
	private Map<String, DataHandlerFactory<? extends DataHandlerFactoryConfig>> factories = new HashMap<>();
	private PluginRegistry plugins;
	
	@Override
	public DataHandlerFactory<? extends DataHandlerFactoryConfig> get(String name) {
		return factories.get(name);
	}
	
	@Override
	public DataHandlerFactoryRegistry register(DataHandlerFactory<? extends DataHandlerFactoryConfig> factory) {
		register(factory.getName(), factory);
		return this;
	}

	@Override
	public DataHandlerFactoryRegistry register(String name, DataHandlerFactory<? extends DataHandlerFactoryConfig> factory) {
		factories.put(name, factory);
		return this;
	}

	@Override
	public List<String> getDataHandlerFactories() {
		return new ArrayList<String>(factories.keySet());
	}

	@Override
	public Integer getPriority() {
		return 2500;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processPlugins() {
		for(Plugin plugin : this.plugins.getPlugins()) {
			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(plugin.getClass());
			if(interfaces.contains(DataHandlerFactory.class)) {
				register((DataHandlerFactory<? extends DataHandlerFactoryConfig>) plugin);
			}
			if(interfaces.contains(DataHandlerFactoryProvider.class)) {
				for(DataHandlerFactory<? extends DataHandlerFactoryConfig> factory : ((DataHandlerFactoryProvider) plugin).getDataHandlerFactories()) {
					register(factory);
				}
			}
		}
	}

	@Override
	public void setRegistry(PluginRegistry registry) {
		this.plugins = registry;
	}

}
