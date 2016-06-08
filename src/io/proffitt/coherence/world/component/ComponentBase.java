package io.proffitt.coherence.world.component;

import java.util.ArrayList;

public class ComponentBase implements Component {
	private ArrayList<Component> children;
	public ComponentBase() {
		children = new ArrayList<Component>();
	}
	public void draw() {
		for (Component c : children) {
			c.draw();
		}
	}
}
