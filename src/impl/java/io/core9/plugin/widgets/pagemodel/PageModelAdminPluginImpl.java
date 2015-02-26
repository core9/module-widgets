package io.core9.plugin.widgets.pagemodel;

import io.core9.core.PluginRegistry;
import io.core9.plugin.admin.AbstractAdminPlugin;
import io.core9.plugin.database.repository.CrudRepository;
import io.core9.plugin.database.repository.NoCollectionNamePresentException;
import io.core9.plugin.database.repository.RepositoryFactory;
import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.exceptions.ComponentDoesNotExists;

import java.util.ArrayList;
import java.util.List;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.PluginLoaded;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import org.apache.commons.lang3.ClassUtils;

@PluginImplementation
public class PageModelAdminPluginImpl extends AbstractAdminPlugin implements PageModelAdminPlugin {
	
	private PluginRegistry registry;
	private List<PageModel> codeModels = new ArrayList<PageModel>();

	@InjectPlugin
	private PageModelFactory factory;
	
	@InjectPlugin
	private HostManager hostManager;

	private CrudRepository<PageModelImpl> repository;
	
	@PluginLoaded
	public void onRepositoryFactory(RepositoryFactory factory) throws NoCollectionNamePresentException {
		this.repository = factory.getRepository(PageModelImpl.class);
	}
	
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
			factory.registerAll(vhost, codeModels);
			factory.registerAll(vhost, getDataModels(vhost));
			try {
				factory.processVhost(vhost);
				request.getResponse().end("Success");
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

	private List<? extends PageModel> getDataModels(VirtualHost vhost) {
		return repository.getAll(vhost);
	}

	@Override
	public void processPlugins() {
		for(Plugin plugin : this.registry.getPlugins()) {
			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(plugin.getClass());
			if(interfaces.contains(PageModelProvider.class)) {
				codeModels.addAll(((PageModelProvider) plugin).getPageModels());
			}
		}
	}

	@Override
	public void setRegistry(PluginRegistry registry) {
		this.registry = registry;
	}

	@Override
	public Integer getPriority() {
		return 2520;
	}

	@Override
	public void addVirtualHost(VirtualHost vhost) {
		factory.registerAll(vhost, getDataModels(vhost));
	}

	@Override
	public void removeVirtualHost(VirtualHost vhost) {
		factory.clear(vhost);
	}

	@Override
	public void execute() {
		factory.registerOnAll(codeModels);
		for(VirtualHost vhost: hostManager.getVirtualHosts()) {
			try {
				factory.processVhost(vhost);
			} catch (ComponentDoesNotExists e) {
				e.printStackTrace();
			}
		}
	}

}
