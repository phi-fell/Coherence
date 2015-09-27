package io.proffitt.coherence.world;

import io.proffitt.coherence.math.Vector4f;

public class Cell {
	float[][]				height;
	public static final int	SIZE	= 64;
	public Cell() {
		height = new float[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				height[i][j] = (float) ((((i - (SIZE / 2)) * (i - (SIZE / 2))) + ((j - (SIZE / 2)) * (j - (SIZE / 2)))) / ((SIZE * SIZE) / 16.0) - 8);
			}
		}
	}
	public float[] getVerts() {
		float[] verts = new float[(SIZE - 1) * (SIZE - 1) * 36];
		Vector4f[][] pos = new Vector4f[SIZE][SIZE];
		Vector4f[][] norm = new Vector4f[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				pos[i][j] = new Vector4f(i - (SIZE / 2), height[i][j], j - (SIZE / 2), 1);
			}
		}
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				norm[i][j] = new Vector4f(0, 0, 0, 0);
				if (i > 0 && j > 0) {
					norm[i][j] = norm[i][j].plus(pos[i][j].minus(pos[i][j - 1]).cross(pos[i][j].minus(pos[i - 1][j])));
				}
				if (i < SIZE - 1 && j > 0) {
					norm[i][j] = norm[i][j].plus(pos[i][j].minus(pos[i + 1][j]).cross(pos[i][j].minus(pos[i][j-1])));
				}
				if (i > 0 && j < SIZE - 1) {
					norm[i][j] = norm[i][j].plus(pos[i][j].minus(pos[i-1][j]).cross(pos[i][j].minus(pos[i][j + 1])));
				}
				if (i < SIZE - 1 && j < SIZE - 1) {
					norm[i][j] = norm[i][j].plus(pos[i][j].minus(pos[i][j+1]).cross(pos[i][j].minus(pos[i+1][j])));
				}
				norm[i][j].w = 0;
				norm[i][j].normalize();
			}
		}
		for (int i = 0; i < SIZE - 1; i++) {
			for (int j = 0; j < SIZE - 1; j++) {
				verts[(i * (SIZE - 1) * 36) + (j * 36) + 0] = pos[i][j+1].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 1] = pos[i][j+1].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 2] = pos[i][j+1].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 3] = norm[i][j+1].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 4] = norm[i][j+1].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 5] = norm[i][j+1].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 6] = pos[i][j].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 7] = pos[i][j].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 8] = pos[i][j].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 9] = norm[i][j].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 10] = norm[i][j].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 11] = norm[i][j].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 12] = pos[i+1][j].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 13] = pos[i+1][j].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 14] = pos[i+1][j].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 15] = norm[i+1][j].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 16] = norm[i+1][j].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 17] = norm[i+1][j].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 18] = pos[i][j+1].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 19] = pos[i][j+1].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 20] = pos[i][j+1].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 21] = norm[i][j+1].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 22] = norm[i][j+1].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 23] = norm[i][j+1].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 24] = pos[i+1][j].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 25] = pos[i+1][j].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 26] = pos[i+1][j].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 27] = norm[i+1][j].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 28] = norm[i+1][j].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 29] = norm[i+1][j].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 30] = pos[i+1][j+1].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 31] = pos[i+1][j+1].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 32] = pos[i+1][j+1].z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 33] = norm[i+1][j+1].x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 34] = norm[i+1][j+1].y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 35] = norm[i+1][j+1].z;
			}
		}
		return verts;
	}
}
