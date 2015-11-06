package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL20.glUniform1f;
import io.proffitt.coherence.graphics.*;
import io.proffitt.coherence.gui.Console;
import io.proffitt.coherence.gui.MenuComponent;
import io.proffitt.coherence.gui.MenuParent;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.settings.Value;
import io.proffitt.coherence.world.Entity;
import io.proffitt.coherence.world.Level;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Game implements Runnable, SettingsListener, MenuParent {
	Thread			t;
	boolean			running;
	Configuration	globals;
	Window			w;
	Console			console;
	Camera			perspectiveCam	= new Camera();
	Camera			orthoCam		= new Camera();
	public Game(Window wind) {
		globals = ResourceHandler.get().getConfig("globals");
		globals.nullGet("console").setBool(false);
		ResourceHandler.get().getConfig("settings").register(this);
		console = new Console();
		running = false;
		w = wind;
	}
	@Override
	public void onSettingChanged(String setting, Value newValue) {
		if (setting.equals("MSAA")) {
			System.out.println("Changing MSAA requires a restart to take effect!");
		}
	}
	public boolean isRunning() {
		return running;
	}
	public void handleTextInput(long window, int key) {
		if (globals.get("console").getBool()) {
			if (Character.charCount(key) == 1) {
				char c = Character.toChars(key)[0];
				boolean valid = false;
				if (c >= 'a' && c <= 'z') {
					valid = true;
				} else if (c >= 'A' && c <= 'Z') {
					valid = true;
				} else if (c >= '0' && c <= '9') {
					valid = true;
				} else if (c == ' ' || c == '=') {//list all operators here
					valid = true;
				}
				if (valid) {
					console.registerTextInput(c);
				}
			}
		}
	}
	public void handleKeyPress(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			if (key == GLFW_KEY_GRAVE_ACCENT) {
				globals.get("console").Divide(new Value(true));
				if (globals.get("console").getBool()) {
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
				} else {
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
				}
			} else if (key == GLFW_KEY_ESCAPE) {
				if (globals.get("console").getBool()) {
					globals.get("console").setBool(false);
				} else {
					Window.getWindow(window).destroy();
					System.exit(0);
				}
			} else {
				if (globals.get("console").getBool()) {
					if (key == GLFW_KEY_ENTER) {
						console.registerTextInput('\n');
					} else if (key == GLFW_KEY_BACKSPACE){
						console.registerTextInput('\b');
					}
				} else {
					if (key == GLFW_KEY_V) {
						Value vsync = ResourceHandler.get().getConfig("settings").nullGet("VSYNC");
						vsync.setBool(!vsync.getBool());
					}
				}
			}
		} else if (action == GLFW_RELEASE) {
		}
	}
	double mx = 0, my = 0;
	public void handleMousePos(long window, double xpos, double ypos) {
		if (glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
			perspectiveCam.rotate((float) (ypos - my) / -200f, (float) (xpos - mx) / -200f, 0);
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
		w.setCallbacks(GLFWKeyCallback(this::handleKeyPress), GLFWCharCallback(this::handleTextInput), GLFWScrollCallback(this::handleMouseScroll), GLFWCursorPosCallback(this::handleMousePos), GLFWMouseButtonCallback(this::handleMouseClick));
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
		perspectiveCam.setPos(0, 0, 4).setPerspective().setWidth(w.getWidth()).setHeight(w.getHeight()).setFOV(65).setNearPlane(0.01f).setFarPlane(1000f);
		orthoCam.setOrtho().setWidth(w.getWidth()).setHeight(w.getHeight());
		MenuComponent HUD = ResourceHandler.get().getMenu("hud");
		HUD.setParent(this);
		FrameBuffer HDRFBO = new FrameBuffer(w.getWidth(), w.getHeight());
		FloatBuffer pbuffer = BufferUtils.createFloatBuffer(HDRFBO.getWidth() * HDRFBO.getHeight() * 3);
		float[] fbverts = { -1, -1, 0, 0, 0, 0, 1, -1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, -1, -1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, -1, 1, 0, 0, 1, 0 };
		Model FBModel = new Model(fbverts);
		int er = GL_NO_ERROR;
		while ((er = glGetError()) != GL_NO_ERROR) {
			System.out.println("OpenGL Error in initialization: " + Integer.toHexString(er));
		}
		globals.nullGet("calcHDR").setBool(false);
		globals.nullGet("HDRMax").setDouble(1);
		HDRCalculator HDRcalc = new HDRCalculator();
		// time stuff, no more init after this
		long lastTime = System.nanoTime();
		long sinceFPS = lastTime;
		int FPS = 0;
		while (running) {
			long nT = System.nanoTime();
			double delta = (nT - lastTime) / 1000000000f;
			while (1.0 / delta > 400) {// avoid overheating at high fps
				nT = System.nanoTime();
				delta = (nT - lastTime) / 1000000000f;
			}
			if (nT - sinceFPS >= 1000000000) {
				globals.nullGet("FPS").setInt(FPS);
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
			if (globals.get("console").getBool()) {
				console.draw(w);
			} else {
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
					globals.get("calcHDR").setBool(true);
				}
				if (w.isKeyDown(GLFW_KEY_R)) {
					globals.get("calcHDR").setBool(false);
					globals.get("HDRMax").setDouble(globals.get("HDRMax").getDouble() + 0.01);
				}
				if (w.isKeyDown(GLFW_KEY_F)) {
					globals.get("calcHDR").setBool(false);
					globals.get("HDRMax").setDouble(globals.get("HDRMax").getDouble() - 0.01);
				}
				if (w.isKeyDown(GLFW_KEY_O)) {
					perspectiveCam.setFOV(perspectiveCam.getFOV() - 1f);
				}
				if (w.isKeyDown(GLFW_KEY_L)) {
					perspectiveCam.setFOV(perspectiveCam.getFOV() + 1f);
				}
				if (w.isKeyDown(GLFW_KEY_M)) {
					level.setHeight(perspectiveCam.getX(), perspectiveCam.getZ(), level.getHeight(perspectiveCam.getX(), perspectiveCam.getZ()) + 0.1f);
				}
				if (w.isKeyDown(GLFW_KEY_N)) {
					level.setHeight(perspectiveCam.getX(), perspectiveCam.getZ(), level.getHeight(perspectiveCam.getX(), perspectiveCam.getZ()) - 0.1f);
				}
			}
			perspectiveCam.move(zMod * speed * (float) delta, xMod * speed * (float) delta);
			// update
			monkey.getTransfrom().getRotation().y += (float) (delta / 10);
			// render
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			HDRFBO.bind();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ResourceHandler.get().getShader("HDRdefault").bind();
			perspectiveCam.bind();
			monkey.draw(); // render monkey
			ResourceHandler.get().getShader("terrain").bind();
			ResourceHandler.get().getTexture("grass").bind();
			perspectiveCam.bind();
			level.draw(); // draw level
			HDRFBO.unbind();
			// calculate HDRmax
			if (globals.get("calcHDR").getBool()) {
				// HDRFBO.blit();
				HDRFBO.ssbind();
				pbuffer.clear();
				glReadPixels(0, 0, HDRFBO.getWidth(), HDRFBO.getHeight(), GL_RGB, GL_FLOAT, pbuffer);
				HDRFBO.unbind();
				HDRcalc.calculate(pbuffer, HDRFBO.getWidth(), HDRFBO.getHeight());
				globals.get("HDRMax").setDouble((globals.get("HDRMax").getDouble() * 0.98f) + (HDRcalc.getValue() * 0.02f));
			}
			// Render HDRFBO
			ResourceHandler.get().getShader("HDR").bind();
			glUniform1f(7, globals.get("HDRMax").getFloat());
			HDRFBO.blit();
			HDRFBO.getTexture().bind();
			FBModel.render();
			HDRFBO.getTexture().unbind();
			// render text
			ResourceHandler.get().getShader("2dOrtho").bind();
			orthoCam.bind();
			HUD.draw();
			// render debug console
			if (globals.get("console").getBool()) {
				console.draw(w);
			}
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
	@Override
	public int getWidth() {
		return w.getWidth();
	}
	@Override
	public int getHeight() {
		return w.getHeight();
	}
	@Override
	public int getX() {
		return 0;
	}
	@Override
	public int getY() {
		return 0;
	}
	@Override
	public Value getValue(String k) {
		return globals.get(k);
	}
}
