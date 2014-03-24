package io.core9.plugin.widgets.datahandler.factories;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

public interface BundleDataHandler<T extends DataHandlerFactoryConfig> extends Core9Plugin, DataHandlerFactory<T> {

}
