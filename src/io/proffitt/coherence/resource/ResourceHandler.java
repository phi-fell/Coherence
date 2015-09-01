package io.proffitt.coherence.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ResourceHandler {
	private static ResourceHandler	rh;
	public static ResourceHandler get() {
		if (rh == null) {
			rh = new ResourceHandler();
		}
		return rh;
	}
	private HashMap<String, Shader>	shaders;
	private ResourceHandler() {
		shaders = new HashMap<String, Shader>();
	}
	public Shader getShader(String name) {
		if (!shaders.containsKey(name)) {
			shaders.put(name, loadShader(name));
		}
		return shaders.get(name);
	}
	private Shader loadShader(String name) {
		return new Shader(loadResourceAsString("res/shader/" + name + "_vertex.glsl"), loadResourceAsString("res/shader/" + name + "_fragment.glsl"));
	}
	private String loadResourceAsString(String path) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(this.getClass().getResource(path).getFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder contents = new StringBuilder("");
		while (scanner.hasNextLine()) {
			contents.append(scanner.nextLine()).append("\n");
		}
		scanner.close();
		return contents.toString();
	}
}
