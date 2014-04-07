package io.core9.plugin.widgets.pagemodel;

import io.core9.plugin.database.repository.AbstractCrudEntity;
import io.core9.plugin.database.repository.Collection;
import io.core9.plugin.widgets.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Collection("configuration")
public class PageModelImpl extends AbstractCrudEntity implements PageModel {

	private String templateName;
	private String name;
	private String path;
	private Set<String> roles;
	private Set<String> permissions;
	private List<Component> components;
	
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
		if(components == null) {
			components = new ArrayList<Component>();
		}
		return this.components;
	}

	@Override
	public PageModelImpl setComponents(List<Component> components) {
		this.components = components;
		return this;
	}

	@Override
	public Set<String> getRoles() {
		if(roles == null) {
			roles = new HashSet<String>();
		}
		return roles;
	}

	@Override
	public PageModel setRoles(Set<String> roles) {
		this.roles = roles;
		return this;
	}

	@Override
	public Set<String> getPermissions() {
		if(permissions == null) {
			permissions = new HashSet<String>();
		}
		return this.permissions;
	}

	@Override
	public PageModel setPermissions(Set<String> permissions) {
		this.permissions = permissions;
		return this;
	}
	
}
