package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.GLFWCursorPosCallback;
import static org.lwjgl.glfw.GLFW.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.GLFWScrollCallback;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.system.MathUtil.*;

import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.resource.ResourceHandler;

public class Game implements Runnable {
	Thread t;
	boolean running;
	Window w;
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
		w.setCallbacks(GLFWKeyCallback(this::handleKeyPress), GLFWScrollCallback(this::handleMouseScroll), GLFWCursorPosCallback(this::handleMousePos),
				GLFWMouseButtonCallback(this::handleMouseClick));
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_GREATER);
		glClearDepth(0);
		glEnable(GL_MULTISAMPLE);
		Model m = ResourceHandler.get().getModel("smoothmonkey");
		while (running) {
			w.poll();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ResourceHandler.get().getShader("default").bind();
			//glUniformMatrix4fv(3, false, null);//model
			//glUniformMatrix4fv(4, false, null);//view
			//glUniformMatrix4fv(5, false, null);//projection
			m.render();
			w.swap();
		}
		m.destroy();
		running = false;
	}
}
