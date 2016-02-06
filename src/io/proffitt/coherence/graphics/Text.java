package io.proffitt.coherence.graphics;

import io.proffitt.coherence.resource.Texture;

public class Text {
	Image					img;
	public Text(Texture t) {
		img = t.getAsImage();
	}
	public Image getBackingImage() {
		return img;
	}
	public void destroy() {
		img.destroy();
		img = null;
	}
}
