package io.proffitt.coherence.gui;

import io.proffitt.coherence.settings.Value;

public interface MenuParent {
	public int getWidth();
	public int getHeight();
	public int getX();
	public int getY();
	public Value getValue(String k);
}
