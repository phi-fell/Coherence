package io.proffitt.coherence.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;
import io.proffitt.coherence.error.ErrorHandler;
import io.proffitt.coherence.gui.MenuParent;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.settings.Value;

import java.util.ArrayList;

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
		}
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void setCallbacks(GLFWKeyCallback keyC, GLFWScrollCallback scrollC, GLFWCursorPosCallback cursorC, GLFWMouseButtonCallback mouseC) {
		keyCall = keyC;
		scrollCall = scrollC;
		cursorCall = cursorC;
		mouseCall = mouseC;
		glfwSetKeyCallback(id, keyCall);
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
	public void destroy() {
		keyCall.release();
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
