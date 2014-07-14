package io.core9.plugin.widgets.datahandler.factories;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGlobal<T> {
	
	public static Map<String,Object> convertToQuery(List<CustomGlobal<Object>> items, Request req, String component) {
		Map<String,Object> query = new HashMap<String,Object>();
		if(items == null) {
			return query;
		}
		items.forEach(item -> {
			query.put(item.getKey().replace('#', '.'), item.getValue(req, component));
		});
		return query;
	}

	private String key;
	private DataHandlerGlobal<T> value;
	
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public DataHandlerGlobal<T> getValue() {
		return value;
	}
	
	public void setValue(DataHandlerGlobal<T> value) {
		this.value = value;
	}
	
	public T getValue(Request req, String component) {
		if(value.isGlobal()) {
			return req.getContext(component + "." + this.key, value.getValue());
		}
		return value.getValue();
	}
	
	
}
