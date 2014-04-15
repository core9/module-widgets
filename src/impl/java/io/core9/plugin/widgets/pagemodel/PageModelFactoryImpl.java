package io.core9.plugin.widgets.pagemodel;

import io.core9.module.auth.AuthenticationPlugin;
import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.handler.Middleware;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.server.vertx.VertxServer;
import io.core9.plugin.template.closure.ClosureTemplateEngine;
import io.core9.plugin.widgets.Component;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.exceptions.ComponentDoesNotExists;
import io.core9.plugin.widgets.widget.Widget;
import io.core9.plugin.widgets.widget.WidgetFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class PageModelFactoryImpl implements PageModelFactory {
	
	@InjectPlugin
	private AuthenticationPlugin authentication;
	
	@InjectPlugin
	private WidgetFactory widgets;
	
	@InjectPlugin 
	private HostManager hostManager;
	
	@InjectPlugin
	private VertxServer server;
	
	@InjectPlugin
	private ClosureTemplateEngine engine;

	private Map<VirtualHost,Map<String, PageModel>> registry = new HashMap<VirtualHost,Map<String, PageModel>>();
	
	@Override
	public PageModelFactory register(VirtualHost vhost, PageModel pageModel) {
		if(!registry.containsKey(vhost)) {
			registry.put(vhost, new HashMap<String,PageModel>());
		}
		registry.get(vhost).put(pageModel.getName(), pageModel);
		return this;
	}

	@Override
	public PageModelFactory registerAll(VirtualHost vhost, List<? extends PageModel> pageModels) {
		if(!registry.containsKey(vhost)) {
			registry.put(vhost, new HashMap<String,PageModel>());
		}
		for(PageModel pageModel : pageModels) {
			registry.get(vhost).put(pageModel.getName(), pageModel);
		}
		return this;
	}
	
	@Override
	public PageModelFactory registerOnAll(PageModel pageModel) {
		for(VirtualHost vhost: hostManager.getVirtualHosts()) {
			register(vhost,pageModel);
		}
		return this;
	}

	@Override
	public PageModelFactory registerOnAll(List<? extends PageModel> pageModels) {
		for(VirtualHost vhost: hostManager.getVirtualHosts()) {
			registerAll(vhost,pageModels);
		}
		return this;
	}
	
	@Override
	public PageModelFactory processVhost(VirtualHost vhost) {
		for(PageModel pageModel : registry.get(vhost).values()) {
			try {
				PageModelTemplate template = parseTemplate(pageModel, widgets.getRegistry(vhost));
				engine.addString(template.getFilename(), template.toString());
				server.use(vhost, pageModel.getPath(), createMiddleware(pageModel, widgets.getRegistry(vhost)));
			} catch (ComponentDoesNotExists e) {
				e.printStackTrace();
			}
		}
		engine.createCache();
		return this;
	}
	
	private static PageModelTemplate parseTemplate(PageModel model, Map<String,Widget> widgets) throws ComponentDoesNotExists {
		PageModelTemplate template = new PageModelTemplate();
		template.setName(model.getTemplateName());
		for(Component component : model.getComponents()) {
			Widget widget = widgets.get(component.getName());
			if(widget == null) {
				System.out.println("Component : " + component.getName() + " does not exist!");
				throw new ComponentDoesNotExists();
			}
			template.addWidget(widget);
		}
		return template;
	}
	
	/**
	 * Create a Middleware, depending on the page model roles/permissions
	 * TODO: Cleanup
	 * @param model
	 * @param widgets
	 * @return
	 * @throws ComponentDoesNotExists
	 */
	private Middleware createMiddleware(final PageModel model, final Map<String,Widget> widgets) throws ComponentDoesNotExists {
		if((model.getRoles().isEmpty() && model.getPermissions().isEmpty()) || System.getProperty("DEBUG") != null) {
			return new Middleware() {
				@Override
				public void handle(Request req) {
					req.getResponse().setTemplate("io.core9." + model.getTemplateName());
					req.getResponse().addValue("data", makeDataObject(req, model, widgets));
				}
			};
		} else if (model.getPermissions().isEmpty()) {
			return new Middleware() {
				@Override
				public void handle(Request req) {
					if(authentication.getUser(req).hasRole(model.getRoles())) {
						req.getResponse().setTemplate("io.core9." + model.getTemplateName());
						req.getResponse().addValue("data", makeDataObject(req, model, widgets));
					} else {
						req.getResponse().setStatusCode(401);
					}
				}
			};
		} else if (model.getRoles().isEmpty()) {
			return new Middleware() {
				@Override
				public void handle(Request req) {
					if(authentication.getUser(req).isPermitted(model.getPermissions())) {
						req.getResponse().setTemplate("io.core9." + model.getTemplateName());
						req.getResponse().addValue("data", makeDataObject(req, model, widgets));
					} else {
						req.getResponse().setStatusCode(401);
					}
				}
			};
		} else {
			return new Middleware() {
				@Override
				public void handle(Request req) {
					if(authentication.getUser(req).isPermitted(model.getPermissions()) &&
							authentication.getUser(req).hasRole(model.getRoles())) {
						req.getResponse().setTemplate("io.core9." + model.getTemplateName());
						req.getResponse().addValue("data", makeDataObject(req, model, widgets));
					} else {
						req.getResponse().setStatusCode(401);
					}
				}
			};
		}
	}
	
	private static Map<String, Object> makeDataObject(Request req, PageModel model, Map<String,Widget> widgets) {
		Map<String,Object> data = new HashMap<String,Object>();
		for(Component component : model.getComponents()) {
			Widget widget = widgets.get(component.getName());
			if(component.getGlobals().size() > 0) {
				for(Map.Entry<String,String> entry : component.getGlobals().entrySet()) {
					if(entry.getValue() != null && entry.getValue().startsWith(":")) {
						req.putContext(component.getId() + "." + entry.getKey(), req.getParams().get(entry.getValue().substring(1)));
					} else {
						req.putContext(component.getId() + "." + entry.getKey(), entry.getValue());
					}
				}
			}
			if(widget != null) {
				DataHandler<?> handler;
				if((handler = widget.getDataHandler()) == null) {
					data.put(component.getId(), new HashMap<String,Object>());
				} else {
					handler.getOptions().setComponentId(component.getId());
					data.put(component.getId(), handler.handle(req));
				}
			}
		}
		return data;
	}

	@Override
	public PageModelFactory clear(VirtualHost vhost) {
		for(PageModel pageModel : this.registry.get(vhost).values()) {
			server.deregister(vhost, pageModel.getPath());
		}
		this.registry.get(vhost).clear();
		return this;
	}
}
