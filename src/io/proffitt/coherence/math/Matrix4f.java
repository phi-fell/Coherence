package io.proffitt.coherence.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix4f {
	private float[][] m = new float[4][4];
	public boolean equals(Matrix4f rhs) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (m[i][j] != rhs.m[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	public Matrix4f() {
		this(1);
	}
	public Matrix4f(float f) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == j) {
					m[i][j] = f;
				} else {
					m[i][j] = 0;
				}
			}
		}
	}
	public Matrix4f(float[][] in) {
		if (in.length != 4 || in[0].length != 4) {
			throw new RuntimeException("Array cannot be mat4x4");
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m[i][j] = in[i][j];
			}
		}
	}
	public Matrix4f multiply(Matrix4f rhs) {
		Matrix4f ret = new Matrix4f(0);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				ret.m[i][j] = (m[0][j] * rhs.m[i][0]) + (m[1][j] * rhs.m[i][1]) + (m[2][j] * rhs.m[i][2]) + (m[3][j] * rhs.m[i][3]);
			}
		}
		return ret;
	}
	public String toString() {
		String ret = "";
		for (int i = 0; i < 4; i++) {
			ret += "[ ";
			for (int j = 0; j < 4; j++) {
				ret = ret + m[j][i] + " ";
			}
			ret = ret + "]\n";
		}
		return ret;
	}
	public float[] toFloatArray() {
		float[] ret = new float[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				ret[(i * 4) + j] = m[i][j];
			}
		}
		return ret;
	}
	public FloatBuffer toFloatBuffer() {
		FloatBuffer ret = BufferUtils.createFloatBuffer(16);
		ret.put(this.toFloatArray()).flip();
		return ret;
	}
	public static Matrix4f getTranslation(float x, float y, float z) {
		Matrix4f mat = new Matrix4f(1);
		mat.m[3][0] = x;
		mat.m[3][1] = y;
		mat.m[3][2] = z;
		return mat;
	}
	public static Matrix4f getScale(float x, float y, float z) {
		Matrix4f mat = new Matrix4f(1);
		mat.m[0][0] = x;
		mat.m[1][1] = y;
		mat.m[2][2] = z;
		return mat;
	}
	public static Matrix4f getRotationX(float r) {
		Matrix4f mat = new Matrix4f(1);
		mat.m[1][1] = (float) Math.cos(r);
		mat.m[2][2] = (float) Math.cos(r);
		mat.m[1][2] = (float) Math.sin(r);
		mat.m[2][1] = -(float) Math.sin(r);
		return mat;
	}
	public static Matrix4f getRotationY(float r) {
		Matrix4f mat = new Matrix4f(1);
		mat.m[0][0] = (float) Math.cos(r);
		mat.m[2][2] = (float) Math.cos(r);
		mat.m[0][2] = -(float) Math.sin(r);
		mat.m[2][0] = (float) Math.sin(r);
		return mat;
	}
	public static Matrix4f getRotationZ(float r) {
		Matrix4f mat = new Matrix4f(1);
		mat.m[0][0] = (float) Math.cos(r);
		mat.m[1][1] = (float) Math.cos(r);
		mat.m[0][1] = (float) Math.sin(r);
		mat.m[1][0] = -(float) Math.sin(r);
		return mat;
	}
	public static Matrix4f getOrthographic(float width, float height) {
		Matrix4f mat = new Matrix4f(1);
		mat.m[0][0] = 2 / width;
		mat.m[1][1] = 2 / height;
		return mat;
	}
	public static Matrix4f getPerspective(float fov, float aspect, float near, float far) {
		// NOTE: fov is in radians!
		Matrix4f mat = new Matrix4f(0);
		mat.m[0][0] = 1 / (float) Math.tan(fov / 2);
		mat.m[1][1] = aspect / (float) Math.tan(fov / 2);
		mat.m[2][2] = (far + near) / (near - far);
		mat.m[3][2] = (2 * far * near) / (near - far);
		mat.m[2][3] = -1;
		return mat;
	}
}
