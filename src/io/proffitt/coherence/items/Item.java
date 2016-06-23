package io.proffitt.coherence.items;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.ItemSchematic;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.resource.Shader;
import io.proffitt.coherence.world.Entity;
import io.proffitt.coherence.world.component.Model;

public class Item extends Entity {
	static Transform inventoryTransform;
	static {
		inventoryTransform = new Transform();
		inventoryTransform.getScale().x = Inventory.ITEM_SIZE_PIX / 2;
		inventoryTransform.getScale().y = Inventory.ITEM_SIZE_PIX / 2;
		inventoryTransform.getScale().z = Inventory.ITEM_SIZE_PIX / 2;
	}
	ItemSchematic	schema;
	int				count;
	public Item(String id) {
		super(null);
		count = 1;
		schema = ResourceHandler.get().getItem(id);
		this.transform.getScale().x = 1;
		this.transform.getScale().y = 1;
		this.transform.getScale().z = 1;
		components.put(Model.class, schema.getModel());
	}
	public void update(double delta) {
		super.update(delta);
		//TODO: anything else?
	}
	public void draw(int x, int y) {
		if (components.containsKey(Model.class)) {
			//TODO!
			inventoryTransform.getPosition().x = x + (Inventory.ITEM_SIZE_PIX / 2);
			inventoryTransform.getPosition().y = y + (Inventory.ITEM_SIZE_PIX / 2);
			double t = System.nanoTime() / 1000000000f;
			t += GUID * 7;
			inventoryTransform.getRotation().x = (float) (t);
			inventoryTransform.getRotation().y = (float) (t / 5);
			inventoryTransform.getRotation().z = (float) (t / 13);
			Shader.setEntityMat(inventoryTransform.getAsMatrix());
			((Model) components.get(Model.class)).draw();
		}
	}
	public Transform getTransfrom() {
		return transform;
	}
	public AABB getAABB() {
		return new AABB(schema.getModel().getMesh().getAABB(), transform);
	}
	public boolean sameType(Item item) {
		return item.schema.equals(schema);
	}
}
