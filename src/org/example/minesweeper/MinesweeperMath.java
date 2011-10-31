package org.example.minesweeper;

public class MinesweeperMath {

	public static int mod(int a, int b) {
		if(a < 0)
			return a + b;
		else 
			return a % b;
	}

}
