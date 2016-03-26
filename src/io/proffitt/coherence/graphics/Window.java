package io.proffitt.coherence.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;
import io.proffitt.coherence.error.ErrorHandler;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.settings.Value;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;

public class Window implements SettingsListener {
	static ArrayList<Window>	windows	= new ArrayList<Window>();
	long						id;
	int							width, height;
	String						title;
	int							glMaj;
	int							glMin;
	int							glForward;
	int							glProfile;
	int							resizable;
	GLFWKeyCallback				keyCall;
	GLFWCharCallback			charCall;
	GLFWScrollCallback			scrollCall;
	GLFWCursorPosCallback		cursorCall;
	GLFWMouseButtonCallback		mouseCall;
	public Window(int w, int h, String t) {
		width = w;
		height = h;
		title = t;
		glMaj = 4;
		glMin = 3;
		glForward = GL_TRUE;
		glProfile = GLFW_OPENGL_CORE_PROFILE;
		resizable = GL_FALSE;
	}
	@Override
	public void onSettingChanged(String setting, Value newValue) {
		if (setting.equals("VSYNC")) {
			glfwSwapInterval(newValue.getBool() ? 1 : 0);
		} else if (setting.equals("cursor")) {
			setCursorMode(newValue.getString());
		}
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void setCursorMode(String mode) {
		if (mode.equals("disabled")) {
			glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		} else if (mode.equals("hidden")) {
			glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		} else if (mode.equals("normal")) {
			glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		} else {
			System.out.println("INVALID CURSOR MODE!");
		}
	}
	public void setCallbacks(GLFWKeyCallback keyC, GLFWCharCallback charC, GLFWScrollCallback scrollC, GLFWCursorPosCallback cursorC, GLFWMouseButtonCallback mouseC) {
		cleanupCallbacks();
		keyCall = keyC;
		charCall = charC;
		scrollCall = scrollC;
		cursorCall = cursorC;
		mouseCall = mouseC;
		glfwSetKeyCallback(id, keyCall);
		glfwSetCharCallback(id, charCall);
		glfwSetScrollCallback(id, scrollCall);
		glfwSetCursorPosCallback(id, cursorCall);
		glfwSetMouseButtonCallback(id, mouseCall);
	}
	public void create() {
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, glMaj);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, glMin);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, glForward);
		glfwWindowHint(GLFW_OPENGL_PROFILE, glProfile);
		glfwWindowHint(GLFW_RESIZABLE, resizable);
		glfwWindowHint(GLFW_SAMPLES, ResourceHandler.get().getConfig("settings").get("MSAA").getInt());
		id = glfwCreateWindow(width, height, title, NULL, NULL);
		ErrorHandler.get().handle(id != NULL);
		synchronized (windows) {
			windows.add(this);
		}
		makeCurrent();
		GL.createCapabilities();
		glfwSwapInterval(ResourceHandler.get().getConfig("settings").get("VSYNC").getBool() ? 1 : 0);
		ResourceHandler.get().getConfig("settings").register(this);
		ResourceHandler.get().getConfig("globals").register(this);
	}
	public boolean isKeyDown(int key) {
		return glfwGetKey(id, key) == GLFW_PRESS;
	}
	public void makeCurrent() {
		glfwMakeContextCurrent(id);
	}
	public void poll() {
		glfwPollEvents();
	}
	public void swap() {
		glfwSwapBuffers(id);
	}
	private void cleanupCallbacks() {
		if (keyCall != null) {
			keyCall.release();
		}
		if (charCall != null) {
			charCall.release();
		}
		if (scrollCall != null) {
			scrollCall.release();
		}
		if (cursorCall != null) {
			cursorCall.release();
		}
		if (mouseCall != null) {
			mouseCall.release();
		}
	}
	public void destroy() {
		cleanupCallbacks();
		glfwDestroyWindow(id);
	}
	public long getID() {
		return id;
	}
	public static Window getWindow(long ID) {
		Window ret = null;
		boolean cont = true;
		synchronized (windows) {
			for (int i = 0; i < windows.size() && cont; i++) {
				if (windows.get(i).id == ID) {
					ret = windows.get(i);
					cont = false;
				}
			}
		}
		return ret;
	}
}
