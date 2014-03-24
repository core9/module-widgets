package io.core9.plugin.widgets.datahandler;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.server.VirtualHost;

public interface DataHandlerOptionsParser extends Core9Plugin {

	String parse(VirtualHost virtualHost, Class<?> configClass);

}
