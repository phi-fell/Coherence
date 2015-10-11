package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.Texture;

public class Text {
	Texture					tex;
	static final float[]	textVerts	= { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0 };
	static final Model		model		= new Model(textVerts);
	public Text(Texture t) {
		tex = t;
	}
	public void draw(Window w, Vector4f pos) {
		draw(w, pos, 0);
	}
	public void draw(Window w, Vector4f pos, float offset) {
		draw(w, pos, offset, offset);
	}
	public void draw(Window w, Vector4f pos, float hOffset, float vOffset) {
		glUniformMatrix4fv(3, false, Matrix4f.getTranslation(pos.x - (tex.width * hOffset), pos.y - (tex.height * vOffset), pos.z).multiply(Matrix4f.getScale(tex.width, tex.height, 1)).toFloatBuffer());// model
		glUniformMatrix4fv(4, false, Matrix4f.getOrthographic(w.getWidth(), w.getHeight()).toFloatBuffer());// projection
		tex.bind();
		model.render();
	}
	public void destroy(){
		tex.destroy();
		tex = null;
	}
}
