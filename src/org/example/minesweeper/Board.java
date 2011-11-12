package org.example.minesweeper;

import java.util.Random;

import org.example.minesweeper.Cell.CellStates;

public class Board {
	private Cell[][] board;
	private int rowN, colN;
	int mines;
	private int pushedCells;
	private boolean finished;

	public Board(int difficult) {
		mines = 0;
		// Build the matrix of Cells which depends on difficult
		switch (difficult) {
		case (0):
			rowN = 6;
			colN = 6;
			mines = 5;
			break;
		case (1):
			rowN = 8;
			colN = 8;
			mines = 10;
			break;
		case (2):
			rowN = 10;
			colN = 10;
			mines = 13;
			break;
		}
		board = new Cell[rowN][colN];
		for (int row = 0; row < rowN; row++)
			for (int col = 0; col < colN; col++) {
				board[row][col] = new Cell();
			}

		// To allocate mines
		allocateMines(board, mines);
		// To calculate each block value
		allocateValues(board);
		
	}

	public CellStates getCellState(int row, int col) {
		return board[row][col].getState();
	}

	public int getCellValue(int row, int col) {
		return board[row][col].getValue();
	}

	public int getNCol() {
		return colN;
	}

	public int getNRow() {
		return rowN;
	}

	public boolean getCellVisibility(int selRow, int selCol) {
		return board[selRow][selCol].isVisible();
	}

	public boolean isFinished() {
		return finished;
	}
	
	public Cell getCell(int row, int col) {
		return board[row][col];
	}

	/**
	 * It sets the cell's status through the given coordinates and indicates when the game is over
	 * @param row Row in which the cell is situated
	 * @param col Column in which the cell is situated
	 */
	public void setCellStatePushed(int row, int col) {
		board[row][col].setState(CellStates.PUSHED);
		pushedCells++;
		if (pushedCells == (colN * rowN - mines))
			finished = true;
	}
	
	
	public void setCellState(int row, int col, CellStates state) {
		board[row][col].setState(state);
	}

	public void setCellVisibility(int selRow, int selCol) {
		board[selRow][selCol].setCellVisible(true);
	}
	
	private void allocateValues(Cell[][] board2) {
		int minesN = 0;
		for (int row = 0; row < rowN; row++) {
			for (int col = 0; col < colN; col++) {
				minesN = checkAround(row, col);
				if (minesN != 0) {
					board[row][col].setValue(minesN);
				}
			}
		}
	}

	private int checkAround(int row, int col) {
		// Looks for a mine in all senses
		int n = 0;
		if (row - 1 >= 0) {
			if (board[row - 1][col].getState() == CellStates.MINE)
				n++;
		}

		if (col - 1 >= 0) {
			if (board[row][col - 1].getState() == CellStates.MINE)
				n++;
		}

		if (row - 1 >= 0 && col - 1 >= 0) {
			if (board[row - 1][col - 1].getState() == CellStates.MINE)
				n++;
		}

		if (col + 1 < colN) {
			if (board[row][col + 1].getState() == CellStates.MINE)
				n++;
		}

		if (row - 1 >= 0 && col + 1 < colN) {
			if (board[row - 1][col + 1].getState() == CellStates.MINE)
				n++;
		}

		if (row + 1 < rowN) {
			if (board[row + 1][col].getState() == CellStates.MINE)
				n++;
		}

		if (row + 1 < rowN && col - 1 >= 0) {
			if (board[row + 1][col - 1].getState() == CellStates.MINE)
				n++;
		}

		if (row + 1 < rowN && col + 1 < colN) {
			if (board[row + 1][col + 1].getState() == CellStates.MINE)
				n++;
		}

		return n;
	}

	private void allocateMines(Cell[][] board, int mines) {
		int n = mines;
		int row = 0, col = 0;
		Random positionGenerator = new Random();
		while (n > 0) {
			row = positionGenerator.nextInt(rowN);
			col = positionGenerator.nextInt(colN);
			if (board[row][col].getState() != CellStates.MINE) {
				board[row][col].setState(CellStates.MINE);
				n--;
			}
		}
	}

	public String toString() {
		String s = "";
		/*for (int i = 0; i < board.length; i++){
			s += "\n";
			for (Cell c : board[i]) {
				switch (c.getState()) {
				case MINE: s+= "B";
					break;
				default: s+= ((Integer)c.getValue()).toString();
					break;
				}
			}
		}*/
		return s;
	}
}
