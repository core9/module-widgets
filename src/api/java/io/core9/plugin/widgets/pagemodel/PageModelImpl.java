package io.core9.plugin.widgets.pagemodel;

import io.core9.plugin.widgets.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageModelImpl implements PageModel {

	private String templateName;
	private String name;
	private String path;
	private List<String> roles;
	private List<Component> components = new ArrayList<Component>();
	private String requestRole;
	
	@Override
	public PageModelImpl setRequestRole(String requestRole){
		this.requestRole = requestRole;
		return this;
	}

	@Override
	public String getRequestRole(){
		return requestRole;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public PageModelImpl setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String getTemplateName() {
		return templateName;
	}

	@Override
	public PageModelImpl setTemplateName(String templateName) {
		this.templateName = templateName;
		return this;
	}

	@Override
	public String getPath() {
		if (this.path == null) {
			return "/" + this.name;
		} else {
			return this.path;
		}
	}

	@Override
	public PageModelImpl setPath(String string) {
		this.path = string;
		return this;
	}

	@Override
	public List<Component> getComponents() {
		return this.components;
	}

	@Override
	public PageModelImpl setComponents(List<Component> components) {
		this.components = components;
		return this;
	}

	@Override
	public PageModelImpl addComponent(Component component) {
		this.components.add(component);
		return this;
	}

	@Override
	public PageModelImpl addComponents(List<Component> components) {
		this.components.addAll(components);
		return this;
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	@Override
	public PageModelImpl addRoles(List<String> roles) {
		for (String role : roles) {
			roles.add(role);
		}
		return this;
	}

	@Override
	public PageModelImpl removeRole(String role) {
		Iterator<String> iter = roles.iterator();
		while (iter.hasNext()) {
			if (iter.next().equalsIgnoreCase(role)) {
				iter.remove();
			}
		}
		return this;
	}

	@Override
	public PageModelImpl removeRoles(List<String> delRoles) {
		Iterator<String> iter = roles.iterator();
		while (iter.hasNext()) {
			if (delRoles.contains(iter.next().toLowerCase().trim())) {
				iter.remove();
			}
		}
		return this;
	}

	@Override
	public PageModelImpl addRole(String role) {
		roles.add(role);
		return this;
	}

	/**
	 * Parse a pagemodel from a config item
	 * 
	 * @param config
	 * @return
	 */
	// public static PageModelImpl parsePageModelFromConfig(Map<String, Object>
	// config) {
	// PageModelImpl model = new PageModelImpl();
	// try {
	// for(Map<String, Object> component : (List<Map<String, Object>>)
	// config.get("components")) {
	// model.addComponent(component);
	// }
	// } catch (Exception e) {
	// System.out.println(" component exeption");
	// }
	//
	// model.setPath((String) config.get("path"));
	// model.setName((String) config.get("name"));
	// model.setTemplateName((String) config.get("templatename"));
	// return model;
	// }

	// private static List<Component> parseComponents(List<Map<String,Object>>
	// components) {
	// List<Component> result = new ArrayList<Component>();
	// }

}
