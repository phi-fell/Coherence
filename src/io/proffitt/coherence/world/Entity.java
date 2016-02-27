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
	private final float	height	= 1.5f;
	float				onGround;
	protected Model		model;
	protected Texture	tex;
	protected Transform	transform;
	Vector4f			velocity;
	private Camera		locked	= null;
	public Entity(Model m, Texture t) {
		onGround = 0;
		velocity = new Vector4f();
		model = m;
		tex = t;
		transform = new Transform();
	}
	public boolean isOnGround() {
		return onGround <= 0;
	}
	public void lockToGround(float dH, double delta) {
		onGround = dH - height;
		if (onGround > 0) {
			velocity.y -= 36 * delta;
		}
		if (onGround < 0) {
			transform.getPosition().y -= onGround;
			onGround = 0;
			velocity.y = 0;
		}
	}
	public void update(double delta) {
		transform.getPosition().addInPlace(velocity.times((float) delta));
	}
	void lockCamera() {
		if (locked != null) {
			Vector4f pos = transform.getPosition();
			locked.setPos(pos.x, pos.y, pos.z);
		}
	}
	public Transform getTransfrom() {
		return transform;
	}
	public Vector4f getVelocity() {
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
