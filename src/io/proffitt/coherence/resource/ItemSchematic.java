package io.proffitt.coherence.resource;

import io.proffitt.coherence.graphics.Model;

public class ItemSchematic {
	Model	model;
	Texture	tex;
	String	name;
	public ItemSchematic(CMLFile c) {
		model = ResourceHandler.get().getModel(c.getObj("model").getValue().getString());
		tex = ResourceHandler.get().getTexture(c.getObj("texture").getValue().getString());
		name = c.getObj("name").getValue().getString();
	}
	public Model getModel() {
		return model;
	}
	public Texture getTexture() {
		return tex;
	}
}
