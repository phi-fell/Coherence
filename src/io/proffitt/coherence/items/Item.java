package io.proffitt.coherence.items;

import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.ItemSchematic;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.world.Entity;

public class Item extends Entity {
	ItemSchematic schema;
	int count;
	public Item(String id) {
		super(null, null);
		count = 1;
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
	public boolean sameType(Item item) {
		return item.schema.equals(schema);
	}
}
