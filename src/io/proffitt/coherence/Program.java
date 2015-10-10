package io.proffitt.coherence;

import io.proffitt.coherence.graphics.GLFWContext;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.settings.Configuration;

public class Program {
	public static void main(String[] args) {
		Configuration config = new Configuration();
		GLFWContext glfw = new GLFWContext();
		Window w = new Window(config, 1400, 800, "coherence");
		Game g = new Game(config, w);
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
