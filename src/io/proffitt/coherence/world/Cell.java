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
		for (int i = 0; i < SIZE - 1; i++) {
			for (int j = 0; j < SIZE - 1; j++) {
				// vertices
				Vector4f tl = new Vector4f(i - (SIZE / 2), height[i][j], j - (SIZE / 2), 1);
				Vector4f tr = new Vector4f((i + 1) - (SIZE / 2), height[i + 1][j], j - (SIZE / 2), 1);
				Vector4f bl = new Vector4f(i - (SIZE / 2), height[i][j + 1], (j + 1) - (SIZE / 2), 1);
				Vector4f br = new Vector4f((i + 1) - (SIZE / 2), height[i + 1][j + 1], (j + 1) - (SIZE / 2), 1);
				// normals
				Vector4f tln = tr.minus(tl).cross(tl.minus(bl)); // TODO: make surface normals smooth rather than faceted.
				Vector4f trn = br.minus(tr).cross(tr.minus(tl));
				Vector4f bln = tl.minus(bl).cross(bl.minus(br));
				Vector4f brn = bl.minus(br).cross(br.minus(tr));
				verts[(i * (SIZE - 1) * 36) + (j * 36) + 0] = bl.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 1] = bl.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 2] = bl.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 3] = bln.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 4] = bln.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 5] = bln.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 6] = tl.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 7] = tl.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 8] = tl.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 9] = tln.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 10] = tln.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 11] = tln.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 12] = tr.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 13] = tr.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 14] = tr.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 15] = trn.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 16] = trn.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 17] = trn.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 18] = bl.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 19] = bl.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 20] = bl.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 21] = bln.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 22] = bln.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 23] = bln.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 24] = tr.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 25] = tr.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 26] = tr.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 27] = trn.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 28] = trn.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 29] = trn.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 30] = br.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 31] = br.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 32] = br.z;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 33] = brn.x;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 34] = brn.y;
				verts[(i * (SIZE - 1) * 36) + j * 36 + 35] = brn.z;
			}
		}
		return verts;
	}
}
