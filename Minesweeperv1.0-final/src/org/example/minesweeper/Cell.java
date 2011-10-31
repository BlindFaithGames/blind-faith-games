package org.example.minesweeper;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements minesweeper's cells
 */

public class Cell {

	public static int WIDTH = 20;	
	public static int HEIGHT = 19;
	
	private int value;				/* Number of mines close to this cell */

	private CellStates state; 		/* Mine, push or not push */

	/*	Enum with the diferent states of a cell */
	public static enum CellStates {
		MINE, PUSHED, NO_PUSHED, FLAGGED
	};

	/**
	 * Constructor by default
	 */
	public Cell() {
		super();
		value = 0;
		state = CellStates.NO_PUSHED;
	}

	/**
	 * Parametrized constructor
	 * @param value Number of mines close to this cell
	 * @param state The state of the cell
	 */
	public Cell(int value, CellStates state) {
		super();
		this.value = value;
		this.state = state;
	}

	/**
	 * It returns the state of the cell
	 * @return State of the cell
	 */
	public CellStates getState() {
		return state;
	}

	/**
	 * It returns the value with the number of mines close to the cell
	 * @return Number of mines close to the cell
	 */
	public int getValue() {
		return value;
	}

	/**
	 * It sets the state of the cell
	 * @param state State of the cell
	 */
	public void setState(CellStates state) {
		this.state = state;
	}

	/**
	 * It sets the value with the number of mines close to the cell
	 * @param value Number of mines close to the cell
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
