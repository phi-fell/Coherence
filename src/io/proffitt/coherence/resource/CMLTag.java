package io.proffitt.coherence.resource;

import java.util.ArrayList;
import java.util.HashMap;

public class CMLTag {
	private HashMap<String, CMLTag> subtags;
	private String value;
	public CMLTag(String val) {
		subtags = new HashMap<String, CMLTag>();
		value = val;
	}
	public CMLTag(String val, String data) {
		value = val;
		subtags = new HashMap<String, CMLTag>();
		String[] lines = data.split("\n");
		int indent = 0;
		boolean found = false;
		for (int i = 0; i < lines[0].length() && !found; i++) {
			if (lines[0].charAt(i) == ' ') {
				indent = i;
			} else {
				found = true;
			}
		}
		String startTab = "";
		for (int i = 0; i <= indent; i++) {
			startTab += " ";
		}
		int index = 0;
		while (index < lines.length) {
			String[] valAndID = lines[index].split(":");
			String id = valAndID[0];
			String v = valAndID.length > 1 ? valAndID[1] : "";
			index++;
			String subData = "";
			boolean once = false;
			for (; index < lines.length && lines[index].startsWith(startTab); index++) {
				subData += (once ? "\n" : "") + lines[index];
				once = true;
			}
			if (subData.trim().length() > 0) {
				CMLTag next = new CMLTag(v, subData);
				subtags.put(id, next);
			}
		}
	}
	public String toString() {
		return toString("");
	}
	public String toString(String pre) {
		String ret = "";
		ret += ": " + value + "\n";
		for (String k : subtags.keySet()) {
			ret += k + subtags.get(k).toString(pre + "    ") + "\n";
		}
		return ret;
	}
	public String getValue() {
		return value;
	}
	public int getInt() {
		return Integer.parseInt(value);
	}
	public double getDouble() {
		return Double.parseDouble(value);
	}
	public float getFloat() {
		return Float.parseFloat(value);
	}
	public long getLong() {
		return Long.parseLong(value);
	}
	public CMLTag getTag(String id) {
		return subtags.get(id);
	}
	public CMLTag[] getTags() {
		return (CMLTag[]) subtags.values().toArray();
	}
}
