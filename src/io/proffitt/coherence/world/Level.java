package io.proffitt.coherence.world;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.math.Vector3f;

public class Level {
	public static final int	CELL_SIZE	= 64;
	final int				w, h;
	private Cell[][]		cells;
	public Level(int x, int y) {
		w = x;
		h = y;
		cells = new Cell[w][h];
		for (int a = 0; a < w; a++) {
			for (int b = 0; b < h; b++) {
				cells[a][b] = new Cell(this, a, b, CELL_SIZE);
			}
		}
		for (int a = 0; a < w; a++) {
			for (int b = 0; b < h; b++) {
				Cell[][] adj = new Cell[3][3];
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						adj[i][j] = ((a + (i - 1) >= 0 && a + (i - 1) < w) && (b + (j - 1) >= 0 && b + (j - 1) < h)) ? cells[a + (i - 1)][b + (j - 1)] : null;
					}
				}
				cells[a][b].generateModel(adj);
			}
		}
	}
	public Entity getClosestEntity(Camera c, float hdif) {
		int cx = (int) (c.getX() / CELL_SIZE);
		int cy = (int) (c.getZ() / CELL_SIZE);
		Entity ret = null;
		float rDist = -1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cx + i - 1 > 0 && cx + i - 1 < w && cy + j - 1 > 0 && cy + j - 1 < h) {
					Entity candidate = cells[cx + i - 1][cy + j - 1].getClosestEntity(c, hdif);
					if (candidate != null) {
						float cDist = c.getPointAsViewed(candidate.getTransfrom().getPosition()).getProjectionOnto(new Vector3f(0, 0, -1)).getLength();
						if (cDist > 0 && (ret == null || cDist < rDist)) {
							ret = candidate;
							rDist = cDist;
						}
					}
				}
			}
		}
		return ret;
	}
	public void boundEntity(Entity e) {
		if (e.getTransfrom().getPosition().x < 0) {
			e.getTransfrom().getPosition().x = 0;
			if (e.getVelocity().x < 0) {
				e.getVelocity().x = 0;
			}
		}
		if (e.getTransfrom().getPosition().z < 0) {
			e.getTransfrom().getPosition().z = 0;
			if (e.getVelocity().z < 0) {
				e.getVelocity().z = 0;
			}
		}
		if (e.getTransfrom().getPosition().x >= w * CELL_SIZE) {
			e.getTransfrom().getPosition().x = w * CELL_SIZE - 0.01f;
			if (e.getVelocity().x > 0) {
				e.getVelocity().x = 0;
			}
		}
		if (e.getTransfrom().getPosition().z >= h * CELL_SIZE) {
			e.getTransfrom().getPosition().z = h * CELL_SIZE - 0.01f;
			if (e.getVelocity().z > 0) {
				e.getVelocity().z = 0;
			}
		}
	}
	public void addEntity(Entity e) {
		cells[(int) (e.getTransfrom().getPosition().x / CELL_SIZE)][(int) (e.getTransfrom().getPosition().z / CELL_SIZE)].addEntity(e);
	}
	public void update(double delta) {
		for (int a = 0; a < w; a++) {
			for (int b = 0; b < h; b++) {
				Cell[][] adj = new Cell[3][3];
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						adj[i][j] = ((a + (i - 1) >= 0 && a + (i - 1) < w) && (b + (j - 1) >= 0 && b + (j - 1) < h)) ? cells[a + (i - 1)][b + (j - 1)] : null;
					}
				}
				cells[a][b].update(delta, a, b, adj);//TODO: fix other 3 params
			}
		}
	}
	public void draw() {
		for (int a = 0; a < w; a++) {
			for (int b = 0; b < h; b++) {
				if (!cells[a][b].modelValid()) {
					Cell[][] adj = new Cell[3][3];
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							adj[i][j] = ((a + (i - 1) >= 0 && a + (i - 1) < w) && (b + (j - 1) >= 0 && b + (j - 1) < h)) ? cells[a + (i - 1)][b + (j - 1)] : null;
						}
					}
					cells[a][b].generateModel(adj);
				}
				cells[a][b].draw(a * CELL_SIZE, 0, b * CELL_SIZE);
			}
		}
	}
	public void drawContents() {
		for (int a = 0; a < w; a++) {
			for (int b = 0; b < h; b++) {
				cells[a][b].drawContents(a * CELL_SIZE, 0, b * CELL_SIZE);
			}
		}
	}
	public float getHeight(float xf, float zf) {
		int x = (int) (xf + 0.5f);
		int z = (int) (zf + 0.5f);
		return cells[x / CELL_SIZE][z / CELL_SIZE].getHeight(x % CELL_SIZE, z % CELL_SIZE);
	}
	public void setHeight(float xf, float zf, float h) {
		int x = (int) (xf + 0.5f);
		int z = (int) (zf + 0.5f);
		cells[x / CELL_SIZE][z / CELL_SIZE].setHeight(x % CELL_SIZE, z % CELL_SIZE, h);
	}
}
