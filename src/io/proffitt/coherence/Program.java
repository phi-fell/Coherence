package io.proffitt.coherence;

import io.proffitt.coherence.graphics.GLFWContext;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.math.Matrix4f;

public class Program {
	public static void main(String[] args) {
		System.out.println(Matrix4f.getPerspective(1.6f, 4f/3f, 0.1f, 3f).toString());
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
