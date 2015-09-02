package io.proffitt.coherence;

import io.proffitt.coherence.graphics.GLFWContext;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.math.Matrix4f;

public class Program {
	public static void main(String[] args) {
		Matrix4f hi = new Matrix4f();
		System.out.println(hi);
		
		int i = 0;
		if (i == 0) {
			return;
		}
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
