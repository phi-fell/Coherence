package io.proffitt.coherence.world;

import io.proffitt.coherence.graphics.Camera;

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
				cells[a][b] = new Cell(CELL_SIZE);
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
	public void addEntity(Entity e) {
		cells[(int) (e.getTransfrom().getPosition().x / CELL_SIZE)][(int) (e.getTransfrom().getPosition().y / CELL_SIZE)].addEntity(e);
		;
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
	public void draw(Camera cam) {
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
				cells[a][b].draw(a * CELL_SIZE, 0, b * CELL_SIZE, cam);
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
