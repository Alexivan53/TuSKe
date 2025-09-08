package com.github.tukenuke.tuske.util;

import ch.njol.skript.variables.Variables;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 * @author Tuke_Nuke on 28/05/2017
 *
 * Updated for Skript 2.12+: localVariables may not be a WeakHashMap; handle any Map implementation.
 */
public class VariableUtil {
	private static VariableUtil instance;
	public static VariableUtil getInstance() {
		if (instance == null)
			instance = new VariableUtil();
		return instance;
	}

	private VariableUtil() {

	}
	@SuppressWarnings("unchecked")
	public Map<Event, Object> map = initMap();

	@SuppressWarnings("unchecked")
	private Map<Event, Object> initMap() {
		Object reflected = ReflectionUtils.getField(Variables.class, null, "localVariables");
		if (reflected instanceof Map)
			return (Map<Event, Object>) reflected;
		return new WeakHashMap<>();
	}
	/**
	 * Some hacking methods to copy variables from one event, and paste
	 * to another. It allows to run the section using the same variables
	 * when was making the gui
	 * @param from - The Event to copy the variable from
	 * @return The VariableMap wrapped in Object
	 */
	@SuppressWarnings("unchecked")
	public Object copyVariables(Event from){
		if (from != null && map.containsKey(from)) {
			Object variablesMap = map.get(from);
			if (variablesMap == null)
				return null;
			Object newVariablesMap = ReflectionUtils.newInstance(variablesMap.getClass());
			//TuSKe.debug(newVariablesMap, variablesMap);
			if (newVariablesMap == null)
				return null;
			HashMap<String, Object> single = ReflectionUtils.getField(newVariablesMap.getClass(), newVariablesMap, "hashMap");
			TreeMap<String, Object> list = ReflectionUtils.getField(newVariablesMap.getClass(), newVariablesMap, "treeMap");
			single.putAll(ReflectionUtils.getField(variablesMap.getClass(), variablesMap, "hashMap"));
			list.putAll(ReflectionUtils.getField(variablesMap.getClass(), variablesMap, "treeMap"));
			return newVariablesMap;
		}
		return null;
	}

	/**
	 * Paste some variables copied lately to another event
	 * @param to - The event to paste
	 * @param variables - The object VariableMap returned in {@link #copyVariables(Event)}
	 */
	public void pasteVariables(Event to, Object variables){
		if (to != null)
			map.put(to, variables);
	}

}
