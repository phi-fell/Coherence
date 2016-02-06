package io.proffitt.coherence.settings;

import java.util.ArrayList;
import java.util.HashMap;

import io.proffitt.coherence.resource.CMLFile;
import io.proffitt.coherence.resource.CMLObject;

public class Configuration implements ValueOwner {
	private ArrayList<SettingsListener>	listeners;
	private HashMap<String, Value>		settings;
	public Configuration() {
		listeners = new ArrayList<SettingsListener>();
		settings = new HashMap<String, Value>();
	}
	public void loadFromCML(CMLFile f) {
		for (CMLObject co : f.getObjs()) {
			Value next = new Value(co.getValue());
			next.setOwner(this, co.getName());
			settings.put(co.getName(), next);
		}
	}
	public void register(SettingsListener newSL) {
		listeners.add(newSL);
	}
	public void remove(String k) {
		settings.remove(k);
		notifyListeners(k, null);
	}
	public void set(String k, Value newVal) {
		setNoNotify(k, newVal);
		notifyListeners(k, newVal);
	}
	//requires that listeners be notified soon after!
	private void setNoNotify(String k, Value newVal) {
		settings.put(k, newVal);
	}
	private void notifyListeners(String k, Value nv) {
		for (SettingsListener sl : listeners) {
			sl.onSettingChanged(k, nv);
		}
	}
	public Value get(String k) {
		Value ret = settings.get(k);
		return ret;
	}
	public Value nullGet(String k) {
		Value ret = get(k);
		if (ret == null) {
			ret = new Value("NULL");
			setNoNotify(k, ret);
		}
		return ret;
	}
	@Override
	public void alert(String id) {
		notifyListeners(id, get(id));
	}
	public String toString() {
		String ret = "";
		boolean first = true;
		;
		for (String k : settings.keySet()) {
			if (first) {
				first = false;
			} else {
				ret += ",\n";
			}
			ret += k + ": " + settings.get(k).toString();
		}
		return ret;
	}
}
