package io.proffitt.coherence.world;

public class Level {
	private Cell[][] cells;
	public Level(int x, int y){
		cells = new Cell[x][y];
		for (int a = 0; a < x; a++){
			for (int b = 0; b < y; b++){
				cells[a][b] = new Cell();
			}
		}
	}
}
