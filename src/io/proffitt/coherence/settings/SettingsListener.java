package io.proffitt.coherence.settings;

public interface SettingsListener {
	public void onSettingChanged(int setting, int newValue);
}
