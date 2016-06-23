package io.proffitt.coherence.world;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Mesh;
import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.math.Vector3f;
import io.proffitt.coherence.resource.Texture;
import io.proffitt.coherence.world.component.Component;
import io.proffitt.coherence.world.component.Model;

public abstract class Entity {
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static int assignGUID(Entity e) {
		entities.add(e);
		return entities.size() - 1;
	}
	public static Entity getEntity(int eID) {
		return entities.get(eID);
	}
	public final int						GUID;
	private Cell							parentCell;
	protected HashMap<Class<?>, Component>	components;
	public Entity(Collection<Component> comps) {
		GUID = Entity.assignGUID(this);
		parentCell = null;
		components = new HashMap<Class<?>, Component>();
		for (Component c : comps) {
			components.put(c.getClass(), c);
		}
		for (Component c : components.values()) {
			c.setParent(this);
		}
	}
	public <T> T getComponent(Class<T> c) {
		return c.cast(components.get(c.getClass()));
	}
	public void addToCell(Cell c) {
		getComponent(Transform.class);
		parentCell = c;
	}
	public Cell getCell() {
		return parentCell;
	}
	public void update(double delta) {
		for (Component c : components.values()) {
			c.update();
		}
	}
	public void draw() {
		for (Component c : components.values()) {
			c.draw();
		}
	}
}
