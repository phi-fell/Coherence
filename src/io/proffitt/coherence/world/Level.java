package io.proffitt.coherence.world;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.math.Vector3f;

public class Level {
	public static final int	CELL_SIZE		= 64;
	public static final int	VIEW_DISTANCE	= 1;
	World					parentWorld;
	CellMatrix				cellz;
	public Level(World wo, int cx, int cy) {
		parentWorld = wo;
		cellz = new CellMatrix(this, VIEW_DISTANCE, VIEW_DISTANCE, cx, cy);
	}
	public void centerOn(Entity e) {
		cellz.centerOn(e.getCell().levelX, e.getCell().levelY);
	}
	public World getWorld() {
		return parentWorld;
	}
	public Entity getClosestEntity(Camera c, float hdif) {
		int cx = (int) (c.getX() / CELL_SIZE);
		int cy = (int) (c.getZ() / CELL_SIZE);
		Entity ret = null;
		float rDist = -1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cellz.getCell(cx + i - 1, cy + j - 1) != null) {
					Entity candidate = cellz.getCell(cx + i - 1, cy + j - 1).getClosestEntity(c, hdif);
					if (candidate != null) {
						float cDist = c.getPointAsViewed(candidate.getTransfrom().getPosition()).getProjectionOnto(new Vector3f(0, 0, -1)).getLength();
						if (ret == null || cDist < rDist) {
							ret = candidate;
							rDist = cDist;
						}
					}
				}
			}
		}
		return ret;
	}
	public void addEntity(Entity e) {
		cellz.getCell((int) (e.getTransfrom().getPosition().x / CELL_SIZE), (int) (e.getTransfrom().getPosition().z / CELL_SIZE)).addEntity(e);
	}
	public void removeEntity(Entity e) {
		cellz.removeEntity(e);
	}
	public void update(double delta) {
		cellz.update(delta);
	}
	public void draw() {
		cellz.draw();
	}
	public void drawContents() {
		cellz.drawContents();
	}
	public float getHeight(float xf, float zf) {
		return cellz.getHeight(xf, zf);
	}
	public void setHeight(float xf, float zf, float h) {
		cellz.setHeight(xf, zf, h);
	}
}
