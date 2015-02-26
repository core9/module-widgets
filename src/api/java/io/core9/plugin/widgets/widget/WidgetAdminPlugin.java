package io.core9.plugin.widgets.widget;

import io.core9.core.boot.BootStrategy;
import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.admin.AdminPlugin;
import io.core9.plugin.server.VirtualHost;

public interface WidgetAdminPlugin extends Core9Plugin, AdminPlugin, BootStrategy {
	
	void addVirtualHost(VirtualHost vhost);
	
	void removeVirtualHost(VirtualHost vhost);
	
}
