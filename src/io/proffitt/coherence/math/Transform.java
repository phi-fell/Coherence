package io.proffitt.coherence.math;

public class Transform {
	Vector3f	pos;
	Vector3f	rot;
	Vector3f	scale;
	public Transform() {
		pos = new Vector3f();
		rot = new Vector3f();
		scale = new Vector3f(1, 1, 1);
	}
	public Vector3f getPosition() {
		return pos;
	}
	public void setPosition(Vector3f newPos) {
		pos.x = newPos.x;
		pos.y = newPos.y;
		pos.z = newPos.z;
	}
	public Vector3f getRotation() {
		return rot;
	}
	public Vector3f getScale() {
		return scale;
	}
	public Matrix4f getAsMatrix() {
		return Matrix4f.getTranslation(pos.x, pos.y, pos.z).multiply(Matrix4f.getRotationZ(rot.z)).multiply(Matrix4f.getRotationX(rot.x)).multiply(Matrix4f.getRotationY(rot.y)).multiply(Matrix4f.getScale(scale.x, scale.y, scale.z));
	}
}
