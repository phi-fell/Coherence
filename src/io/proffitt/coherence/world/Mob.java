package io.proffitt.coherence.world;

import io.proffitt.coherence.ai.EntityAI;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.items.Inventory;
import io.proffitt.coherence.math.Vector3f;
import io.proffitt.coherence.resource.Texture;

public class Mob extends Entity {
	EntityAI		ai;
	MobAttributes	attributes;
	Inventory		inv;
	public Mob(Model m, Texture t, EntityAI eAI) {
		super(m, t);
		ai = eAI;
		inv = new Inventory(this, 10, 10, "Inventory");
	}
	public void update(double delta) {
		Vector3f goal = ai.getMoveVector(delta);
		if (velocity.y == 0 && goal.y > 0) {
			velocity.y = 18f;
		}
		goal.y = 0;
		transform.setPosition(transform.getPosition().plus(goal.times((float) delta)));
		super.update(delta);
	}
	public Inventory getInventory() {
		return inv;
	}
}
