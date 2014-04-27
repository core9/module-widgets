package io.core9.plugin.widgets.pagemodel;

import io.core9.plugin.database.repository.CrudEntity;
import io.core9.plugin.widgets.Component;

import java.util.List;
import java.util.Set;

public interface PageModel extends CrudEntity {
	
	String getName();
	PageModel setName(String name);
	
	String getTemplateName();
	PageModel setTemplateName(String templateName);
	
	String getPath();
	PageModel setPath(String string);
	
	List<Component> getComponents();
	PageModel setComponents(List<Component> components);
		
	Set<String> getRoles();
	PageModel setRoles(Set<String> roles);
	
	Set<String> getPermissions();
	PageModel setPermissions(Set<String> permissions);

}
