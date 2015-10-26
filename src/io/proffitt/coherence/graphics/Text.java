package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.Texture;

public class Text {
	static final float[]	textVerts	= { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0 };
	static final Model		model		= new Model(textVerts);
	Image img;
	public Text(Texture t) {
		img = t.getAsImage();
	}
	public Image getBackingImage(){
		return img;
	}
	public void destroy(){
		img.destroy();
		img = null;
	}
}
