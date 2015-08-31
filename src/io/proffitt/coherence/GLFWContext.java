package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import io.proffitt.coherence.error.ErrorHandler;

public class GLFWContext {
	public GLFWContext() {
		ErrorHandler.get().handle(glfwInit() == GL_TRUE);
	}
	public void destroy() {
		glfwTerminate();
	}
}
