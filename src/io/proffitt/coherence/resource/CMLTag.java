package io.proffitt.coherence.resource;

import io.proffitt.coherence.settings.Value;

import java.util.HashMap;

public class CMLTag {
	private HashMap<String, CMLTag>	subtags;
	private Value					value;
	public CMLTag(String val) {
		subtags = new HashMap<String, CMLTag>();
		value = new Value(val);
		value.parse();
	}
	public CMLTag(String val, String data) {
		value = new Value(val);
		value.parse();
		subtags = new HashMap<String, CMLTag>();
		if (data.trim().length() > 0) {
			String[] tlines = data.split("\n");
			int numValid = tlines.length;
			for (String l : tlines) {
				if (l.trim().length() == 0 || l.trim().startsWith("//")) {
					numValid--;
				}
			}
			String[] lines = new String[numValid];
			int valIndex = 0;
			for (int i = 0; i < tlines.length; i++){
				if (tlines[i].trim().length() != 0 && !tlines[i].trim().startsWith("//")){
					lines[valIndex] = tlines[i];
					valIndex++;
				}
			}
			String indent = "";
			for (; indent.length() < lines[0].length() && lines[0].charAt(indent.length()) == ' '; indent += " ") {
			}
			indent += " ";
			int index = 0;
			while (index < lines.length) {
				String[] valAndID = lines[index].split(":");
				String id = valAndID[0].trim();
				String v = valAndID.length > 1 ? valAndID[1].trim() : "";
				index++;
				if (v.equals(">")) {
					v = "";
					boolean once = false;
					for (; index < lines.length && lines[index].startsWith(indent); index++) {
						v += (once ? " " : "") + lines[index].trim();
						once = true;
					}
				} else if (v.equals("|")) {
					v = "";
					boolean once = false;
					for (; index < lines.length && lines[index].startsWith(indent); index++) {
						v += (once ? "\n" : "") + lines[index].substring(indent.length());
						once = true;
					}
					while (getCount(v, "\n ") == getCount(v, "\n") && getCount(v, "\n") != 0 && v.startsWith(" ")) {
						v = v.substring(1).replace("\n ", "\n");
					}
				} else if (v.startsWith(">|") && v.endsWith("|")) {
					String delimiter = v.substring(2, v.length() - 1).replace("\\n", "\n");
					v = "";
					boolean once = false;
					for (; index < lines.length && lines[index].startsWith(indent); index++) {
						v += (once ? delimiter : "") + lines[index].trim();
						once = true;
					}
				}
				String subData = "";
				boolean once = false;
				for (; index < lines.length && lines[index].startsWith(indent); index++) {
					subData += (once ? "\n" : "") + lines[index];
					once = true;
				}
				CMLTag next = new CMLTag(v, subData);
				subtags.put(id, next);
			}
		}
	}
	private static int getCount(String s, String toFind) {
		return s.contains(toFind) ? (1 + getCount(s.substring(s.indexOf(toFind) + 1), toFind)) : 0;
	}
	public String toString() {
		return toString("");
	}
	public String toString(String pre) {
		String ret = "";
		ret += ": " + value.getString() + "\n";
		for (String k : subtags.keySet()) {
			ret += pre + k + subtags.get(k).toString(pre + "    ");
		}
		return ret;
	}
	public Value getValue() {
		return value;
	}
	public CMLTag getTag(String id) {
		return subtags.get(id);
	}
	public CMLTag[] getTags() {
		return subtags.values().toArray(new CMLTag[0]);
	}
	public String[] getTagIDs() {
		return subtags.keySet().toArray(new String[0]);
	}
}
