package io.proffitt.coherence;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

import io.proffitt.coherence.ai.PlayerAI;
import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.gui.*;
import io.proffitt.coherence.items.Item;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.settings.Value;
import io.proffitt.coherence.world.Entity;
import io.proffitt.coherence.world.Mob;
import io.proffitt.coherence.world.World;

public class Game implements Runnable, SettingsListener, MenuParent {
	Thread			t;
	boolean			running, exitGracefully;
	Configuration	globals;
	Window			w;
	Console			console;
	Camera			perspectiveCam	= new Camera();
	Camera			orthoCam		= new Camera();
	Mob				player			= null;
	World			world;
	public Game(Window wind) {
		globals = ResourceHandler.get().getConfig("globals");
		globals.nullGet("console").setBool(false);
		ResourceHandler.get().getConfig("settings").register(this);
		globals.register(perspectiveCam);
		console = new Console();
		running = false;
		exitGracefully = false;
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
				if ((c >= 'a' && c <= 'z') || c >= 'A' && c <= 'Z') {//letters
					valid = true;
				} else if (c >= '0' && c <= '9') {//numbers
					valid = true;
				} else if (c == '~' || c == ';') {//used for meta commands (binding a command to a key or somesuch)
					valid = true;
				} else if (c == ' ' || c == '=' || c == '!' || c == '+' || c == '-' || c == '*' || c == '/' || c == '?' || c == ':') {//list all operators here
					valid = true;
				} else if (c == '_' || c == '.' || c == '(' || c == ')') {//list all other allowed non-alphanumeric characters here
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
			//logging
			if (globals.nullGet("log_input").getBool()) {
				System.out.println("Key " + key + " pressed.");
			}
			//hardcoded behavior
			if (key == GLFW_KEY_GRAVE_ACCENT) {
				if ((mods & GLFW_MOD_SHIFT) == 1 && globals.get("console").getBool()) {
				} else {
					globals.get("console").Divide(new Value(true));
					if (globals.get("console").getBool()) {
						console.clearInput();
						globals.nullGet("cursor").setString("normal");
					} else {
						globals.nullGet("cursor").setString("disabled");
					}
				}
			} else if (key == GLFW_KEY_ESCAPE) {
				if (globals.get("console").getBool()) {
					if (!console.clearInput()) {
						globals.get("console").setBool(false);
						globals.nullGet("cursor").setString("disabled");
					}
				} else {
					exitGracefully = true;
				}
			} else {
				//type into console
				if (globals.get("console").getBool()) {
					if (key == GLFW_KEY_ENTER) {
						console.registerTextInput('\n');
					} else if (key == GLFW_KEY_BACKSPACE) {
						console.registerTextInput('\b');
					} else if (key == GLFW_KEY_UP) {
						console.inputUp();
					} else if (key == GLFW_KEY_DOWN) {
						console.inputDown();
					}
				} else {
					//bindable behavior
					Configuration bindings = ResourceHandler.get().getConfig("keybindings");
					if (bindings.get("" + key) != null) {
						String boundEvent = bindings.get("" + key).getString();
						//execute command bound to key
						if (boundEvent.startsWith("~")) {
							console.executeCommand(boundEvent.substring(1));
						} else {
							//various other behaviors
							if (boundEvent.equals("PICKUP")) {
								Entity lookAt = world.getLevel().getClosestEntity(perspectiveCam, 1f);
								if (lookAt != null && lookAt instanceof Item) {
									player.getInventory().addItem((Item) lookAt);
									lookAt.getCell().removeEntity(lookAt);
									//TODO: remove item from level
								}
							}
							//TODO: move keyboard checking out of PlayerAI, and make functions such as PlayerAI.jump() or
							//maybe PlayerAI.performAction("JUMP") or something, so it doesn't reference keyboard input directly.
						}
					}
				}
			}
		} else if (action == GLFW_RELEASE) {
			if (globals.nullGet("log_input").getBool()) {
				System.out.println("Key " + key + " released.");
			}
		}
	}
	double mx = 0, my = 0;
	public void handleMousePos(long window, double xpos, double ypos) {
		if (globals.nullGet("cursor").equals("disabled")) {
			perspectiveCam.rotate((float) (ypos - my) / -200f, (float) (xpos - mx) / -200f, 0);
			Window wind = Window.getWindow(window);
			glfwSetCursorPos(window, wind.getWidth() / 2f, wind.getHeight() / 2f);
			mx = wind.getWidth() / 2f;
			my = wind.getHeight() / 2f;
		} else {
			mx = xpos;
			my = ypos;
			if (globals.nullGet("inventory_open").getBool()) {
				player.getInventory().handleMouse((int) mx, (int) my);
			}
		}
	}
	public void handleMouseClick(long window, int button, int action, int mods) {
		if (action == GLFW_PRESS) {
			if (button == GLFW_MOUSE_BUTTON_LEFT) {
				//LMB pressed
				if (globals.nullGet("inventory_open").getBool()) {
					player.getInventory().handleClick((int) mx, (int) my);
				}
			}
		}
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
		w.setCallbacks(GLFWKeyCallback(this::handleKeyPress), GLFWCharCallback(this::handleTextInput), GLFWScrollCallback(this::handleMouseScroll), GLFWCursorPosCallback(this::handleMousePos),
				GLFWMouseButtonCallback(this::handleMouseClick));
		globals.nullGet("cursor").setString("disabled");
		glfwSetCursorPos(w.getID(), w.getWidth() / 2f, w.getHeight() / 2f);
		mx = w.getWidth() / 2f;
		my = w.getHeight() / 2f;
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClearDepth(1);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		world = new World();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Item penny = new Item("dagger");
				penny.getTransfrom().getPosition().z += 1 + (i * 4.6);
				penny.getTransfrom().getPosition().x += 1 + (j * 4.6);
				world.getLevel().addEntity(penny);
			}
		}
		player = new Mob(null, null, new PlayerAI(w, perspectiveCam));
		globals.register(player.getInventory());
		perspectiveCam.lockTo(player);
		world.getLevel().addEntity(player);
		perspectiveCam.setPos(0, 0, 4).setPerspective().setWidth(w.getWidth()).setHeight(w.getHeight()).setNearPlane(0.1f).setFarPlane(1000f).setRot(-0.2f, (float) (Math.PI * 1.25), 0);
		globals.nullGet("FOV").setFloat(65);
		orthoCam.setOrtho().setWidth(w.getWidth()).setHeight(w.getHeight());
		MenuComponent HUD = new Menu(null, 0, 0, 0, 0);
		HUD.addComponent(new TextComponent(ResourceHandler.get().getFont("Courier New,12"), "Text Test!", 5, 5));
		HUD.addComponent(new TextComponent(ResourceHandler.get().getFont("Courier New,12"), "FPS:", 5, 20));
		HUD.addComponent(new TextComponent(ResourceHandler.get().getFont("Courier New,12"), "$FPS", 40, 20));
		HUD.addComponent(new TextComponent(ResourceHandler.get().getFont("Courier New,12"), "VSYNC:", 5, 35));
		HUD.addComponent(new TextComponent(ResourceHandler.get().getFont("Courier New,12"), "$VSYNC", 54, 35));
		HUD.addComponent(new ImageComponent(ResourceHandler.get().getTexture("crosshair").getAsImage(), w.getWidth() / 2 - ResourceHandler.get().getTexture("crosshair").width / 2,
				w.getHeight() / 2 - ResourceHandler.get().getTexture("crosshair").height / 2));
		HUD.setParent(this);
		int er = GL_NO_ERROR;
		while ((er = glGetError()) != GL_NO_ERROR) {
			System.out.println("OpenGL Error in initialization: " + Integer.toHexString(er));
		}
		// time stuff, no more init after this
		long lastTime = System.nanoTime();
		long sinceFPS = lastTime;
		int FPS = 0;
		while (!exitGracefully) {
			long nT = System.nanoTime();
			double delta = (nT - lastTime) / 1000000000f;
			while (1.0 / delta > 150) {// avoid overheating at high fps
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
			if (!globals.get("console").getBool()) {
				if (w.isKeyDown(GLFW_KEY_M)) {
					world.getLevel().setHeight(perspectiveCam.getX(), perspectiveCam.getZ(), world.getLevel().getHeight(perspectiveCam.getX(), perspectiveCam.getZ()) + 0.1f);
				}
				if (w.isKeyDown(GLFW_KEY_N)) {
					world.getLevel().setHeight(perspectiveCam.getX(), perspectiveCam.getZ(), world.getLevel().getHeight(perspectiveCam.getX(), perspectiveCam.getZ()) - 0.1f);
				}
			}
			// update
			world.update(delta);
			world.getLevel().centerOn(player);
			// render
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ResourceHandler.get().getShader("terrain").bind();
			perspectiveCam.bind();
			world.getLevel().draw(); // draw level
			ResourceHandler.get().getShader("HDRtextured").bind();
			perspectiveCam.bind();
			world.getLevel().drawContents();
			// render UI
			if (globals.get("renderUI").getBool()) {
				glClear(GL_DEPTH_BUFFER_BIT);
				ResourceHandler.get().getShader("2dOrtho").bind();
				orthoCam.bind();
				HUD.draw();
				if (globals.nullGet("inventory_open").getBool()) {
					glClear(GL_DEPTH_BUFFER_BIT);
					player.getInventory().drawGUIBG();
					ResourceHandler.get().getShader("3dOrtho").bind();
					orthoCam.bind();
					glClear(GL_DEPTH_BUFFER_BIT);
					player.getInventory().drawContents();
					ResourceHandler.get().getShader("2dOrtho").bind();
					orthoCam.bind();
					glClear(GL_DEPTH_BUFFER_BIT);
					player.getInventory().drawGUIOverlay();
				}
			}
			// render debug console
			if (globals.get("console").getBool()) {
				ResourceHandler.get().getShader("2dOrtho").bind();
				orthoCam.bind();
				console.draw(w);
			}
			// End of gameloop
			double frameLenMs = (System.nanoTime() - lastTime) / 1000000f;
			if (frameLenMs < 8) {
				System.gc();
			} else {
			}
			w.swap();
			int err = GL_NO_ERROR;
			while ((err = glGetError()) != GL_NO_ERROR) {
				System.out.println("OpenGL Error in render loop: 0x" + Integer.toHexString(err));
			}
		}
		ResourceHandler.get().cleanup();
		w.destroy();
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
		if (globals.get(k) != null) {
			return globals.get(k);
		} else {
			return ResourceHandler.get().getConfig("settings").get(k);
		}
	}
}
