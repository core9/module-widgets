package io.core9.plugin.widgets.datahandler;

import io.core9.core.boot.BootStrategy;
import io.core9.core.plugin.Core9Plugin;

import java.util.List;

public interface DataHandlerFactoryRegistry extends Core9Plugin, BootStrategy {
	
	DataHandlerFactoryRegistry register(DataHandlerFactory<? extends DataHandlerFactoryConfig> factory);

	DataHandlerFactoryRegistry register(String name, DataHandlerFactory<? extends DataHandlerFactoryConfig> factory);
	
	DataHandlerFactory<? extends DataHandlerFactoryConfig> get(String name);
	
	List<String> getDataHandlerFactories();
}
