package io.proffitt.coherence.resource;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import io.proffitt.coherence.math.Matrix4f;

public class Shader {
	int	vertexID;
	int	fragmentID;
	int	programID;
	public Shader(String vert, String frag) {
		programID = glCreateProgram();
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vert);
		glCompileShader(vertexID);
		if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Error in vertex shader\n" + glGetShaderInfoLog(vertexID, glGetShaderi(vertexID, GL_INFO_LOG_LENGTH)));
		}
		glAttachShader(programID, vertexID);
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, frag);
		glCompileShader(fragmentID);
		if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Error in fragment shader\n" + glGetShaderInfoLog(fragmentID, glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH)));
		}
		glAttachShader(programID, fragmentID);
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException("Error linking shader.");
		}
	}
	public void bind() {
		glUseProgram(programID);
	}
	public void unbind() {
		glUseProgram(0);
	}
	public int getUniformLocation(String s) {
		return glGetUniformLocation(programID, s);
	}
	public static void setUniformMat4(int loc, Matrix4f mat) {
		glUniformMatrix4fv(loc, false, mat.toFloatBuffer());
	}
	public static void setModelMat(Matrix4f mat) {
		setUniformMat4(3, mat);
	}
	public static void setEntityMat(Matrix4f mat) {
		setUniformMat4(4, mat);
	}
	public static void setViewMat(Matrix4f mat) {
		setUniformMat4(5, mat);
	}
	public static void setProjectionMat(Matrix4f mat) {
		setUniformMat4(6, mat);
	}
	public static void setUVMat(Matrix4f mat) {
		setUniformMat4(7, mat);
	}
	public void setUniformInt(String s, int i) {
		glUniform1i(getUniformLocation(s), i);
	}
	public void setUniformFloat(String s, float f) {
		glUniform1f(getUniformLocation(s), f);
	}
	public void setUniformFloat(String s, float f0, float f1) {
		glUniform2f(getUniformLocation(s), f0, f1);
	}
	public void setUniformFloat(String s, float f0, float f1, float f2) {
		glUniform3f(getUniformLocation(s), f0, f1, f2);
	}
	public void setUniformFloat(String s, float f0, float f1, float f2, float f3) {
		glUniform4f(getUniformLocation(s), f0, f1, f2, f3);
	}
	public void destroy() {
		unbind();
		glDetachShader(programID, vertexID);
		glDetachShader(programID, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		glDeleteProgram(programID);
	}
}
