package io.proffitt.coherence.math;

public class Vector4f {
	float	x, y, z, w; // 3d vector in homogeneous coordinates
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
	public Vector4f cross(Vector4f rhs) {
		return new Vector4f();
	}
}
