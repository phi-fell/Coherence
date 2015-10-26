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
	public abstract void draw();
}
