package io.proffitt.coherence.resource;

import io.proffitt.coherence.graphics.Model;

public class ItemSchematic {
	String				ID;
	ItemSchematic		parent;
	Model				model;
	Texture				tex;
	String				name;
	public final int	maxStackSize;
	float				sx, sy, sz, su;	// x, y, z, uniform scale factors
	public ItemSchematic(String id, CMLFile c) {
		ID = id;
		if (ID == "prototype") {
			parent = null;
			model = ResourceHandler.get().getModel(c.getObj("model").getValue().getString());
			tex = ResourceHandler.get().getTexture(c.getObj("texture").getValue().getString());
			name = c.getObj("name").getValue().getString();
			maxStackSize = c.getObj("stack").getValue().getInt();
			sx = c.getObj("scale.x").getValue().getFloat();
			sy = c.getObj("scale.y").getValue().getFloat();
			sz = c.getObj("scale.z").getValue().getFloat();
			su = c.getObj("scale.uniform").getValue().getFloat();
		} else {
			if (c.getObj("parent") != null) {
				parent = ResourceHandler.get().getItem(c.getObj("parent").getValue().getString());
			} else {
				parent = ResourceHandler.get().getItem("prototype");
			}
			if (c.getObj("model") != null) {
				model = ResourceHandler.get().getModel(c.getObj("model").getValue().getString());
			} else {
				model = parent.model;
			}
			if (c.getObj("texture") != null) {
				tex = ResourceHandler.get().getTexture(c.getObj("texture").getValue().getString());
			} else {
				tex = parent.tex;
			}
			if (c.getObj("name") != null) {
				name = c.getObj("name").getValue().getString();
			} else {
				name = parent.name;
			}
			if (c.getObj("stack") != null) {
				maxStackSize = c.getObj("stack").getValue().getInt();
			} else {
				maxStackSize = parent.maxStackSize;
			}
			if (c.getObj("scale.x") != null) {
				sx = c.getObj("scale.x").getValue().getFloat();
			} else {
				sx = parent.sx;
			}
			if (c.getObj("scale.y") != null) {
				sy = c.getObj("scale.y").getValue().getFloat();
			} else {
				sy = parent.sy;
			}
			if (c.getObj("scale.z") != null) {
				sz = c.getObj("scale.z").getValue().getFloat();
			} else {
				sz = parent.sz;
			}
			if (c.getObj("scale.uniform") != null) {
				su = c.getObj("scale.uniform").getValue().getFloat();
			} else {
				su = parent.su;
			}
		}
	}
	public String getName() {
		return ID;
	}
	public Model getModel() {
		return model;
	}
	public Texture getTexture() {
		return tex;
	}
	public float getScaleX() {
		return sx * su;
	}
	public float getScaleY() {
		return sy * su;
	}
	public float getScaleZ() {
		return sz * su;
	}
	public boolean equals(Object o) {
		return o.getClass().equals(this.getClass()) && ID.equals(((ItemSchematic) o).ID);
	}
}
