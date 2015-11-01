package io.proffitt.coherence.world;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.math.Transform;

public class Entity {
	Model		model;
	Transform	transform;
	public Entity(Model m) {
		model = m;
		transform = new Transform();
	}
	public Transform getTransfrom() {
		return transform;
	}
	public void draw() {
		glUniformMatrix4fv(3, false, transform.getAsMatrix().toFloatBuffer());// model
		model.render();
	}
	public void destroy() {
		model.destroy();
	}
}
