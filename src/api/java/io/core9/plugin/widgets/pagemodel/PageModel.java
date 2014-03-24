package io.core9.plugin.widgets.pagemodel;

import io.core9.plugin.widgets.Component;

import java.util.List;

public interface PageModel {
	
	String getName();
	PageModel setName(String name);
	
	String getTemplateName();
	PageModel setTemplateName(String templateName);
	
	String getPath();
	PageModel setPath(String string);
	
	List<Component> getComponents();
	PageModelImpl setComponents(List<Component> components);
	
	PageModel addComponent(Component component);
	PageModel addComponents(List<Component> components);
	List<String> getRoles();
	PageModelImpl addRoles(List<String> roles);
	PageModelImpl removeRole(String role);
	PageModelImpl removeRoles(List<String> delRoles);
	PageModelImpl addRole(String role);
	PageModelImpl setRequestRole(String requestRole);
	String getRequestRole();
}
