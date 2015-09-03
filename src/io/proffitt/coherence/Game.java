package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.math.Matrix4f;
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
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClearDepth(1);
		glEnable(GL_MULTISAMPLE);
		Model m = ResourceHandler.get().getModel("smoothmonkey");
		long firstTime = System.nanoTime();
		int fov = 65;
		while (running) {
			w.poll();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ResourceHandler.get().getShader("default").bind();
			glUniformMatrix4fv(3, false, Matrix4f.getRotationY((System.nanoTime() - firstTime) / 10000000000f).toFloatBuffer());// model
			glUniformMatrix4fv(4, false, Matrix4f.getTranslation(0, 0, -3f).toFloatBuffer());// view
			glUniformMatrix4fv(5, false, Matrix4f.getPerspective(fov, w.getWidth() / ((float) w.getHeight()), 0.01f, 1000f).toFloatBuffer());// projection
			m.render();
			m.render();
			w.swap();
		}
		m.destroy();
		running = false;
	}
}
