package io.core9.plugin.widgets.datahandler.factories;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;

public interface BundleDataHandler<T extends BundleDataHandlerFactoryOptions> extends Core9Plugin, DataHandlerFactory<T> {

}
