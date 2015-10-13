package io.proffitt.coherence.world;

public class Level {
	public static final int	CELL_SIZE	= 64;
	final int				w, h;
	private Cell[][]		cells;
	public Level(int x, int y) {
		w = x;
		h = y;
		cells = new Cell[x][y];
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
				cells[a][b] = new Cell(CELL_SIZE);
			}
		}
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
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
	public void draw() {
		for (int a = 0; a < w; a++) {
			for (int b = 0; b < h; b++) {
				cells[a][b].draw(a * CELL_SIZE, 0, b * CELL_SIZE);
			}
		}
	}
}
