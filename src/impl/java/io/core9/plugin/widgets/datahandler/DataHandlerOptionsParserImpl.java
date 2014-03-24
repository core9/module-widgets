package io.core9.plugin.widgets.datahandler;

import io.core9.plugin.admin.plugins.AdminConfigRepository;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.widgets.Core9Configuration;
import io.core9.plugin.widgets.Core9GlobalConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import org.apache.commons.lang3.ClassUtils;

@PluginImplementation
public class DataHandlerOptionsParserImpl implements DataHandlerOptionsParser {
	
	@InjectPlugin
	private AdminConfigRepository configRepository;

	public String parse(VirtualHost vhost, Class<?> configClass) {
		JSONObject result = new JSONObject();
		parseFields(vhost, result, configClass);
		parseMethodAnnotations(vhost, result, configClass);
		return result.toString();
	}
	
	private void parseFields(VirtualHost vhost, JSONObject result,	Class<?> configClass) {
		List<Field[]> fieldsList = new ArrayList<Field[]>();
		fieldsList.add(configClass.getDeclaredFields());
		for(Class<?> clazz : ClassUtils.getAllSuperclasses(configClass)) {
			fieldsList.add(clazz.getDeclaredFields());
		}
		for(Field[] fields: fieldsList) {
			for(Field field : fields) {
				if(field.getType().equals(DataHandlerGlobalString.class)) {
					JSONObject fieldJson = new JSONObject();
					fieldJson.put("widget", "textfield_with_global");
					fieldJson.put("label", field.getName());
					result.put(field.getName(), fieldJson);
				}
				System.out.println(field.getType());
			}
		}
	}

	private void parseMethodAnnotations(VirtualHost vhost, JSONObject result, Class<?> configClass) {
		for(Field field: configClass.getDeclaredFields()) {
			if(field.isAnnotationPresent(Core9Configuration.class)) {
				String type = field.getAnnotation(Core9Configuration.class).type();
				List<Map<String,Object>> configs = configRepository.getConfigList(vhost, type);
				JSONObject values = new JSONObject();
				for(Map<String,Object> config: configs) {
					values.put((String) config.get("_id"), (String) config.get("name"));
				}
				JSONObject fieldJson = new JSONObject();
				fieldJson.put("widget", "select");
				fieldJson.put("label", field.getName() + " by Name");
				fieldJson.put("values", values);
				result.put(field.getName(), fieldJson);
			}
			if(field.isAnnotationPresent(Core9GlobalConfiguration.class)) {
				String type = field.getAnnotation(Core9GlobalConfiguration.class).type();
				List<Map<String,Object>> configs = configRepository.getConfigList(vhost, type);
				JSONObject values = new JSONObject();
				for(Map<String,Object> config: configs) {
					values.put((String) config.get("_id"), (String) config.get("name"));
				}
				JSONObject fieldJson = new JSONObject();
				fieldJson.put("widget", "select_with_global");
				fieldJson.put("label", field.getName() + " by Name");
				fieldJson.put("values", values);
				result.put(field.getName(), fieldJson);
			}
		}
	}

	
}
