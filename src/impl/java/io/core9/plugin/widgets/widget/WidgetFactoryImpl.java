package io.core9.plugin.widgets.widget;

import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.template.closure.ClosureTemplateEngine;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public final class WidgetFactoryImpl implements WidgetFactory {
	
	@InjectPlugin
	private DataHandlerFactoryRegistry dataHandlerFactories;

	@InjectPlugin
	private ClosureTemplateEngine engine;

	@InjectPlugin
	private HostManager hostManager;
	
	private Map<VirtualHost,Map<String, Widget>> registry = new HashMap<VirtualHost,Map<String, Widget>>();	
	
	@Override
	public Widget parse(Map<String,Object> config) {
		final DozerBeanMapper mapper = new DozerBeanMapper();
		WidgetImpl widget = mapper.map(config, WidgetImpl.class);
		DataHandlerFactory<? extends DataHandlerFactoryConfig> dataHandlerFactory = (DataHandlerFactory<? extends DataHandlerFactoryConfig>) dataHandlerFactories.get((String) config.get("handler"));
		DataHandlerFactoryConfig handlerconfig = null;
		if(dataHandlerFactory != null && dataHandlerFactory.getConfigClass() != null) {
			// Map the configuration data to the configuration class
			handlerconfig = mapper.map(config.get("handleroptions"), dataHandlerFactory.getConfigClass());
			widget.setDataHandler(dataHandlerFactory.createDataHandler(handlerconfig));
		}
		return widget;
	}

	@Override
	public WidgetFactory register(VirtualHost vhost, Widget widget) {
		if(!registry.containsKey(vhost)) {
			registry.put(vhost, new HashMap<String,Widget>());
		}
		registry.get(vhost).put(widget.getName(), widget);
		return this;
	}
	

	@Override
	public WidgetFactory registerAll(VirtualHost vhost, List<? extends Widget> widgets) {
		if(!registry.containsKey(vhost)) {
			registry.put(vhost, new HashMap<String,Widget>());
		}
		for(Widget widget : widgets) {
			registry.get(vhost).put(widget.getName(), widget);
		}
		return this;
	}

	@Override
	public WidgetFactory registerOnAll(Widget widget) {
		for(VirtualHost vhost : hostManager.getVirtualHosts()) {
			register(vhost, widget);
		}
		return this;
	}
	
	@Override
	public WidgetFactory registerOnAll(List<? extends Widget> widgets) {
		for(Widget widget : widgets) {
			registerOnAll(widget);
		}
		return this;
	}
	
	@Override
	public WidgetFactory processVhost(VirtualHost vhost) {
		for(Widget widget : registry.get(vhost).values()) {
			engine.addString(widget.getTemplateName() + ".soy", widget.getTemplate());
		}
		engine.createCache();
		return this;
	}

	@Override
	public Map<String, Widget> getRegistry(VirtualHost vhost) {
		return registry.get(vhost);
	}
}
