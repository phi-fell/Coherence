package io.proffitt.coherence.gui;

import io.proffitt.coherence.graphics.Text;
import io.proffitt.coherence.resource.Font;

public class TextComponent extends MenuComponent {
	private Font	font;
	private Text	text;
	private String	textCache;
	private String	val;
	public TextComponent(Font f, String t, int x, int y) {
		super(null, x, y, 0, 0);
		font = f;
		val = t;
		textCache = val;
		text = font.getText(textCache);
	}
	public void draw() {
		if (getValue(val) != null && !textCache.equals(getValue(val).getString())){
			textCache = getValue(val).getString();
			text.destroy();
			text = font.getText(textCache);
		}
		text.getBackingImage().draw(getX(), getY());
	}
}
