package io.core9.plugin.widgets;

import java.util.HashMap;
import java.util.Map;

public class Component {
	private String id;
	private String name;
	private Map<String, String> globals;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the globals
	 */
	public Map<String, String> getGlobals() {
		return globals;
	}

	/**
	 * @param globals the globals to set
	 */
	public void setGlobals(Map<String, String> globals) {
		this.globals = globals;
	}
	
	/**
	 * Create an empty component;
	 */
	public Component() {
		
	}
	
	/**
	 * Creates a component
	 * @param name
	 */
	public Component(String name) {
		this.name = name;
		this.globals = new HashMap<String,String>();
	}
	
	/**
	 * Create a component
	 * @param name
	 * @param globals
	 */
	public Component(String name, Map<String,String> globals) {
		this.name = name;
		this.globals = globals;
	}
	
}
