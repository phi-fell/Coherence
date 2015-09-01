package io.proffitt.coherence.graphics;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import io.proffitt.coherence.error.ErrorHandler;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;

public class GLFWContext {
	GLFWErrorCallback	errorCall;
	public GLFWContext() {
		ErrorHandler.get().handle(glfwInit() == GL_TRUE);
		glfwSetErrorCallback(errorCall = Callbacks.errorCallbackPrint(System.err));
	}
	public void destroy() {
		errorCall.release();
		glfwTerminate();
	}
}
