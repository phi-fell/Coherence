package io.proffitt.coherence.math;

public class Vector3f {
	public float x, y, z; // 3d vector in homogeneous coordinates
	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	public Vector3f(float a, float b, float c) {
		x = a;
		y = b;
		z = c;
	}
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	public float getLenSq() {
		return (x * x) + (y * y) + (z * z);
	}
	public float getLength() {
		return (float) Math.sqrt(getLenSq());
	}
	public void normalize() {
		float len = getLength();
		if (len == 0) {
			return;
		}
		x /= len;
		y /= len;
		z /= len;
	}
	public Vector3f times(float s) {
		return new Vector3f(x * s, y * s, z * s);
	}
	public Vector3f plus(Vector3f rhs) {
		return new Vector3f(x + rhs.x, y + rhs.y, z + rhs.z);
	}
	public Vector3f minus(Vector3f rhs) {
		return new Vector3f(x - rhs.x, y - rhs.y, z - rhs.z);
	}
	public float dot(Vector3f rhs) {
		return (this.x * rhs.x) + (this.y * rhs.y) + (this.z * rhs.z);
	}
	public Vector3f cross(Vector3f rhs) {
		return new Vector3f((y * rhs.z) - (z * rhs.y), (z * rhs.x) - (x * rhs.z), (x * rhs.y) - (y * rhs.x));
	}
	public Vector3f multiplyInPlace(float s) {
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	public Vector3f addInPlace(Vector3f rhs) {
		x += rhs.x;
		y += rhs.y;
		z += rhs.z;
		return this;
	}
	public Vector3f subtractInPlace(Vector3f rhs) {
		x -= rhs.x;
		y -= rhs.y;
		z -= rhs.z;
		return this;
	}
	public Vector3f getProjectionOf(Vector3f rhs) {
		return this.times(dot(rhs) / dot(this));
	}
	public Vector3f getProjectionOnto(Vector3f rhs) {
		return rhs.times(rhs.dot(this) / rhs.dot(rhs));
	}
	public Vector3f getRejectionFrom(Vector3f rhs) {
		return this.minus(getProjectionOnto(rhs));
	}
	public Vector3f getRejectionOf(Vector3f rhs) {
		return rhs.minus(getProjectionOf(rhs));
	}
	public float getRejectionRatioFrom(Vector3f rhs) {
		return getRejectionFrom(rhs).getLength() / getProjectionOnto(rhs).getLength();
	}
	public float getRejectionRatioOf(Vector3f rhs) {
		return getRejectionOf(rhs).getLength() / getProjectionOf(rhs).getLength();
	}
}
