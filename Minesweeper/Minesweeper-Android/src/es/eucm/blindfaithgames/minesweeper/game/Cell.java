package es.eucm.blindfaithgames.minesweeper.game;

import android.content.Context;

import es.eucm.blindfaithgames.minesweeper.R;



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

	public String cellToString(Context c) {
		switch (state){
		case PUSHED:
			return c.getString(R.string.cellStatePushed) + " "  + value;
		case NOTPUSHED :
			return c.getString(R.string.cellStateNotPushed);
		case FLAGGED:
			return c.getString(R.string.cellStateFlagged);
		case MINE:
			if(!visible)
				return c.getString(R.string.cellStateNotPushed);
			else
				return c.getString(R.string.cellStateMine);
		}
		
		return c.getString(R.string.cellStateUnknown);	
	}
	
}
