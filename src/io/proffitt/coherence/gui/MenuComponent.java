package io.proffitt.coherence.gui;

import io.proffitt.coherence.settings.Value;

import java.util.ArrayList;

public abstract class MenuComponent implements MenuParent {
	int							x, y, w, h;
	boolean						relativeWidth	= false;
	boolean						relativeHeight	= false;
	ArrayList<MenuComponent>	components;
	MenuParent					parent;
	public MenuComponent(MenuParent p, int X, int Y, int width, int height) {
		components = new ArrayList<MenuComponent>();
		parent = p;
		w = width;
		h = height;
		x = X;
		y = Y;
	}
	public void setParent(MenuParent p) {
		parent = p;
	}
	public Value getValue(String k) {
		return parent.getValue(k);
	}
	public int getX() {
		return x + (parent == null ? 0 : parent.getX());
	}
	public int getY() {
		return y + (parent == null ? 0 : parent.getY());
	}
	public int getWidth() {
		return w + (relativeWidth ? parent.getWidth() : 0);
	}
	public int getHeight() {
		return h + (relativeHeight ? parent.getHeight() : 0);
	}
	public void addComponent(MenuComponent mc) {
		components.add(mc);
		mc.setParent(this);
	}
	/**
	 * @param xmod
	 *            X position of parent component
	 * @param ymod
	 *            Y position of parent component
	 */
	public void draw() {
		// This method should be overriden by ANY visual component
		for (MenuComponent mc : components) {
			mc.draw();
		}
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
