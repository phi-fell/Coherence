package io.proffitt.coherence.world;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.resource.Texture;

public abstract class Entity {
	Model			model;
	Texture			tex;
	Transform		transform;
	Vector4f		velocity;
	private Camera	locked	= null;
	public Entity(Model m, Texture t) {
		velocity = new Vector4f();
		model = m;
		tex = t;
		transform = new Transform();
	}
	public void update(double delta) {
		transform.getPosition().addInPlace(velocity);
		if (locked != null) {
			Vector4f pos = transform.getPosition();
			locked.setPos(pos.x, pos.y, pos.z);
		}
	}
	public Transform getTransfrom() {
		return transform;
	}
	public Vector4f getVelocity(){
		return velocity;
	}
	public AABB getAABB() {
		return new AABB(model.getAABB(), transform);
	}
	public void draw(Camera cam) {
		if (model != null) {
			ResourceHandler.get().getShader("HDRtextured").bind();
			tex.bind();
			cam.bind();
			glUniformMatrix4fv(3, false, transform.getAsMatrix().toFloatBuffer());// model
			model.render();
		}
	}
	public void lockCamera(Camera camera) {
		locked = camera;
	}
}
