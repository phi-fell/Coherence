package io.proffitt.coherence.world;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.util.ArrayList;

import io.proffitt.coherence.graphics.Model;
import io.proffitt.coherence.math.Matrix4f;
import io.proffitt.coherence.math.Vector4f;

public class Cell {
	public final int	SIZE;
	float[][]			height;
	Model				model;
	Cell[][]			adj;
	ArrayList<Entity>	entities;
	public Cell(int size) {
		SIZE = size;
		height = new float[SIZE][SIZE];
		entities = new ArrayList<Entity>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				// height[i][j] = (float) ((((i - (SIZE / 2)) * (i - (SIZE /
				// 2))) + ((j - (SIZE / 2)) * (j - (SIZE / 2)))) / ((SIZE *
				// SIZE) / 16.0) - 8);
				height[i][j] = -1.5f;
				height[i][j] += (Math.random() - 0.5f) * 0.2;
			}
		}
		model = null;
	}
	public void addEntity(Entity e) {
		entities.add(e);
	}
	public void setAdjacent(Cell[][] a) {
		adj = a;
	}
	public void setHeight(int x, int z, float h) {
		height[x][z] = h;
		if (model != null) {
			model.destroy();
		}
		model = null;
	}
	public float getHeight(int x, int z) {
		return height[x][z];
	}
	public void generateModel() {
		if (model != null) {
			model.destroy();
		}
		model = new Model(getVerts(adj));
	}
	public void draw(float x, float y, float z) {
		if (model == null) {
			generateModel();
		}
		glUniformMatrix4fv(3, false, Matrix4f.getTranslation(x, y, z).toFloatBuffer());// model
		model.render();
	}
	private float getStitchingHeight(int x, int y, Cell[][] adj) {
		int cx = 1;
		int cy = 1;
		if (x < 0) {
			x += SIZE;
			cx--;
		} else if (x >= SIZE) {
			x -= SIZE;
			cx++;
		}
		if (y < 0) {
			y += SIZE;
			cy--;
		} else if (y >= SIZE) {
			y -= SIZE;
			cy++;
		}
		if (adj[cx][cy] == null) {
			x += (cx - 1) * SIZE;
			y += (cy - 1) * SIZE;
			x = x < 0 ? 0 : x;
			x = x < SIZE ? x : SIZE - 1;
			y = y < 0 ? 0 : y;
			y = y < SIZE ? y : SIZE - 1;
			return height[x][y];
		}
		return adj[cx][cy].height[x][y];
	}
	private Vector4f pos(int x, int y, Cell[][] adj) {
		return new Vector4f(x, getStitchingHeight(x, y, adj), y, 1);
	}
	private float[] getVerts(Cell[][] adj) {
		float[] verts = new float[(SIZE) * (SIZE) * 36];
		Vector4f[][] norm = new Vector4f[SIZE + 1][SIZE + 1];
		for (int i = 0; i <= SIZE; i++) {
			for (int j = 0; j <= SIZE; j++) {
				norm[i][j] = new Vector4f(0, 0, 0, 0);
				norm[i][j] = norm[i][j].plus(pos(i, j, adj).minus(pos(i, j - 1, adj)).cross(pos(i, j, adj).minus(pos(i - 1, j, adj))));
				norm[i][j] = norm[i][j].plus(pos(i, j, adj).minus(pos(i + 1, j, adj)).cross(pos(i, j, adj).minus(pos(i, j - 1, adj))));
				norm[i][j] = norm[i][j].plus(pos(i, j, adj).minus(pos(i - 1, j, adj)).cross(pos(i, j, adj).minus(pos(i, j + 1, adj))));
				norm[i][j] = norm[i][j].plus(pos(i, j, adj).minus(pos(i, j + 1, adj)).cross(pos(i, j, adj).minus(pos(i + 1, j, adj))));
				norm[i][j].w = 0;
				norm[i][j].normalize();
			}
		}
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				verts[(i * (SIZE) * 36) + (j * 36) + 0] = pos(i, j + 1, adj).x;
				verts[(i * (SIZE) * 36) + j * 36 + 1] = pos(i, j + 1, adj).y;
				verts[(i * (SIZE) * 36) + j * 36 + 2] = pos(i, j + 1, adj).z;
				verts[(i * (SIZE) * 36) + j * 36 + 3] = norm[i][j + 1].x;
				verts[(i * (SIZE) * 36) + j * 36 + 4] = norm[i][j + 1].y;
				verts[(i * (SIZE) * 36) + j * 36 + 5] = norm[i][j + 1].z;
				verts[(i * (SIZE) * 36) + j * 36 + 6] = pos(i, j, adj).x;
				verts[(i * (SIZE) * 36) + j * 36 + 7] = pos(i, j, adj).y;
				verts[(i * (SIZE) * 36) + j * 36 + 8] = pos(i, j, adj).z;
				verts[(i * (SIZE) * 36) + j * 36 + 9] = norm[i][j].x;
				verts[(i * (SIZE) * 36) + j * 36 + 10] = norm[i][j].y;
				verts[(i * (SIZE) * 36) + j * 36 + 11] = norm[i][j].z;
				verts[(i * (SIZE) * 36) + j * 36 + 12] = pos(i + 1, j, adj).x;
				verts[(i * (SIZE) * 36) + j * 36 + 13] = pos(i + 1, j, adj).y;
				verts[(i * (SIZE) * 36) + j * 36 + 14] = pos(i + 1, j, adj).z;
				verts[(i * (SIZE) * 36) + j * 36 + 15] = norm[i + 1][j].x;
				verts[(i * (SIZE) * 36) + j * 36 + 16] = norm[i + 1][j].y;
				verts[(i * (SIZE) * 36) + j * 36 + 17] = norm[i + 1][j].z;
				verts[(i * (SIZE) * 36) + j * 36 + 18] = pos(i, j + 1, adj).x;
				verts[(i * (SIZE) * 36) + j * 36 + 19] = pos(i, j + 1, adj).y;
				verts[(i * (SIZE) * 36) + j * 36 + 20] = pos(i, j + 1, adj).z;
				verts[(i * (SIZE) * 36) + j * 36 + 21] = norm[i][j + 1].x;
				verts[(i * (SIZE) * 36) + j * 36 + 22] = norm[i][j + 1].y;
				verts[(i * (SIZE) * 36) + j * 36 + 23] = norm[i][j + 1].z;
				verts[(i * (SIZE) * 36) + j * 36 + 24] = pos(i + 1, j, adj).x;
				verts[(i * (SIZE) * 36) + j * 36 + 25] = pos(i + 1, j, adj).y;
				verts[(i * (SIZE) * 36) + j * 36 + 26] = pos(i + 1, j, adj).z;
				verts[(i * (SIZE) * 36) + j * 36 + 27] = norm[i + 1][j].x;
				verts[(i * (SIZE) * 36) + j * 36 + 28] = norm[i + 1][j].y;
				verts[(i * (SIZE) * 36) + j * 36 + 29] = norm[i + 1][j].z;
				verts[(i * (SIZE) * 36) + j * 36 + 30] = pos(i + 1, j + 1, adj).x;
				verts[(i * (SIZE) * 36) + j * 36 + 31] = pos(i + 1, j + 1, adj).y;
				verts[(i * (SIZE) * 36) + j * 36 + 32] = pos(i + 1, j + 1, adj).z;
				verts[(i * (SIZE) * 36) + j * 36 + 33] = norm[i + 1][j + 1].x;
				verts[(i * (SIZE) * 36) + j * 36 + 34] = norm[i + 1][j + 1].y;
				verts[(i * (SIZE) * 36) + j * 36 + 35] = norm[i + 1][j + 1].z;
			}
		}
		return verts;
	}
}
