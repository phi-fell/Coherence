package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Model {
	int	VAO;
	int	VBO;
	int	vertnum;
	public Model(float[] verts) {
		vertnum = verts.length / 3;
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verts.length);
		verticesBuffer.put(verts).flip();
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindVertexArray(0);
	}
	public void destroy() {
		glBindVertexArray(0);
		glDeleteVertexArrays(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(VBO);
	}
	public void render() {
		glBindVertexArray(VAO);
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, vertnum);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}
