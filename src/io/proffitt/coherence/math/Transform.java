package io.proffitt.coherence.math;

public class Transform {
	Vector4f	pos;
	Vector4f	rot;
	Vector4f	scale;
	public Transform() {
		pos = new Vector4f();
		rot = new Vector4f();
		scale = new Vector4f(1, 1, 1, 1);
	}
	public Vector4f getPosition() {
		return pos;
	}
	public Vector4f getRotation() {
		return rot;
	}
	public Vector4f getScale() {
		return scale;
	}
	public Matrix4f getAsMatrix() {
		return Matrix4f.getScale(scale.x, scale.y, scale.z).multiply(Matrix4f.getRotationZ(rot.z).multiply(Matrix4f.getRotationX(rot.x).multiply(Matrix4f.getRotationY(rot.y).multiply(Matrix4f.getTranslation(pos.x, pos.y, pos.z)))));
	}
}
