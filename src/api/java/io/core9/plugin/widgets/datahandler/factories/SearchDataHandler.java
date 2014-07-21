package io.core9.plugin.widgets.datahandler.factories;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

public interface SearchDataHandler<T extends DataHandlerFactoryConfig> extends DataHandlerFactory<T>, Core9Plugin {

}
