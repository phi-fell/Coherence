package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Font;
import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.graphics.Text;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.resource.Texture;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.world.Cell;

public class Game implements Runnable, SettingsListener {
	Thread			t;
	boolean			running;
	Configuration	config;
	Window			w;
	Camera			cam	= new Camera();
	public Game(Configuration c, Window wind) {
		config = c;
		c.register(this);
		running = false;
		w = wind;
	}
	@Override
	public void onSettingChanged(int setting, int newValue) {
		switch (setting) {
		case Configuration.MSAA:
			System.out.println("Changing MSAA requires a restart to take effect!");
			break;
		default:
			break;
		}
	}
	public boolean isRunning() {
		return running;
	}
	public void handleKeyPress(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			if (key == GLFW_KEY_F5) {
				if (glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
				} else {
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
				}
			} else if (key == GLFW_KEY_ESCAPE) {
				Window.getWindow(window).destroy();
				System.exit(0);
			} else if (key == GLFW_KEY_V) {
				config.set(Configuration.VSYNC, 1 - config.get(Configuration.VSYNC));
			}
		} else if (action == GLFW_RELEASE) {
		}
	}
	double	mx	= 0, my = 0;
	public void handleMousePos(long window, double xpos, double ypos) {
		if (glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
			cam.rotate((float) (ypos - my) / -200f, (float) (xpos - mx) / -200f, 0);
			Window wind = Window.getWindow(window);
			glfwSetCursorPos(window, wind.getWidth() / 2f, wind.getHeight() / 2f);
			mx = wind.getWidth() / 2f;
			my = wind.getHeight() / 2f;
		} else {
			mx = xpos;
			my = ypos;
		}
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
		w.setCallbacks(GLFWKeyCallback(this::handleKeyPress), GLFWScrollCallback(this::handleMouseScroll), GLFWCursorPosCallback(this::handleMousePos), GLFWMouseButtonCallback(this::handleMouseClick));
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClearDepth(1);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		Model m = ResourceHandler.get().getModel("smoothmonkey");
		Cell cell = new Cell();
		int fov = 65;
		cam.setPos(0, 0, 4);
		cam.setRot(0, 0, 0);
		float modelRot = 0;
		Font defaultFont = new Font("Courier New", 12);
		Text helloText = defaultFont.getText("Hello World!\nHow're ya doin?\nI'm doing pretty well, myself.\n\tthis is tabbed in\nthis isn't.\n\t\tthis is double tabbed!!!");
		Text fpsText = defaultFont.getText("FPS: -1");
		long lastTime = System.nanoTime();
		long sinceFPS = lastTime;
		int FPS = 0;
		while (running) {
			long nT = System.nanoTime();
			if (nT - sinceFPS >= 1000000000) {
				fpsText = defaultFont.getText("FPS: " + FPS);
				FPS = 0;
				sinceFPS = nT;
			} else {
				FPS++;
			}
			double delta = (nT - lastTime) / 1000000000f;
			lastTime = nT;
			w.poll();
			// Start of gameloop
			// handleinput
			int zMod = 0;
			int xMod = 0;
			float speed = 3;
			if (w.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
				speed = 8;
			}
			if (glfwGetInputMode(w.getID(), GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
				if (w.isKeyDown(GLFW_KEY_W)) {
					zMod--;
				}
				if (w.isKeyDown(GLFW_KEY_S)) {
					zMod++;
				}
				if (w.isKeyDown(GLFW_KEY_A)) {
					xMod--;
				}
				if (w.isKeyDown(GLFW_KEY_D)) {
					xMod++;
				}
			}
			cam.move(zMod * speed * (float) delta, xMod * speed * (float) delta);
			// update
			modelRot += (float) (delta / 10);
			// render
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ResourceHandler.get().getShader("default").bind();
			glUniformMatrix4fv(4, false, cam.getViewMatrix().toFloatBuffer());// view
			glUniformMatrix4fv(5, false, Matrix4f.getPerspective(fov, w.getWidth() / ((float) w.getHeight()), 0.01f, 1000f).toFloatBuffer());// projection
			//render monkey:
			glUniformMatrix4fv(3, false, Matrix4f.getRotationY(modelRot).toFloatBuffer());// model
			m.render();
			//done with monkey
			cell.draw();
			//render text
			ResourceHandler.get().getShader("text").bind();
			fpsText.draw(w, new Vector4f(0, 0, 0, 0));
			// End of gameloop
			w.swap();
		}
		m.destroy();
		running = false;
	}
}
