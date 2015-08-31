package io.proffitt.coherence;

import io.proffitt.coherence.graphics.Window;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Game implements Runnable {
	Thread			t;
	boolean running;
	Window			w;
	GLFWKeyCallback	keyCall;
	public Game(Window wind) {
		running = false;
		w = wind;
		keyCall = GLFWKeyCallback(this::handleKeyPress);
	}
	public boolean isRunning(){
		return running;
	}
	public void handleKeyPress(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			System.out.println("key: " + (char) key + " pressed");
		} else if (action == GLFW_RELEASE) {
			System.out.println("key: " + (char) key + " released");
		}
	}
	public void start() {
		running = true;
		t = new Thread(this, "Game Thread");
		t.start();
	}
	@Override
	public void run() {
		w.create();
		glfwSetKeyCallback(w.getID(), keyCall);
		int x = 0;
		while (running) {
			w.poll();
			w.swap();
		}
		running = false;
	}
}
