package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.Texture;

public class Image {
	Texture tex;
	static final float[] imgVerts = { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0 };
	static final Model model = new Model(imgVerts);
	public Image(Texture t) {
		tex = t;
	}
	/**
	 * fewer param version of {@link Text#draw(Window, Vector4f, float, float)
	 * draw(w, pos, hOffset, vOffset)}
	 */
	public void draw(Window w, Vector4f pos) {
		draw(w, pos, 0);
	}
	/**
	 * fewer param version of {@link Text#draw(Window, Vector4f, float, float)
	 * draw(w, pos, hOffset, vOffset)}
	 */
	public void draw(Window w, Vector4f pos, float offset) {
		draw(w, pos, offset, offset);
	}
	/**
	 * @param w
	 *            window to draw to
	 * @param pos
	 *            position in window
	 * @param hOffset
	 *            horizontal alignment (0 for left align at pos, 1 for right
	 *            align, 0.5f for centered)
	 * @param vOffset
	 *            vertical alignment about pos
	 */
	public void draw(Window w, Vector4f pos, float hOffset, float vOffset) {
		glUniformMatrix4fv(3, false,
				Matrix4f.getTranslation(pos.x - (tex.width * hOffset), pos.y - (tex.height * vOffset), pos.z).multiply(Matrix4f.getScale(tex.width, tex.height, 1)).toFloatBuffer());// model
		glUniformMatrix4fv(4, false, Matrix4f.getOrthographic(w.getWidth(), w.getHeight()).toFloatBuffer());// projection
		tex.bind();
		model.render();
	}
	public void destroy() {
		if (tex != null) {
			tex.destroy();
			tex = null;
		}
	}
}
