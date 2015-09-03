package io.proffitt.coherence.resource;

import io.proffitt.coherence.graphics.Model;

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
	private HashMap<String, Model>	models;
	private ResourceHandler() {
		shaders = new HashMap<String, Shader>();
		models = new HashMap<String, Model>();
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
	public Model getModel(String name) {
		if (!models.containsKey(name)) {
			models.put(name, loadModel(name));
		}
		return models.get(name);
	}
	private Model loadModel(String name) {
		String file = this.loadResourceAsString("res/model/" + name + ".obj");
		String[] lines = file.split("\n");
		int facenum = 0;
		int vertnum = 0;
		int normnum = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("f")) {
				facenum++;
			} else if (lines[i].startsWith("vn")) {
				normnum++;
			} else if (lines[i].startsWith("v")) {
				vertnum++;
			}
		}
		float[] rawverts = new float[vertnum * 3];
		int vertpos = 0;
		float[] rawnorms = new float[normnum * 3];
		int normpos = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("vn")) {
				String[] components = lines[i].split(" ");
				rawnorms[normpos] = Float.parseFloat(components[1]);
				rawnorms[normpos + 1] = Float.parseFloat(components[2]);
				rawnorms[normpos + 2] = Float.parseFloat(components[3]);
				normpos += 3;
			} else if (lines[i].startsWith("v")) {
				String[] components = lines[i].split(" ");
				rawverts[vertpos] = Float.parseFloat(components[1]);
				rawverts[vertpos + 1] = Float.parseFloat(components[2]);
				rawverts[vertpos + 2] = Float.parseFloat(components[3]);
				vertpos += 3;
			}
		}
		float[] verts = new float[facenum * 18];
		int facepos = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("f")) {
				String[] components = lines[i].split(" ");
				for (int k = 0; k < 3; k++) {
					String[] c = components[k + 1].split("//");
					int vpos = (Integer.parseInt(c[0]) - 1) * 3;
					int npos = (Integer.parseInt(c[1]) - 1) * 3;
					for (int j = 0; j < 3; j++) {
						verts[facepos + j] = rawverts[vpos + j];
					}
					for (int j = 0; j < 3; j++) {
						verts[facepos + j + 3] = rawnorms[npos + j];
					}
					facepos += 6;
				}
			}
		}
		Model m = new Model(verts);
		return m;
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
