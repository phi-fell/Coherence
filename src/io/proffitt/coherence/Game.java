package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import io.proffitt.coherence.error.ErrorHandler;
import io.proffitt.coherence.graphics.*;
import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Vector4f;
import io.proffitt.coherence.resource.Font;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.world.Entity;
import io.proffitt.coherence.world.Level;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;

public class Game implements Runnable, SettingsListener {
	Thread			t;
	boolean			running;
	Configuration	config;
	Window			w;
	Camera			cam				= new Camera();
	boolean			debugConsole	= false;
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
			if (key == GLFW_KEY_GRAVE_ACCENT) {
				debugConsole = !debugConsole;
				if (debugConsole) {
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
		glfwSetInputMode(w.getID(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSetCursorPos(w.getID(), w.getWidth() / 2f, w.getHeight() / 2f);
		mx = w.getWidth() / 2f;
		my = w.getHeight() / 2f;
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClearDepth(1);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		Entity monkey = new Entity(ResourceHandler.get().getModel("smoothmonkey"));
		Level level = new Level(10, 10);
		int fov = 65;
		cam.setPos(0, 0, 4);
		cam.setRot(0, 0, 0);
		Text fpsText = ResourceHandler.get().getFont("Courier New,12").getText("FPS: -1");
		FrameBuffer HDRFBO = new FrameBuffer(w.getWidth(), w.getHeight());
		FloatBuffer pbuffer = BufferUtils.createFloatBuffer(HDRFBO.getWidth() * HDRFBO.getHeight() * 3);
		float[] fbverts = { -1, -1, 0, 0, 0, 0, 1, -1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, -1, -1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, -1, 1, 0, 0, 1, 0 };
		Model FBModel = new Model(fbverts);
		int er = GL_NO_ERROR;
		while ((er = glGetError()) != GL_NO_ERROR) {
			System.out.println("OpenGL Error in initialization: " + Integer.toHexString(er));
		}
		float HDRmax = 1;
		boolean autoHDR = false;
		HDRCalculator HDRcalc = new HDRCalculator();
		//time stuff, no more init after this
		long lastTime = System.nanoTime();
		long sinceFPS = lastTime;
		int FPS = 0;
		while (running) {
			long nT = System.nanoTime();
			double delta = (nT - lastTime) / 1000000000f;
			while (1.0 / delta > 400) {//avoid overheating at high fps
				nT = System.nanoTime();
				delta = (nT - lastTime) / 1000000000f;
			}
			if (nT - sinceFPS >= 1000000000) {
				fpsText.destroy();
				fpsText = ResourceHandler.get().getFont("Courier New,12").getText("FPS: " + FPS);
				FPS = 0;
				sinceFPS = nT;
			} else {
				FPS++;
			}
			lastTime = nT;
			w.poll();
			// Start of gameloop
			// handleinput
			int zMod = 0;
			int xMod = 0;
			float speed = 5;
			if (w.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
				speed = 30;
			}
			if (w.isKeyDown(GLFW_KEY_SPACE)) {
				speed = 300;
			}
			if (!debugConsole) {
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
				if (w.isKeyDown(GLFW_KEY_H)) {
					autoHDR = true;
				}
				if (w.isKeyDown(GLFW_KEY_R)) {
					autoHDR = false;
					HDRmax += 0.01;
				}
				if (w.isKeyDown(GLFW_KEY_F)) {
					autoHDR = false;
					HDRmax -= 0.01;
				}
			}
			cam.move(zMod * speed * (float) delta, xMod * speed * (float) delta);
			// update
			monkey.getTransfrom().getRotation().y += (float) (delta / 10);
			// render
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			HDRFBO.bind();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ResourceHandler.get().getShader("HDRdefault").bind();
			glUniformMatrix4fv(4, false, cam.getViewMatrix().toFloatBuffer());// view
			glUniformMatrix4fv(5, false, Matrix4f.getPerspective(fov, w.getWidth() / ((float) w.getHeight()), 0.01f, 1000f).toFloatBuffer());// projection
			monkey.draw(); //render monkey
			ResourceHandler.get().getShader("terrain").bind();
			ResourceHandler.get().getTexture("grass").bind();
			glUniformMatrix4fv(4, false, cam.getViewMatrix().toFloatBuffer());// view
			glUniformMatrix4fv(5, false, Matrix4f.getPerspective(fov, w.getWidth() / ((float) w.getHeight()), 0.01f, 1000f).toFloatBuffer());// projection
			level.draw(); // draw level
			HDRFBO.unbind();
			//calculate HDRmax
			if (autoHDR) {
				//HDRFBO.blit();
				HDRFBO.ssbind();
				pbuffer.clear();
				glReadPixels(0, 0, HDRFBO.getWidth(), HDRFBO.getHeight(), GL_RGB, GL_FLOAT, pbuffer);
				HDRFBO.unbind();
				HDRcalc.calculate(pbuffer, HDRFBO.getWidth(), HDRFBO.getHeight());
				HDRmax = (HDRmax * 0.98f) + (HDRcalc.getValue() * 0.02f);
			}
			//Render HDRFBO
			ResourceHandler.get().getShader("HDR").bind();
			glUniform1f(7, HDRmax);
			HDRFBO.blit();
			HDRFBO.getTexture().bind();
			FBModel.render();
			HDRFBO.getTexture().unbind();
			//render text
			ResourceHandler.get().getShader("text").bind();
			fpsText.draw(w, new Vector4f(0, 0, 0, 0));
			//render debug console
			//TODO: render debug console
			// End of gameloop
			w.swap();
			int err = GL_NO_ERROR;
			while ((err = glGetError()) != GL_NO_ERROR) {
				System.out.println("OpenGL Error in render loop: 0x" + Integer.toHexString(err));
			}
		}
		monkey.destroy();
		ResourceHandler.get().cleanup();
		running = false;
	}
}
