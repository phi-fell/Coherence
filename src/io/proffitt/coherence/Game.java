package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.resource.ResourceHandler;

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
		float[] v = new float[] { +0.0f, +0.8f, 1, -0.8f, -0.8f, 1, +0.8f, -0.8f, 1 };
		Model m = new Model(v);
		while (running) {
			w.poll();
			glClear(GL_COLOR_BUFFER_BIT);
			ResourceHandler.get().getShader("default").bind();
			m.render();
			w.swap();
		}
		m.destroy();
		running = false;
	}
}
