package com.minesweeper.game;


public class Cell {
	private int value;
	private boolean visible;
	private CellStates state;

	public static enum CellStates {
		MINE, PUSHED, NOTPUSHED, FLAGGED
	};

	public Cell() {
		super();
		value = -1;
		state = CellStates.NOTPUSHED;
	}

	public Cell(int value, CellStates state, boolean visible) {
		super();
		this.value = value;
		this.state = state;
	}

	public CellStates getState() {
		return state;
	}

	public int getValue() {
		return value;
	}

	public void setState(CellStates state) {
		this.state = state;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setCellVisible(boolean visible) {
		this.visible = visible;
	}

	public String toString(){
		switch(state){
			case PUSHED:
				return "Pushed " + value;
			case NOTPUSHED :
				return "No pushed";
			case FLAGGED:
				return "Flagged";
			case MINE:
				if(!visible)
					return "No pushed";
				else
					return "Mine";
		}
		return "Unkwown state";
	}

}
