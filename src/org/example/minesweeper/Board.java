package org.example.minesweeper;

import java.util.Random;

import org.example.minesweeper.Cell.CellStates;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez This class
 *         implements minesweeper's board
 */

public class Board {

	private Cell[][] board; /*
							 * Contains a matriz of cells that provide the logic
							 * game.
							 */
	private int rowN, colN; /* Number of rows and cols */
	int mines; /* Number of mines in the board */
	boolean finished; /* It indicates whether the game has finished or not */
	private int pushedCells; /* Number of pushed cells in the board */

	/**
	 * Constructor by default that sets the game's difficulty, allocate mines
	 * and precalculate cell's values.
	 * @param difficult Difficulty level.
	 */
	public Board(int difficult) {
		mines = 0;
		finished = false;
		pushedCells = 0;
		// Build the matrix of Cells which depends on difficult
		switch (difficult) {
		case (0):
			rowN = 3;
			colN = 3;
			mines = 1;
			break;
		case (1):
			rowN = 16;
			colN = 16;
			mines = 40;
			break;
		case (2):
			rowN = 16;
			colN = 30;
			mines = 99;
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

		for (int row = 0; row < rowN; row++) {
			for (int col = 0; col < colN; col++) {
				if (board[row][col].getState() != CellStates.MINE)
					System.out.print(board[row][col].getValue() + " ");
				else
					System.out.print("M ");
			}
			System.out.println();
		}
	}

	/*---------------------------------------------------------
	 *  Getters && Setters 
	 *  ------------------------------------------------------*/

	/**
	 * Returns the status of the cell through the given column and row
	 * @param row Row in which the cell is situated
	 * @param col Column in which the cell is situated
	 * @return The status of the cell
	 */
	public CellStates getCellState(int row, int col) {
		return board[row][col].getState();
	}

	/**
	 * Returns the number of mines close to the cell through the given column and row
	 * @param row Row in which the cell is situated
	 * @param col Column in which the cell is situated
	 * @return Number of mines close to the cell
	 */
	public int getCellValue(int row, int col) {
		return board[row][col].getValue();
	}

	/**
	 * It returns the number of columns
	 * @return Number of columns
	 */
	public int getNCol() {
		return colN;
	}

	/**
	 * It returns the number of rows
	 * @return Number of rows
	 */
	public int getNRow() {
		return rowN;
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
	
	/**
	 * It sets the cell's status through the given coordinates and indicates when the game is over
	 * @param row Row in which the cell is situated
	 * @param col Column in which the cell is situated
	 * @param state state in which the cell will be
	 */
	public void setCellState(int row, int col, CellStates state) {
		board[row][col].setState(state);
	}
	
	
	/**
	 * It indicates whether the game has finished or not
	 * @return Boolean that indicates whether the game has finished or not
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * It allocate the values that indicates the number of mines close to each cell
	 * @param board2 Game's board
	 */
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

	/**
	 * Looking mines in every way
	 * @param row Row in which the cell is situated
	 * @param col Column in which the cell is situated
	 * @return Number of mines found
	 */
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

	/**
	 * Allocate a number of mines given by parameter
	 * @param board Game's board
	 * @param mines Number of mines to allocate
	 */
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

}
