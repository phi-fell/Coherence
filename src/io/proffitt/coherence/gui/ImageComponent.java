package io.proffitt.coherence.gui;

import io.proffitt.coherence.graphics.Image;

public class ImageComponent extends MenuComponent {
	Image	img;
	public ImageComponent(Image i, int x, int y) {
		super(null, x, y, i.getBackingTexture().width, i.getBackingTexture().height);
		img = i;
	}
	public void draw() {
		img.draw(getX(), getY());
	}
}
