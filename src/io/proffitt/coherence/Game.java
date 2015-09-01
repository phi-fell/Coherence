package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import io.proffitt.coherence.graphics.Window;

public class Game implements Runnable {
	Thread	t;
	boolean	running;
	Window	w;
	public Game(Window wind) {
		running = false;
		w = wind;
	}
	public boolean isRunning() {
		return running;
	}
	public void handleKeyPress(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			System.out.println("key: " + (char) key + " pressed");
		} else if (action == GLFW_RELEASE) {
			System.out.println("key: " + (char) key + " released");
		}
	}
	public void handleMousePos(long window, double xpos, double ypos) {
	}
	public void handleMouseClick(long window, int button, int action, int mods) {
	}
	public void handleMouseScroll(long window, double xoffset, double yoffset) {
	}
	public void start() {
		running = true;
		t = new Thread(this, "Game Thread");
		t.start();
	}
	@Override
	public void run() {
		w.create();
		w.setCallbacks(GLFWKeyCallback(this::handleKeyPress), GLFWScrollCallback(this::handleMouseScroll), GLFWCursorPosCallback(this::handleMousePos), GLFWMouseButtonCallback(this::handleMouseClick));
		int x = 0;
		while (running) {
			w.poll();
			w.swap();
		}
		running = false;
	}
}
