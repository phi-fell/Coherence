package io.proffitt.coherence.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.world.Cell;
import io.proffitt.coherence.world.Entity;
import io.proffitt.coherence.world.Level;

public class ResourceHandler {
	private static ResourceHandler rh;
	public static ResourceHandler get() {
		if (rh == null) {
			rh = new ResourceHandler();
		}
		return rh;
	}
	private HashMap<String, CMLFile>		properties;
	private HashMap<String, ItemSchematic>	items;
	private HashMap<String, Configuration>	configs;
	private HashMap<String, Texture>		textures;
	private HashMap<String, Font>			fonts;
	private HashMap<String, Shader>			shaders;
	private HashMap<String, Model>			models;
	private ResourceHandler() {
		properties = new HashMap<String, CMLFile>();
		items = new HashMap<String, ItemSchematic>();
		configs = new HashMap<String, Configuration>();
		textures = new HashMap<String, Texture>();
		fonts = new HashMap<String, Font>();
		shaders = new HashMap<String, Shader>();
		models = new HashMap<String, Model>();
	}
	/**
	 * Removes and cleans up all cached resources. ResourceHandler may still be
	 * used after this, but accessing any resource will require it to be
	 * reloaded from disk (or generated) and into memory (and/or onto the GPU).
	 */
	public void cleanup() {
		//this.saveConfig("settings");//save settings
		this.saveConfig("keybindings");//save bound keys
		for (String s : items.keySet().toArray(new String[0])) {
			properties.remove(s);// props don't need cleanup
		}
		for (String s : items.keySet().toArray(new String[0])) {//TODO: is there a reason for '.toArray(new String[0])' ???
			items.remove(s);// items don't need cleanup
		}
		for (String s : configs.keySet().toArray(new String[0])) {
			configs.remove(s);// configs don't need cleanup
		}
		for (String s : textures.keySet().toArray(new String[0])) {
			textures.remove(s).destroy();
		}
		for (String s : fonts.keySet().toArray(new String[0])) {
			fonts.remove(s);// fonts don't need cleanup
		}
		for (String s : shaders.keySet().toArray(new String[0])) {
			shaders.remove(s).destroy();
		}
		for (String s : models.keySet().toArray(new String[0])) {
			models.remove(s).destroy();
		}
	}
	public CMLFile getProperty(String name) {
		if (!properties.containsKey(name)) {
			properties.put(name, loadProperty(name));
		}
		return properties.get(name);
	}
	public CMLFile loadProperty(String name) {
		return this.loadResourceAsCML("res/prop/" + name + ".cml");
	}
	public ItemSchematic getItem(String name) {
		if (!items.containsKey(name)) {
			items.put(name, loadItem(name));
		}
		return items.get(name);
	}
	public ItemSchematic loadItem(String name) {
		return new ItemSchematic(name, this.loadResourceAsCML("res/item/" + name + ".item"));
	}
	public Configuration getConfig(String name) {
		if (!configs.containsKey(name)) {
			configs.put(name, loadConfig(name));
		}
		return configs.get(name);
	}
	private Configuration loadConfig(String name) {
		Configuration ret = new Configuration();
		ret.loadFromCML(this.loadResourceAsCML("res/config/" + name + ".cml"));
		return ret;
	}
	public Texture getTexture(String name) {
		if (!textures.containsKey(name)) {
			textures.put(name, loadTexture(name));
		}
		return textures.get(name);
	}
	private Texture loadTexture(String name) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("res/tex/" + name + ".png"));
		} catch (IOException e) {
			System.out.println("Could not load texture \"" + name + "\"");
			e.printStackTrace();
		}
		return new Texture(img);
	}
	public Font getFont(String name) {
		if (!fonts.containsKey(name)) {
			fonts.put(name, loadFont(name));
		}
		return fonts.get(name);
	}
	private Font loadFont(String name) {
		String fontData[] = name.split(",");
		return new Font(fontData[0].trim(), Integer.parseInt(fontData[1].trim()));
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
		int uvnum = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("f")) {
				facenum++;
			} else if (lines[i].startsWith("vn")) {
				normnum++;
			} else if (lines[i].startsWith("vt")) {
				uvnum++;
			} else if (lines[i].startsWith("v")) {
				vertnum++;
			}
		}
		float[] rawverts = new float[vertnum * 3];
		int vertpos = 0;
		float[] rawnorms = new float[normnum * 3];
		int normpos = 0;
		float[] rawuvs = new float[uvnum * 2];
		int uvpos = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("vn")) {
				String[] components = lines[i].split(" ");
				rawnorms[normpos] = Float.parseFloat(components[1]);
				rawnorms[normpos + 1] = Float.parseFloat(components[2]);
				rawnorms[normpos + 2] = Float.parseFloat(components[3]);
				normpos += 3;
			} else if (lines[i].startsWith("vt")) {
				String[] components = lines[i].split(" ");
				rawuvs[uvpos] = Float.parseFloat(components[1]);
				rawuvs[uvpos + 1] = 1 - Float.parseFloat(components[2]);
				uvpos += 2;
			} else if (lines[i].startsWith("v")) {
				String[] components = lines[i].split(" ");
				rawverts[vertpos] = Float.parseFloat(components[1]);
				rawverts[vertpos + 1] = Float.parseFloat(components[2]);
				rawverts[vertpos + 2] = Float.parseFloat(components[3]);
				vertpos += 3;
			}
		}
		float[] verts = new float[facenum * 24];
		int facepos = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("f")) {
				String[] components = lines[i].split(" ");
				for (int k = 0; k < 3; k++) {
					String[] c = components[k + 1].split("/");
					int vpos = (Integer.parseInt(c[0]) - 1) * 3;
					int upos = (Integer.parseInt(c[1]) - 1) * 2;
					int npos = (Integer.parseInt(c[2]) - 1) * 3;
					for (int j = 0; j < 3; j++) {
						verts[facepos + j] = rawverts[vpos + j];
					}
					for (int j = 0; j < 3; j++) {
						verts[facepos + j + 3] = rawnorms[npos + j];
					}
					for (int j = 0; j < 2; j++) {
						verts[facepos + j + 6] = rawuvs[upos + j];
					}
					facepos += 8;
				}
			}
		}
		Model m = new Model(verts, true);
		return m;
	}
	private CMLFile loadResourceAsCML(String path) {
		return new CMLFile(loadResourceAsString(path));
	}
	private void saveConfig(String name) {
		this.saveResourceAsString("res/config/" + name + ".cml", configs.get(name).toString());
	}
	private String loadResourceAsString(String path) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));
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
	private void saveResourceAsString(String path, String contents) {
		System.out.println("Saving: " + path);
		PrintWriter pw;
		try {
			pw = new PrintWriter(path);
			pw.println(contents);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Cell loadCell(Level l, int x, int y) {
		//TODO: implement cell loading (for previously existing cells)
		//TODO: implement actual cell generation algorithm for nonexistent cells (i.e. not simple trig functions)
		int SIZE = Level.CELL_SIZE;
		float[][] height = new float[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				//height[i][j] = (float) ((((i - (SIZE / 2)) * (i - (SIZE / 2))) + ((j - (SIZE / 2)) * (j - (SIZE / 2)))) / ((SIZE * SIZE) / 16.0) - 8);
				height[i][j] = -1.5f;
				height[i][j] += (Math.random() - 0.5f) * 0.2;
				height[i][j] += Math.sin(((x * SIZE) + i) * 0.07);
				height[i][j] += Math.cos(((y * SIZE) + j) * 0.04);
				height[i][j] += 3 * Math.sin((((x * SIZE) + i) + ((y * SIZE) + j)) * 0.03);
			}
		}
		return new Cell(l, x, y, SIZE, height, new ArrayList<Entity>());
	}
	public static void saveCell(Cell cell) {
		//TODO: implement cell saving
	}
}
