package io.proffitt.coherence.world;

import io.proffitt.coherence.ai.EntityAI;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.resource.Texture;

public class Mob extends Entity {
	EntityAI ai;
	public Mob(Model m, Texture t, EntityAI eAI) {
		super(m, t);
		ai = eAI;
	}
	public void update(double delta) {
		transform.setPosition(transform.getPosition().plus(ai.getMoveVector(delta)));
		super.update(delta);
	}
}
