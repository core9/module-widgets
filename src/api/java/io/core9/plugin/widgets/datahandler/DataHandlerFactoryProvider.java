package io.core9.plugin.widgets.datahandler;

import java.util.List;

public interface DataHandlerFactoryProvider {
	List<DataHandlerFactory<? extends DataHandlerFactoryConfig>> getDataHandlerFactories();
}
