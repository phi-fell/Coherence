package io.proffitt.coherence.settings;

import java.util.ArrayList;

public class Configuration {
	private ArrayList<SettingsListener>	listeners;
	private int[]						settings;
	public static final int				NUM_SETTINGS	= 2;	//KEEP UPDATED!!!
	//settings
	public static final int				VSYNC			= 0;
	public static final int				MSAA			= 1;	//requires restart
	//end settings
	public Configuration() {
		settings = new int[NUM_SETTINGS];
		//defaults
		settings[VSYNC] = 1;
		settings[MSAA] = 16;
		//end defaults
		listeners = new ArrayList<SettingsListener>();
	}
	public void register(SettingsListener newSL) {
		listeners.add(newSL);
	}
	public void set(int setting, int newVal) {
		settings[setting] = newVal;
		for (SettingsListener sl : listeners) {
			sl.onSettingChanged(setting, newVal);
		}
	}
	public int get(int setting) {
		return settings[setting];
	}
}
