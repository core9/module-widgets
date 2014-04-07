package io.core9.plugin.widgets.widget;

import io.core9.core.PluginRegistry;
import io.core9.plugin.admin.AbstractAdminPlugin;
import io.core9.plugin.database.repository.CrudRepository;
import io.core9.plugin.database.repository.NoCollectionNamePresentException;
import io.core9.plugin.database.repository.RepositoryFactory;
import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryRegistry;
import io.core9.plugin.widgets.datahandler.DataHandlerOptionsParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import org.apache.commons.lang3.ClassUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.google.template.soy.base.SoySyntaxException;

@PluginImplementation
public class WidgetAdminPluginImpl extends AbstractAdminPlugin implements WidgetAdminPlugin {
	
	@InjectPlugin
	private WidgetFactory factory;
	
	@InjectPlugin
	private DataHandlerFactoryRegistry datahandlerFactories;
	
	@InjectPlugin
	private RepositoryFactory repository;
	
	@InjectPlugin
	private DataHandlerOptionsParser parser;
	
	@InjectPlugin
	private HostManager hostManager;

	private PluginRegistry registry;

	@Override
	public String getControllerName() {
		return "widget";
	}

	@Override
	protected void process(Request request) {
		switch(request.getMethod()) {
		case GET:
			Map<String,Object> result = new HashMap<String,Object>();
			for(Map.Entry<String,Widget> entry : factory.getRegistry(request.getVirtualHost()).entrySet()) {
				result.put(entry.getKey(), entry.getValue().getDataHandler());
			}
			request.getResponse().sendJsonMap(result);
			break;
		case POST:
			try {
				factory
					.registerAll(request.getVirtualHost(), getDataWidgets(request.getVirtualHost()))
					.processVhost(request.getVirtualHost());
			} catch (SoySyntaxException e) {
				request.getResponse().setStatusCode(500);
				request.getResponse().addValue("error", e.getMessage());
			}
			break;
		default:
			request.getResponse().setStatusCode(404);
			request.getResponse().end();
		}
	}

	@Override
	protected void process(Request request, String type) {
		if(type.equals("datahandler")) {
			request.getResponse().sendJsonArray(datahandlerFactories.getDataHandlerFactories());
		}
	}

	@Override
	protected void process(Request request, String type, String id) {
		if(type.equals("datahandler")) {
			ObjectMapper mapper = new ObjectMapper();
	        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
	        Class<?> configClass = datahandlerFactories.get(id).getConfigClass();
	        if(configClass == null) {
	        	request.getResponse().end();
	        } else {
	        	try {
	        		mapper.acceptJsonFormatVisitor(configClass, visitor);
	        		JsonSchema schema = visitor.finalSchema();
	        		String result = "{";
	        		result += "\"schema\": " + mapper.writeValueAsString(schema);
	        		result += ", \"options\": " + parser.parse(request.getVirtualHost(), configClass);
	        		result += "}";
	        		request.getResponse().end(result);
	        	} catch (JsonProcessingException e) {
	        		e.printStackTrace();
	        		request.getResponse().setStatusCode(500);
	        		request.getResponse().setStatusMessage(e.getMessage());
	        	}
	        }
		}
	}

	@Override
	public Integer getPriority() {
		return 2510;
	}

	@Override
	public void processPlugins() {
		factory.registerOnAll(getCodeWidgets());
		for(VirtualHost vhost : hostManager.getVirtualHosts()) {
			factory
				.registerAll(vhost, getDataWidgets(vhost))
				.processVhost(vhost);
		}
	}

	@Override
	public void setRegistry(PluginRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * Retrieve the code widgets
	 * @return
	 */
	private List<Widget> getCodeWidgets() {
		List<Widget> codeWidgets = new ArrayList<Widget>();
		for(Plugin plugin : this.registry.getPlugins()) {
			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(plugin.getClass());
			if(interfaces.contains(Widget.class)) {
				codeWidgets.add((Widget) plugin);
			}
			if(interfaces.contains(WidgetProvider.class)) {
				codeWidgets.addAll(((WidgetProvider) plugin).getWidgets());
			}
		}
		return codeWidgets;
	}

	/**
	 * Retrieve the data widgets
	 * @param vhost
	 * @return
	 */
	private List<? extends Widget> getDataWidgets(VirtualHost vhost) {
		List<? extends Widget> widgets = new ArrayList<Widget>();
		try {
			CrudRepository<WidgetImpl> crud = repository.getRepository(WidgetImpl.class);
			Map<String,Object> query = new HashMap<String,Object>();
			query.put("configtype", "widget");
			widgets = crud.query(vhost, query);
			ObjectMapper mapper = new ObjectMapper();
			for(Widget widget : widgets) {
				setupDataHandler(widget, mapper);
			}
		} catch (NoCollectionNamePresentException e) {
			e.printStackTrace();
		}
		return widgets;
	}

	/**
	 * Setup the datahandler
	 * @param widget
	 * @param mapper
	 */
	private void setupDataHandler(Widget widget, ObjectMapper mapper) {
		DataHandlerFactory<? extends DataHandlerFactoryConfig> dataHandlerFactory = (DataHandlerFactory<? extends DataHandlerFactoryConfig>) datahandlerFactories.get(widget.getHandler());
		DataHandlerFactoryConfig handlerconfig = null;
		if(dataHandlerFactory != null && dataHandlerFactory.getConfigClass() != null) {
			// Map the configuration data to the configuration class
			if(widget.getHandlerOptions() == null) {
				try {
					handlerconfig = dataHandlerFactory.getConfigClass().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				handlerconfig = mapper.convertValue(widget.getHandlerOptions(), dataHandlerFactory.getConfigClass());
			}
			widget.setDataHandler(dataHandlerFactory.createDataHandler(handlerconfig));
		}		
	}
}