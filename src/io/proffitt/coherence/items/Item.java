package io.proffitt.coherence.items;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.resource.ItemSchematic;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.world.Entity;

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
		super(null, null);
		count = 1;
		schema = ResourceHandler.get().getItem(id);
		this.transform.getScale().x = schema.getScaleX();
		this.transform.getScale().y = schema.getScaleY();
		this.transform.getScale().z = schema.getScaleZ();
		model = schema.getModel();
		tex = schema.getTexture();
	}
	public void update(double delta) {
		super.update(delta);
		//TODO: anything else?
	}
	public void draw(int x, int y) {
		if (model != null) {
			tex.bind();
			inventoryTransform.getPosition().x = x + (Inventory.ITEM_SIZE_PIX / 2);
			inventoryTransform.getPosition().y = y + (Inventory.ITEM_SIZE_PIX / 2);
			double t = System.nanoTime() / 1000000000f;
			t += GUID * 7;
			inventoryTransform.getRotation().x = (float) (t);
			inventoryTransform.getRotation().y = (float) (t / 5);
			inventoryTransform.getRotation().z = (float) (t / 13);
			glUniformMatrix4fv(3, false, inventoryTransform.getAsMatrix().toFloatBuffer());// model
			model.render();
		}
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
