package io.proffitt.coherence.world;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import io.proffitt.coherence.ai.EntityAI;
import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.ResourceHandler;

public class Entity {
	Model		model;
	Transform	transform;
	EntityAI	ai;
	public Entity(Model m, EntityAI eAI) {
		model = m;
		transform = new Transform();
		ai = eAI;
	}
	public void update(double delta) {
		transform.setPosition(transform.getPosition().plus(ai.getMoveVector(delta)));
	}
	public Transform getTransfrom() {
		return transform;
	}
	public AABB getAABB() {
		return new AABB(model.getAABB(), transform);
	}
	public void draw(Camera cam) {
		if (model != null) {
			ResourceHandler.get().getShader("HDRdefault").bind();
			cam.bind();
			glUniformMatrix4fv(3, false, transform.getAsMatrix().toFloatBuffer());// model
			model.render();
		}
	}
}
