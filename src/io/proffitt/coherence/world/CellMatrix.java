package io.proffitt.coherence.world;

import io.proffitt.coherence.resource.ResourceHandler;

public class CellMatrix {
	Cell[][]	cells;
	Level		level;
	int			width, height;
	int			rx, ry;
	int			cx, cy;
	public CellMatrix(Level l, int radiusX, int radiusY, int centerX, int centerY) {
		level = l;
		rx = radiusX;
		ry = radiusY;
		cx = centerX;
		cy = centerY;
		width = (rx * 2) + 1;
		height = (ry * 2) + 1;
		cells = new Cell[width][height];
		for (int i = -rx; i <= rx; i++) {
			for (int j = -ry; j <= ry; j++) {
				cells[i + rx][j + ry] = ResourceHandler.get().loadCell(level, cx + i, cy + j);
			}
		}
	}
	public float getHeight(float xf, float zf) {
		int x = xf < 0 ? (int) (xf - 0.5) : (int) (xf + 0.5);
		int z = zf < 0 ? (int) (zf - 0.5) : (int) (zf + 0.5);
		int cx = (x / Level.CELL_SIZE) + (x < 0 ? -1 : 0);
		int cz = (z / Level.CELL_SIZE) + (z < 0 ? -1 : 0);
		int vx = (x % Level.CELL_SIZE) + (x < 0 && (x % Level.CELL_SIZE != 0) ? Level.CELL_SIZE : 0);
		int vz = (z % Level.CELL_SIZE) + (z < 0 && (z % Level.CELL_SIZE != 0) ? Level.CELL_SIZE : 0);
		return getCell(cx, cz).getHeight(vx, vz);
	}
	public void setHeight(float xf, float zf, float h) {
		int x = xf < 0 ? (int) (xf - 0.5) : (int) (xf + 0.5);
		int z = zf < 0 ? (int) (zf - 0.5) : (int) (zf + 0.5);
		int cx = (x / Level.CELL_SIZE) + (x < 0 ? -1 : 0);
		int cz = (z / Level.CELL_SIZE) + (z < 0 ? -1 : 0);
		int vx = (x % Level.CELL_SIZE) + (x < 0 && (x % Level.CELL_SIZE != 0) ? Level.CELL_SIZE : 0);
		int vz = (z % Level.CELL_SIZE) + (z < 0 && (z % Level.CELL_SIZE != 0) ? Level.CELL_SIZE : 0);
		System.out.println(xf + ", " + zf);
		getCell(cx, cz).setHeight(vx, vz, h);
	}
	public void drawContents() {
		for (int a = 0; a < width; a++) {
			for (int b = 0; b < height; b++) {
				cells[a][b].drawContents();
			}
		}
	}
	public void draw() {
		for (int a = 0; a < width; a++) {
			for (int b = 0; b < height; b++) {
				cells[a][b].draw();
			}
		}
	}
	public void update(double delta) {
		for (int a = 0; a < width; a++) {
			for (int b = 0; b < height; b++) {
				cells[a][b].update(delta);
			}
		}
	}
	public void removeEntity(Entity e) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].removeEntity(e);
			}
		}
	}
	public Cell getCell(int x, int y) {
		int relX = (x - cx) + rx;
		int relY = (y - cy) + ry;
		if (relX >= 0 && relX < width && relY >= 0 && relY < height) {
			return cells[relX][relY];
		} else {
			//Cell not loaded!
			return null;
		}
	}
	public Cell[][] getAdjacent(Cell c) {
		Cell[][] adj = new Cell[3][3];
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				adj[i + 1][j + 1] = getCell(c.levelX + i, c.levelY + j);
			}
		}
		return adj;
	}
	public void centerOn(int x, int y) {
		if (cx == x && cy == y) {
			//do nothing, obviously
		} else if (Math.abs(cx - x) <= 1 && Math.abs(cy - y) <= 1) {
			if (x > cx) {
				//shift left
				cx++;
				for (int i = -rx; i <= rx; i++) {
					for (int j = -ry; j <= ry; j++) {
						if (i == -rx) {
							ResourceHandler.saveCell(cells[i + rx][j + ry]);
						}
						if (i == rx) {
							cells[i + rx][j + ry] = ResourceHandler.get().loadCell(level, cx + i, cy + j);
						} else {
							cells[i + rx][j + ry] = cells[i + rx + 1][j + ry];
						}
					}
				}
				for (int j = 0; j < width; j++) {
					cells[width - 2][j].generateModel();
				}
			} else if (x < cx) {
				//shift right
				cx--;
				for (int i = rx; i >= -rx; i--) {
					for (int j = -ry; j <= ry; j++) {
						if (i == rx) {
							ResourceHandler.saveCell(cells[i + rx][j + ry]);
						}
						if (i == -rx) {
							cells[i + rx][j + ry] = ResourceHandler.get().loadCell(level, cx + i, cy + j);
						} else {
							cells[i + rx][j + ry] = cells[i + rx - 1][j + ry];
						}
					}
				}
			}
			if (y > cy) {
				//shift down
				cy++;
				for (int i = -rx; i <= rx; i++) {
					for (int j = -ry; j <= ry; j++) {
						if (j == -ry) {
							ResourceHandler.saveCell(cells[i + rx][j + ry]);
						}
						if (j == ry) {
							cells[i + rx][j + ry] = ResourceHandler.get().loadCell(level, cx + i, cy + j);
						} else {
							cells[i + rx][j + ry] = cells[i + rx][j + ry + 1];
						}
					}
				}
				for (int i = 0; i < width; i++) {
					cells[i][height - 2].generateModel();
				}
			} else if (y < cy) {
				//shift up
				cy--;
				for (int i = -rx; i <= rx; i++) {
					for (int j = ry; j >= -ry; j--) {
						if (j == ry) {
							ResourceHandler.saveCell(cells[i + rx][j + ry]);
						}
						if (j == -ry) {
							cells[i + rx][j + ry] = ResourceHandler.get().loadCell(level, cx + i, cy + j);
						} else {
							cells[i + rx][j + ry] = cells[i + rx][j + ry - 1];
						}
					}
				}
			}
		} else {
			//TODO: ***IMPORANT*** remove player from level set and add after or do something, otherwise this will remove the player's cell from
			//******************** the level, and either save them to file or do who knows what and hang/crash the game or do who knows what
			//******************** (currently it just makes movement impossible, since player.update can never be called)
			for (int i = -rx; i <= rx; i++) {
				for (int j = -ry; j <= ry; j++) {
					ResourceHandler.saveCell(cells[i + rx][j + ry]);
				}
			}
			cx = x;
			cy = y;
			for (int i = -rx; i <= rx; i++) {
				for (int j = -ry; j <= ry; j++) {
					cells[i + rx][j + ry] = ResourceHandler.get().loadCell(level, cx + i, cy + j);
				}
			}
		}
	}
}