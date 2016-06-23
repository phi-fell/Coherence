package io.proffitt.coherence.world.component;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.graphics.Mesh;
import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.CMLFile;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.resource.Texture;
import io.proffitt.coherence.world.Entity;

public class Model implements Component {
	private Mesh		mesh;
	private Texture		tex;
	private Transform	entityTransform;
	private Transform	transform;
	float				sx, sy, sz, su;	// x, y, z, uniform scale factors
	public Model(Mesh m, Texture t) {
		mesh = m;
		tex = t;
		entityTransform = null;
		transform = new Transform();
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
		if (cml.getObj("mesh") != null) {
			mesh = ResourceHandler.get().getMesh(cml.getObj("mesh").getValue().getString());
		}
		if (cml.getObj("texture") != null) {
			tex = ResourceHandler.get().getTexture(cml.getObj("texture").getValue().getString());
		}
		entityTransform = null;
		transform = new Transform();
	}
	@Override
	public void draw() {
		tex.bind();
		if (transform != null) {
			glUniformMatrix4fv(4, false, transform.getAsMatrix().toFloatBuffer());// entity
		} else {
			glUniformMatrix4fv(4, false, new Matrix4f().toFloatBuffer());// entity
		}
		glUniformMatrix4fv(3, false, entityTransform.getAsMatrix().toFloatBuffer());// model
		mesh.render();
	}
	@Override
	public void setParent(Entity p) {
		entityTransform = p.getComponent(Transform.class);
	}
	public Mesh getMesh() {
		return mesh;
	}
}
