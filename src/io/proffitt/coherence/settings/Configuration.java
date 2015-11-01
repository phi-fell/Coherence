package io.proffitt.coherence.settings;

import io.proffitt.coherence.resource.CMLTag;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuration implements ValueOwner {
	private ArrayList<SettingsListener>	listeners;
	private HashMap<String, Value>		settings;
	public Configuration() {
		listeners = new ArrayList<SettingsListener>();
		settings = new HashMap<String, Value>();
		//defaults
		settings.put("vsync", new Value(true));
		settings.put("msaa", new Value(16));
		//end defaults
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
}
