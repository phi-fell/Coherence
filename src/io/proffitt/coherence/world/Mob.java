package io.proffitt.coherence.world;

import io.proffitt.coherence.ai.EntityAI;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.Texture;

public class Mob extends Entity {
	EntityAI ai;
	public Mob(Model m, Texture t, EntityAI eAI) {
		super(m, t);
		ai = eAI;
	}
	public void update(double delta) {
		Vector4f goal = ai.getMoveVector(delta);
		if (velocity.y == 0 && goal.y > 0) {
			velocity.y = 18f;
		}
		goal.y = 0;
		transform.setPosition(transform.getPosition().plus(goal.times((float) delta)));
		super.update(delta);
	}
}
