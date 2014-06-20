package io.core9.plugin.widgets.widget;

import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.template.closure.ClosureTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public final class WidgetFactoryImpl implements WidgetFactory {

	@InjectPlugin
	private ClosureTemplateEngine engine;

	@InjectPlugin
	private HostManager hostManager;
	
	private Map<VirtualHost,Map<String, Widget>> registry = new HashMap<VirtualHost,Map<String, Widget>>();	
	
	@Override
	public WidgetFactory register(VirtualHost vhost, Widget widget) {
		if(!registry.containsKey(vhost)) {
			registry.put(vhost, new HashMap<String,Widget>());
		}
		registry.get(vhost).put(widget.getId(), widget);
		return this;
	}

	@Override
	public WidgetFactory registerAll(VirtualHost vhost, List<? extends Widget> widgets) {
		if(!registry.containsKey(vhost)) {
			registry.put(vhost, new HashMap<String,Widget>());
		}
		for(Widget widget : widgets) {
			registry.get(vhost).put(widget.getId(), widget);
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
			engine.addString(vhost, widget.getTemplateName() + ".soy", widget.getTemplate());
		}
		engine.createCache(vhost);
		return this;
	}

	@Override
	public Map<String, Widget> getRegistry(VirtualHost vhost) {
		return registry.get(vhost);
	}

	@Override
	public void clear(VirtualHost vhost) {
		registry.get(vhost).clear();
	}
}
