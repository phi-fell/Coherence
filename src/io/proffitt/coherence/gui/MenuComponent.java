package io.proffitt.coherence.gui;

import java.util.ArrayList;

public abstract class MenuComponent {
	int x, y, w, h;
	ArrayList<MenuComponent> components;
	public MenuComponent(int width, int height) {
		w = width;
		h = height;
		x = 0;
		y = 0;
		components = new ArrayList<MenuComponent>();
	}
	public MenuComponent(int X, int Y, int width, int height) {
		w = width;
		h = height;
		x = X;
		y = Y;
	}
	public void addComponent(MenuComponent mc){
		components.add(mc);
	}
	/**
	 * @param xmod X position of parent component
	 * @param ymod Y position of parent component
	 */
	public abstract void draw(int xmod, int ymod);
	/**
	 * @param xrel X position within this component
	 * @param yrel Y position within this component
	 * @return true if click was handled by this component, false otherwise.
	 */
	public abstract boolean handleClick(int xrel, int yrel);
}
