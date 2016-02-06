package io.proffitt.coherence.resource;

import java.util.Collection;
import java.util.HashMap;

import io.proffitt.coherence.settings.Value;

public class CMLObject {
	private String						id		= null;
	private Value						value	= null;
	private HashMap<String, CMLObject>	objects	= null;
	/** 
	 * Object not yet valid, call parse() immediately after this method!
	 */
	private CMLObject() {
	}
	private int parse(String data) {
		int splitIndex = data.indexOf(':');
		if (splitIndex < 0) {
			if (data.length() <= 32) {
				throw new RuntimeException("Invalid CMLObject, no \':\' present in \"" + data + "\"");
			} else {
				throw new RuntimeException("Invalid CMLObject, no \':\' present");
			}
		}
		id = data.substring(0, splitIndex);
		String v = data.substring(splitIndex + 1);
		if (v.startsWith("{")) {
			objects = new HashMap<String, CMLObject>();
			while (v.length() > 0 && !v.startsWith("}")) {
				CMLObject n = new CMLObject();
				v = v.substring(1);
				v = v.substring(n.parse(v));
				objects.put(n.id, n);
			}
			return (data.length() - v.length()) + 1;
		} else {
			int commaIndex = v.indexOf(',');
			int braceIndex = v.indexOf('}');
			int endIndex = commaIndex;
			if (commaIndex == -1 || braceIndex < commaIndex) {
				endIndex = braceIndex;
			}
			value = new Value(v.substring(0, endIndex));
			value.parse();
			return endIndex + splitIndex + 1;
		}
	}
	public CMLObject(String name, Value val) {
		id = name;
		value = new Value(val);
	}
	public CMLObject(String data) {
		int splitIndex = data.indexOf(':');
		if (splitIndex < 0) {
			throw new RuntimeException("Invalid CMLObject, no \':\' present");
		}
		id = data.substring(0, splitIndex);
		String v = data.substring(splitIndex + 1);
		if (v.startsWith("{")) {
			objects = new HashMap<String, CMLObject>();
			while (v.length() > 0 && !v.startsWith("}")) {
				CMLObject n = new CMLObject();
				v = v.substring(1);
				v = v.substring(n.parse(v));
				objects.put(n.id, n);
			}
		} else {
			value = new Value(v);
			value.parse();
		}
	}
	public String toString() {
		return toString("");
	}
	public String toString(String pre) {
		if (value != null) {
			return pre + id + ":" + value;
		} else {
			StringBuilder ret = new StringBuilder(pre);
			ret.append(id).append(":{\n");
			boolean first = true;
			for (CMLObject c : objects.values()) {
				if (first) {
					first = false;
				} else {
					ret.append(",\n");
				}
				ret.append(pre).append(c.toString("  "));
			}
			ret.append("\n").append(pre).append("}");
			return ret.toString();
		}
	}
	public String toAnonymousString(){
		if (value != null) {
			return value.toString();
		} else {
			StringBuilder ret = new StringBuilder();
			boolean first = true;
			for (CMLObject c : objects.values()) {
				if (first) {
					first = false;
				} else {
					ret.append(",\n");
				}
				ret.append(c.toString());
			}
			return ret.toString();
		}
	}
	public String getName(){
		return id;
	}
	public boolean isList(){
		return value == null;
	}
	public Value getValue(){
		return value;
	}
	public CMLObject getSub(String obj){
		return objects.get(obj);
	}
	public Collection<CMLObject> getSubs(){
		return objects.values();
	}
}