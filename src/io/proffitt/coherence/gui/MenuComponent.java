package io.proffitt.coherence.gui;

import java.util.ArrayList;

public abstract class MenuComponent {
	int x, y, w, h;
	boolean relativeWidth = false;
	boolean relativeHeight = false;
	ArrayList<MenuComponent> components;
	MenuParent parent;
	public MenuComponent(MenuParent p, int X, int Y, int width, int height) {
		parent = p;
		w = width;
		h = height;
		x = X;
		y = Y;
	}
	public void setParent(MenuParent p) {
		parent = p;
	}
	public int getWidth() {
		return w + (relativeWidth ? parent.getWidth() : 0);
	}
	public int getHeight() {
		return h + (relativeHeight ? parent.getHeight() : 0);
	}
	public void addComponent(MenuComponent mc) {
		components.add(mc);
	}
	/**
	 * @param xmod
	 *            X position of parent component
	 * @param ymod
	 *            Y position of parent component
	 */
	public void draw(int xmod, int ymod) {
		// This method should be overriden by ANY visual component
	}
	/**
	 * @param xrel
	 *            X position within this component
	 * @param yrel
	 *            Y position within this component
	 * @return true if click was handled by this component, false otherwise.
	 */
	public boolean handleClick(int xrel, int yrel) {
		// This method should be overriden by ANY interactive component
		for (MenuComponent mc : components) {
			if (mc.handleClick(xrel, yrel)) {
				return true;
			}
		}
		return false;
	}
}
