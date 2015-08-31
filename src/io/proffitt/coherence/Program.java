package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import io.proffitt.coherence.graphics.Window;

public class Program {
	public static void main(String[] args) {
		GLFWContext glfw = new GLFWContext();
		Window w = new Window(800, 600, "coherence");
		Game g = new Game(w);
		g.start();
		while (g.isRunning()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		glfw.destroy();
	}
}
