package io.core9.plugin.widgets.datahandler;

public class DataHandlerGlobal<T> {
	private boolean isGlobal;
	private T value;
		
	/**
	 * @return the isGlobal
	 */
	public boolean isGlobal() {
		return isGlobal;
	}
	
	/**
	 * @param isGlobal the isGlobal to set
	 */
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}

}
