package io.core9.plugin.widgets.pagemodel;

import io.core9.core.boot.BootStrategy;
import io.core9.core.executor.Executor;
import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.admin.AdminPlugin;
import io.core9.plugin.server.VirtualHostProcessor;

public interface PageModelAdminPlugin extends AdminPlugin, Core9Plugin, BootStrategy, VirtualHostProcessor, Executor {

}
