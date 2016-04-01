package io.proffitt.coherence.world;

import java.util.HashMap;

public class World {
	private HashMap<String, Level> levels;
	public World() {
		levels = new HashMap<String, Level>();
		levels.put("overworld", new Level(this, 0, 0));
	}
	public Level getLevel() {
		return levels.get("overworld");
	}
	public void update(double dt) {
		for (Level l : levels.values()) {
			l.update(dt);
		}
	}
}
