package io.proffitt.coherence.resource;

import io.proffitt.coherence.graphics.Model;

public class ItemSchematic {
	String				ID;
	Model				model;
	Texture				tex;
	String				name;
	public final int	maxStackSize;
	public ItemSchematic(String id, CMLFile c) {
		ID = id;
		model = ResourceHandler.get().getModel(c.getObj("model").getValue().getString());
		tex = ResourceHandler.get().getTexture(c.getObj("texture").getValue().getString());
		name = c.getObj("name").getValue().getString();
		maxStackSize = c.getObj("stack").getValue().getInt();
	}
	public String getName(){
		return ID;
	}
	public Model getModel() {
		return model;
	}
	public Texture getTexture() {
		return tex;
	}
	public boolean equals(Object o) {
		return o.getClass().equals(this.getClass()) && ID.equals(((ItemSchematic) o).ID);
	}
}
