package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import io.proffitt.coherence.math.Matrix4f;

public class Camera {
	private float		x, y, z;	// position
	private float		aX, aY, aZ; // euler angles
	private boolean		ortho, projGenerated, viewGenerated;
	private float		width, height, fov, near, far;
	private Matrix4f	projMat, viewMat;
	public Camera() {
		aX = 0;
		aY = 0;
		aZ = 0;
		x = 0;
		y = 0;
		z = 0;
		ortho = true;
		width = 1;
		height = 1;
		fov = 90;
		near = 0.01f;
		far = 100;
		projMat = new Matrix4f();
		projGenerated = true;
	}
	public Camera setWidth(float w){
		projGenerated = false;
		width = w;
		return this;
	}
	public Camera setHeight(float h){
		projGenerated = false;
		height = h;
		return this;
	}
	public Camera setPerspective(){
		projGenerated = false;
		ortho = false;
		return this;
	}
	public Camera setOrtho(){
		projGenerated = false;
		ortho = true;
		return this;
	}
	public Camera setFOV(float f){
		projGenerated = false;
		fov = f;
		return this;
	}
	public Camera setNearPlane(float n){
		projGenerated = false;
		near = n;
		return this;
	}
	public Camera setFarPlane(float f){
		projGenerated = false;
		far = f;
		return this;
	}
	private void generateProjection(){
		if (ortho){
			projMat = Matrix4f.getOrthographic(width, height);
		}else{
			projMat = Matrix4f.getPerspective(fov, width / height, near, far);
		}
		projGenerated = true;
	}
	private void generateView(){
		viewMat = Matrix4f.getRotationZ(-aZ).multiply(Matrix4f.getRotationX(-aX).multiply(Matrix4f.getRotationY(-aY).multiply(Matrix4f.getTranslation(-x, -y, -z))));
		viewGenerated = true;
	}
	public void bind() {
		glUniformMatrix4fv(4, false, getViewMatrix().toFloatBuffer());// view
		glUniformMatrix4fv(5, false, getProjectionMatrix().toFloatBuffer());// projection
	}
	public Matrix4f getViewMatrix() {
		if (!viewGenerated){
			generateView();
		}
		return viewMat;
	}
	public Matrix4f getProjectionMatrix(){
		if (!projGenerated){
			generateProjection();
		}
		return projMat;
	}
	public Camera setPos(float nx, float ny, float nz) {
		viewGenerated = false;
		x = nx;
		y = ny;
		z = nz;
		return this;
	}
	public Camera translate(float mx, float my, float mz) {
		viewGenerated = false;
		x += mx;
		y += my;
		z += mz;
		return this;
	}
	public Camera setRot(float nAX, float nAY, float nAZ) {
		viewGenerated = false;
		aX = nAX;
		aY = nAY;
		aZ = nAZ;
		if (aX < Math.PI / -2) {
			aX = (float) Math.PI / -2;
		} else if (aX > Math.PI / 2) {
			aX = (float) Math.PI / 2;
		}
		aY = aY % (float) (Math.PI * 2);
		return this;
	}
	public Camera rotate(float mAX, float mAY, float mAZ) {
		return this.setRot(aX + mAX, aY + mAY, aZ + mAZ);
	}
	public Camera move(float forward, float right) {
		return this.translate((float) ((forward * Math.sin(aY)) + (right * Math.cos(-aY))), 0, (float) ((forward * Math.cos(aY)) + (right * Math.sin(-aY))));
	}
	public float getFOV() {
		return fov;
	}
}
