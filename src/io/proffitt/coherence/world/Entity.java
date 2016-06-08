package io.proffitt.coherence.world;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.util.ArrayList;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Mesh;
import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.math.Transform;
import io.proffitt.coherence.math.Vector3f;
import io.proffitt.coherence.resource.Texture;
import io.proffitt.coherence.world.component.Model;

public abstract class Entity {
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static int getGUID(Entity e) {
		entities.add(e);
		return entities.size() - 1;
	}
	public static Entity getEntity(int eID) {
		return entities.get(eID);
	}
	public static Mesh	defaultModel	= new Mesh(new float[] { -0.25f, 0, -0.25f, 0, 0, 0, 0.25f, 1.75f, 0.25f, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, false, false);
	public final int	GUID;
	private Cell		parentCell;
	float				onGround;
	protected Model		model;
	protected Transform	transform;
	Vector3f			velocity;
	private Camera		locked			= null;
	public Entity(Model m) {
		GUID = Entity.getGUID(this);
		parentCell = null;
		onGround = 0;
		velocity = new Vector3f();
		model = m;
		transform = new Transform();
	}
	public void addToCell(Cell c) {
		parentCell = c;
	}
	public Cell getCell() {
		return parentCell;
	}
	public boolean isOnGround() {
		return onGround <= 0;
	}
	public void lockToGround(float dH, double delta) {
		onGround = dH - (float) (this.getAABB().ry);
		if (onGround > 0) {
			velocity.y -= 36 * delta;
		}
		if (onGround < 0) {
			transform.getPosition().y -= onGround;
			onGround = 0;
			velocity.y = 0;
		}
	}
	public void update(double delta) {
		transform.getPosition().addInPlace(velocity.times((float) delta));
	}
	void lockCamera() {
		if (locked != null) {
			Vector3f pos = transform.getPosition();
			locked.setPos(pos.x, pos.y, pos.z);
		}
	}
	public Transform getTransfrom() {
		return transform;
	}
	public Vector3f getVelocity() {
		return velocity;
	}
	public AABB getAABB() {
		return new AABB(model.getMesh().getAABB(), transform);
	}
	public void draw() {
		if (model != null) {
			glUniformMatrix4fv(4, false, transform.getAsMatrix().toFloatBuffer());// entity
			model.draw();
		}
	}
	public void lockCamera(Camera camera) {
		locked = camera;
	}
}
