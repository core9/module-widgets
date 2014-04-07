package io.core9.plugin.widgets.pagemodel;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.widgets.exceptions.ComponentDoesNotExists;

import java.util.List;

public interface PageModelFactory extends Core9Plugin {

	PageModelFactory register(VirtualHost vhost, PageModel pageModel);

	PageModelFactory registerAll(VirtualHost virtualHost, List<? extends PageModel> pageModels);
	
	PageModelFactory registerOnAll(PageModel pageModels);
	
	PageModelFactory registerOnAll(List<? extends PageModel> pageModels);

	PageModelFactory processVhost(VirtualHost vhost) throws ComponentDoesNotExists;
	
	PageModelFactory clear(VirtualHost vhost);
}
