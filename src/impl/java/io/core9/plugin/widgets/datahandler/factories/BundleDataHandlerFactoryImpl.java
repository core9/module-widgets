package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.Component;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;
import io.core9.plugin.widgets.widget.Widget;
import io.core9.plugin.widgets.widget.WidgetFactory;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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
			public Map<String, Object> handle(final Request req) {
				final Map<String, Object> result = new HashMap<String, Object>();
				if(options.getComponents() == null) {
					return result;
				}
				Observable.from(options.getComponents()).parallel(new Func1<Observable<Component>, Observable<Map.Entry<String,Object>>>() {

					@Override
					public Observable<Entry<String, Object>> call(Observable<Component> obs) {
						return obs.map(new Func1<Component, Map.Entry<String,Object>>() {

							@Override
							public Entry<String, Object> call(Component component) {
								if(component.getGlobals().size() > 0) {
									for(Map.Entry<String,String> entry : component.getGlobals().entrySet()) {
										if(entry.getValue().startsWith(":")) {
											req.putContext(component.getName() + "." + entry.getKey(), req.getContext(entry.getValue().substring(1)));
										} else {
											req.putContext(component.getName() + "." + entry.getKey(), entry.getValue());
										}
									}
								}
								Widget widget = widgets.getRegistry(req.getVirtualHost()).get(component.getId());
								if(widget != null && widget.getDataHandler() != null) {
									return new AbstractMap.SimpleEntry<String,Object>(component.getName(), widget.getDataHandler().handle(req));
								} else {
									return new AbstractMap.SimpleEntry<String,Object>(component.getName(), new HashMap<String,Object>());
								}
							}
						});
					}
				}).toBlocking().forEach(new Action1<Entry<String,Object>>() {

					@Override
					public void call(Entry<String, Object> t1) {
						result.put(t1.getKey(), t1.getValue());
						System.out.println(t1.getKey());
					}
					
				});
				return result;
			}

			@Override
			public BundleDataHandlerFactoryOptions getOptions() {
				return (BundleDataHandlerFactoryOptions) options;
			}
		};
	}
}
