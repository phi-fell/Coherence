package io.proffitt.coherence.resource;

import io.proffitt.coherence.graphics.Mesh;
import io.proffitt.coherence.world.component.Model;

public class ItemSchematic {
	String				ID;
	ItemSchematic		parent;
	Model				model;
	String				name;
	public final int	maxStackSize;
	public ItemSchematic(String id, CMLFile c) {
		ID = id;
		if (ID == "prototype") {
			parent = null;
			model = ResourceHandler.get().getModel(c.getObj("model").getValue().getString());
			name = c.getObj("name").getValue().getString();
			maxStackSize = c.getObj("stack").getValue().getInt();
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
		}
	}
	public String getName() {
		return ID;
	}
	public Model getModel() {
		return model;
	}
	public boolean equals(Object o) {
		return o.getClass().equals(this.getClass()) && ID.equals(((ItemSchematic) o).ID);
	}
}
