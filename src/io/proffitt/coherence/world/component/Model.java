package io.proffitt.coherence.world.component;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.graphics.Mesh;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.CMLFile;
import io.proffitt.coherence.resource.Texture;

public class Model implements Component {
	private Mesh		mesh;
	private Texture		tex;
	private Transform	transform;
	float				sx, sy, sz, su;	// x, y, z, uniform scale factors
	public Model(Mesh m, Texture t) {
		mesh = m;
		tex = t;
	}
	public Model(CMLFile cml) {
		if (cml.getObj("scale.x") != null) {
			sx = cml.getObj("scale.x").getValue().getFloat();
		} else {
			sx = 1;
		}
		if (cml.getObj("scale.y") != null) {
			sy = cml.getObj("scale.y").getValue().getFloat();
		} else {
			sy = 1;
		}
		if (cml.getObj("scale.z") != null) {
			sz = cml.getObj("scale.z").getValue().getFloat();
		} else {
			sz = 1;
		}
		if (cml.getObj("scale.uniform") != null) {
			su = cml.getObj("scale.uniform").getValue().getFloat();
		} else {
			su = 1;
		}
	}
	@Override
	public void draw() {
		tex.bind();
		glUniformMatrix4fv(3, false, transform.getAsMatrix().toFloatBuffer());// model
		mesh.render();
	}
	public Mesh getMesh() {
		return mesh;
	}
}
