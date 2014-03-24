package io.core9.plugin.widgets.pagemodel;

import io.core9.core.PluginRegistry;
import io.core9.plugin.admin.AbstractAdminPlugin;
import io.core9.plugin.admin.plugins.AdminConfigRepository;
import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.exceptions.ComponentDoesNotExists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import org.apache.commons.lang3.ClassUtils;

@PluginImplementation
public class PageModelAdminPluginImpl extends AbstractAdminPlugin implements PageModelAdminPlugin {
	
	private PluginRegistry registry;
	
	@InjectPlugin
	private AdminConfigRepository config;
	
	@InjectPlugin
	private PageModelFactory factory;
	
	@InjectPlugin
	private HostManager hostManager;
	
	@Override
	public String getControllerName() {
		return "pagemodel";
	}

	@Override
	protected void process(Request request) {
		switch(request.getMethod()) {
		case POST:
			VirtualHost vhost = request.getVirtualHost();
			factory.clear(vhost);
			factory.registerAll(vhost, getCodeModels());
			factory.registerAll(vhost, getDataModels(vhost));
			try {
				factory.processVhost(vhost);
			} catch (ComponentDoesNotExists e) {
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
	protected void process(Request request, String type) {}

	@Override
	protected void process(Request request, String type, String id) {}

	@Override
	public Integer getPriority() {
		return 2520;
	}

	@Override
	public void processPlugins() {
		// Process code models
		factory.registerOnAll(getCodeModels());
		for(VirtualHost vhost : hostManager.getVirtualHosts()) {
			try {
				factory
					.registerAll(vhost, getDataModels(vhost))
					.processVhost(vhost);
			} catch (ComponentDoesNotExists e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<PageModel> getCodeModels() {
		List<PageModel> codeModels = new ArrayList<PageModel>();
		for(Plugin plugin : this.registry.getPlugins()) {
			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(plugin.getClass());
			if(interfaces.contains(PageModelProvider.class)) {
				codeModels.addAll(((PageModelProvider) plugin).getPageModels());
			}
		}
		return codeModels;
	}
	
	private List<PageModel> getDataModels(VirtualHost vhost) {
		List<PageModel> pageModels = new ArrayList<PageModel>();
		for(Map<String,Object> pageModel : config.getConfigList(vhost, "pagemodel")) {
			pageModels.add(factory.parse(pageModel));
		}
		return pageModels;
	}

	@Override
	public void setRegistry(PluginRegistry registry) {
		this.registry = registry;
	}

}
