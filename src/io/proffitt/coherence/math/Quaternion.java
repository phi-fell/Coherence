package io.proffitt.coherence.math;

public class Quaternion {
	//NOTE: some functions assume that the quaternion is normalized
	//So don't use non-normalized quaternions except as temporary intermediates.
	float w, x, y, z;
	public Quaternion() {
		w = 1;
		x = 0;
		y = 0;
		z = 0;
	}
	public Quaternion(float W, float X, float Y, float Z) {
		w = W;
		x = X;
		y = Y;
		z = Z;
	}
	public Quaternion productWith(Quaternion q) {
		return new Quaternion((w * q.w) - (x * q.x) - (y * q.y) - (z * q.z), (w * q.x) + (x * q.w) + (y * q.z) - (z * q.y), (w * q.y) - (x * q.z) + (y * q.w) + (z * q.x),
				(w * q.z) + (x * q.y) - (y * q.x) + (z * q.w));
	}
	public void multiplyBy(Quaternion q) {
		w = (w * q.w) - (x * q.x) - (y * q.y) - (z * q.z);
		x = (w * q.x) + (x * q.w) + (y * q.z) - (z * q.y);
		y = (w * q.y) - (x * q.z) + (y * q.w) + (z * q.x);
		z = (w * q.z) + (x * q.y) - (y * q.x) + (z * q.w);
	}
	public float SqMag() {
		return (w * w) + (x * x) + (y * y) + (z * z);
	}
	public float Mag() {
		return (float) Math.sqrt(SqMag());
	}
	public void normalize() {
		float m = Mag();
		w /= m;
		x /= m;
		y /= m;
		z /= m;
	}
	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}
}
