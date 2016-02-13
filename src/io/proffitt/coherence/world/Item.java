package io.proffitt.coherence.world;

import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.ItemSchematic;
import io.proffitt.coherence.resource.ResourceHandler;

public class Item extends Entity {
	ItemSchematic schema;
	public Item(String id) {
		super(null, null);
		schema = ResourceHandler.get().getItem(id);
		model = schema.getModel();
		tex = schema.getTexture();
	}
	public void update(double delta) {
		super.update(delta);
		//TODO: anything else?
	}
	public Transform getTransfrom() {
		return transform;
	}
	public AABB getAABB() {
		return new AABB(schema.getModel().getAABB(), transform);
	}
}
