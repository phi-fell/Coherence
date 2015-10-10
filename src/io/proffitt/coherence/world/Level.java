package io.proffitt.coherence.world;

public class Level {
	public static final int CELL_SIZE = 64;
	final int w,h;
	private Cell[][] cells;
	public Level(int x, int y){
		w=x;
		h=y;
		cells = new Cell[x][y];
		for (int a = 0; a < x; a++){
			for (int b = 0; b < y; b++){
				cells[a][b] = new Cell(CELL_SIZE);
			}
		}
	}
	public void draw(){
		for (int a = 0; a < w; a++){
			for (int b = 0; b < h; b++){
				cells[a][b].draw(a * CELL_SIZE, 0, b * CELL_SIZE);;
			}
		}
	}
}
