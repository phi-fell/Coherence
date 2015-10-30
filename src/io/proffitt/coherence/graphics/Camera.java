package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import io.proffitt.coherence.math.Matrix4f;

public class Camera {
	private float x, y, z; // position
	private float aX, aY, aZ; // euler angles
	private Matrix4f projMat;
	public Camera() {
		aX = 0;
		aY = 0;
		aZ = 0;
		x = 0;
		y = 0;
		z = 0;
		projMat = new Matrix4f();
	}
	public void setProjection(Matrix4f projection){
		projMat = projection;
	}
	public void bind(){
		glUniformMatrix4fv(4, false, getViewMatrix().toFloatBuffer());// view
		glUniformMatrix4fv(5, false, projMat.toFloatBuffer());// projection
	}
	public Matrix4f getViewMatrix() {
		return Matrix4f.getRotationZ(-aZ).multiply(Matrix4f.getRotationX(-aX).multiply(Matrix4f.getRotationY(-aY).multiply(Matrix4f.getTranslation(-x, -y, -z))));
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
		if (aX < Math.PI / -2) {
			aX = (float) Math.PI / -2;
		} else if (aX > Math.PI / 2) {
			aX = (float) Math.PI / 2;
		}
		aY = aY % (float)(Math.PI * 2);
	}
	public void rotate(float mAX, float mAY, float mAZ) {
		setRot(aX + mAX, aY + mAY, aZ + mAZ);
	}
	public void move(float forward, float right) {
		this.translate((float) ((forward * Math.sin(aY)) + (right * Math.cos(-aY))), 0, (float) ((forward * Math.cos(aY)) + (right * Math.sin(-aY))));
	}
}
