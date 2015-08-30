package io.proffitt.coherence.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;

import org.lwjgl.opengl.GLContext;

public class Window {
	static ArrayList<Window>	windows	= new ArrayList<Window>();
	long						id;
	int							width, height;
	String						title;
	int							glMaj;
	int							glMin;
	int							glForward;
	int							glProfile;
	int							resizable;
	int							samples;
	public Window(int w, int h, String t) {
		width = w;
		height = h;
		title = t;
		glMaj = 4;
		glMin = 2;
		glForward = GL_TRUE;
		glProfile = GLFW_OPENGL_CORE_PROFILE;
		resizable = GL_FALSE;
		samples = 0;
	}
	public void create() {
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, glMaj);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, glMin);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, glForward);
		glfwWindowHint(GLFW_OPENGL_PROFILE, glProfile);
		glfwWindowHint(GLFW_RESIZABLE, resizable);
		glfwWindowHint(GLFW_SAMPLES, samples);
		id = glfwCreateWindow(width, height, title, NULL, NULL);
		if (id == NULL) {
			System.out.println("ERROR in window creation");
			return;
		}
		synchronized (windows) {
			windows.add(this);
		}
		makeCurrent();
		GLContext.createFromCurrent();
		glfwSwapInterval(1);
	}
	public boolean isKeyDown(int key){
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
