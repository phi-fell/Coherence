package io.proffitt.coherence.gui;

import io.proffitt.coherence.graphics.Text;

public class FPSText extends MenuComponent {
	static FPSText singleton;
	public static FPSText getSingleton() {
		return singleton;
	}
	public static void generateSingleton(MenuParent mp, Text t) {
		if (singleton == null) {
			singleton = new FPSText(mp, t);
		} else {
			singleton.text.destroy();
			singleton.setParent(mp);
			singleton.text = t;
		}
	}
	private Text text;
	public FPSText(MenuParent p, Text t) {
		super(p, 0, 0, t.getBackingImage().getWidth(), t.getBackingImage().getHeight());
		text = t;
	}
}
