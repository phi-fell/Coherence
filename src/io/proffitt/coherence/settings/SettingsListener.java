package io.proffitt.coherence.settings;

public interface SettingsListener {
	public void onSettingChanged(String setting, Value newValue);
}
