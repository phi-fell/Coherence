package io.proffitt.coherence.math;

public class Vector4f {
	public float x, y, z, w; // 3d vector in homogeneous coordinates
	public Vector4f() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
	public Vector4f(float a, float b, float c, float d) {
		x = a;
		y = b;
		z = c;
		w = d;
	}
	public void divideByW() {
		if (w == 0) {
			return;
		}
		x /= w;
		y /= w;
		z /= w;
		w = 1;
	}
	public Vector4f times(float s) {
		return new Vector4f(x * s, y * s, z * s, w * s);
	}
	public Vector4f plus(Vector4f rhs) {
		return new Vector4f(x + rhs.x, y + rhs.y, z + rhs.z, w + rhs.w);
	}
	public Vector4f minus(Vector4f rhs) {
		return this.plus(rhs.times(-1));
	}
	public Vector4f cross(Vector4f rhs) {
		return new Vector4f((y * rhs.z) - (z * rhs.y), (z * rhs.x) - (x * rhs.z), (x * rhs.y) - (y * rhs.x), w * rhs.w);
	}
}
