package io.core9.plugin.widgets.widget;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.server.VirtualHost;

import java.util.List;
import java.util.Map;

public interface WidgetFactory extends Core9Plugin {
	
	/**
	 * Parse a widget from a configuration object
	 * @param config
	 * @return
	 */
	Widget parse(Map<String,Object> config);
	
	/**
	 * Register a widget on a virtual host
	 * @param vhost
	 * @param widget
	 * @return
	 */
	WidgetFactory register(VirtualHost vhost, Widget widget);
	
	/**
	 * Register all widgets on a virtual host
	 * @param vhost
	 * @param widgets
	 * @return
	 */
	WidgetFactory registerAll(VirtualHost vhost, List<Widget> widgets);
	
	/**
	 * Register a widget on all virtual hosts
	 * @param widget
	 * @return
	 */
	WidgetFactory registerOnAll(Widget widget);
	
	/**
	 * Register a collection of widgets on all virtual hosts
	 * @param widgets
	 * @return
	 */
	WidgetFactory registerOnAll(List<Widget> widgets);

	/**
	 * Process a virtual host
	 * @param vhost
	 * @return
	 */
	WidgetFactory processVhost(VirtualHost vhost);

	/**
	 * Return the complete registry for a virtual host
	 * @return
	 */
	Map<String,Widget> getRegistry(VirtualHost vhost);
}
