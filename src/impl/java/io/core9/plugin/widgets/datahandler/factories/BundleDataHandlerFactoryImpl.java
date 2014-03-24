package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.Component;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;
import io.core9.plugin.widgets.widget.Widget;
import io.core9.plugin.widgets.widget.WidgetFactory;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class BundleDataHandlerFactoryImpl implements BundleDataHandler<BundleDataHandlerFactoryOptions> {

	@InjectPlugin
	private WidgetFactory widgets;
	
	@Override
	public String getName() {
		return "Bundle";
	}

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return BundleDataHandlerFactoryOptions.class;
	}

	@Override
	public DataHandler<BundleDataHandlerFactoryOptions> createDataHandler(final DataHandlerFactoryConfig config) {
		final BundleDataHandlerFactoryOptions options = (BundleDataHandlerFactoryOptions) config;
		return new DataHandler<BundleDataHandlerFactoryOptions>() {
			
			@Override
			public Map<String, Object> handle(Request req) {
				Map<String, Object> result = new HashMap<String, Object>();
				for(Component component : options.getComponents()) {
					if(component.getGlobals().size() > 0) {
						for(Map.Entry<String,String> entry : component.getGlobals().entrySet()) {
							if(entry.getValue().startsWith(":")) {
								req.putContext(component.getId() + "." + entry.getKey(), req.getContext(entry.getValue().substring(1)));
							} else {
								req.putContext(component.getId() + "." + entry.getKey(), entry.getValue());
							}
						}
					}
					Widget widget = widgets.getRegistry(req.getVirtualHost()).get(component.getName());
					DataHandler<?> handler;
					if((handler = widget.getDataHandler()) != null) {
						handler.getOptions().setComponentId(component.getId());
						result.put(component.getId(), handler.handle(req));
					} else {
						result.put(component.getId(), new HashMap<String,Object>());
					}
				}
				return result;
			}

			@Override
			public BundleDataHandlerFactoryOptions getOptions() {
				return (BundleDataHandlerFactoryOptions) options;
			}
		};
	}
}
