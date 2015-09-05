package io.proffitt.coherence.graphics;

import io.proffitt.coherence.math.Matrix4f;

public class Camera {
	private float	x, y, z;	//position
	private float	aX, aY, aZ; //euler angles
	public Camera() {
		aX = 0;
		aY = 0;
		aZ = 0;
		x = 0;
		y = 0;
		z = 0;
	}
	public Matrix4f getViewMatrix() {
		return Matrix4f.getRotationZ(-aZ).multiply(Matrix4f.getRotationY(-aY).multiply(Matrix4f.getRotationX(-aX).multiply(Matrix4f.getTranslation(x, y, z))));
	}
	public void setPos(float nx, float ny, float nz) {
		x = nx;
		y = ny;
		z = nz;
	}
	public void translate(float mx, float my, float mz) {
		x += mx;
		y += my;
		z += mz;
	}
	public void setRot(float nAX, float nAY, float nAZ) {
		aX = nAX;
		aY = nAY;
		aZ = nAZ;
	}
	public void rotate(float mAX, float mAY, float mAZ) {
		aX += mAX;
		aY += mAY;
		aZ += mAZ;
	}
}
