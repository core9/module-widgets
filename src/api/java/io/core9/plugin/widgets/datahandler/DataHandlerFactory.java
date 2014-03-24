package io.core9.plugin.widgets.datahandler;

public interface DataHandlerFactory<T extends DataHandlerFactoryConfig> {
	
	/**
	 * Return the name of the DataHandlerFactory
	 * @return
	 */
	String getName();
	
	/**
	 * Return the configuration class (or null for none)
	 * @return
	 */
	Class<? extends DataHandlerFactoryConfig> getConfigClass();
	
	/**
	 * Creates the datahandler
	 * @param options
	 * @return
	 */
	DataHandler<T> createDataHandler(DataHandlerFactoryConfig options);
}
