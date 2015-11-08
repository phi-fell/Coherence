package io.proffitt.coherence.settings;

import io.proffitt.coherence.resource.CMLTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Configuration implements ValueOwner {
	private ArrayList<SettingsListener>	listeners;
	private HashMap<String, Value>		settings;
	public Configuration() {
		listeners = new ArrayList<SettingsListener>();
		settings = new HashMap<String, Value>();
	}
	public void loadFromCML(CMLTag ct) {
		for (String k : ct.getTagIDs()) {
			Value next = ct.getTag(k).getValue();
			next.setOwner(this, k);
			settings.put(k, next);
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
		for (String k : settings.keySet()){
			ret += k + ": " + settings.get(k).toString() + "\n";
		}
		return ret;
	}
}
