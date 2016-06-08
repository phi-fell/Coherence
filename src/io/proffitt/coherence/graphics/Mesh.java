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
import io.proffitt.coherence.math.AABB;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.resource.Texture;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Mesh {
	int				VAO;
	int				VBO;
	int				vertnum;
	private float[]	verts;
	private AABB	aabb;
	private boolean	uv;
	public Mesh(float[] v, boolean doUV, boolean autoNormalize) {
		uv = doUV;
		verts = v;
		aabb = new AABB(verts, uv ? 8 : 6, autoNormalize);
		vertnum = verts.length / (uv ? 8 : 6);
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verts.length);
		verticesBuffer.put(verts).flip();
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, uv ? 32 : 24, 0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, uv ? 32 : 24, 12);
		if (uv) {
			glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
		}
		glBindVertexArray(0);
	}
	public AABB getAABB() {
		return aabb;
	}
	public void destroy() {
		glBindVertexArray(0);
		glDeleteVertexArrays(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(VBO);
		VAO = 0;
		VBO = 0;
		vertnum = 0;
	}
	public void render() {
		if (Texture.pennyMode) {
			ResourceHandler.get().getTexture("penny").bind();
		}
		glBindVertexArray(VAO);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		if (uv) {
			glEnableVertexAttribArray(2);
		}
		glDrawArrays(GL_TRIANGLES, 0, vertnum);
		if (uv) {
			glDisableVertexAttribArray(2);
		}
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}
