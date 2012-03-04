package com.minesweeper;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;

import com.accgames.others.AnalyticsManager;
import com.minesweeper.R;
import com.minesweeper.game.Board;
import com.minesweeper.game.Cell;
import com.minesweeper.game.Cell.CellStates;
import com.minesweeper.game.MinesweeperAnalytics;
import com.minesweeper.game.MinesweeperTutorialView;
import com.minesweeper.game.Music;
import com.minesweeper.game.TTS;

public class MinesweeperTutorialActivity extends Activity implements OnFocusChangeListener, OnLongClickListener, OnClickListener {

	private static final String TAG = "Minesweeper";

	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	public static final int SPEECH_READ_CODE = 0;
	public static final int VIEW_READ_CODE = 1;
	
	private Board mineField;
	private int rowN;
	private int colN;
	
	private MinesweeperTutorialView minesweeperTutorialView;
	
	private TTS textToSpeech;
	
	private boolean flagMode;

	private View focusedView;
	

	/* Game states */
	public enum FINAL_STATE {
		WIN, LOSE
	};


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		int difficulty = getIntent().getIntExtra(MinesweeperActivity.KEY_DIFFICULTY, DIFFICULTY_EASY);

		// Start game
		mineField = new Board(difficulty);
		System.out.print(mineField);
		rowN = mineField.getNRow();
		colN = mineField.getNCol();

		minesweeperTutorialView = new MinesweeperTutorialView(this, rowN, colN);
		setContentView(minesweeperTutorialView);
		minesweeperTutorialView.requestFocus();
	
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(this.getString(R.string.tut_intro_text) + ". "  + 
									  this.getString(R.string.tut_drag_text) + ". "  +  
									  this.getString(R.string.tut_step));
		this.textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.TUTORIAL_ACTIVITY);
		
	}


	/**
	 * onFocusChange Interface
	 */
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			mTtsAction(VIEW_READ_CODE,v);	
			focusedView = v;
		}
	}
	
	/**
	 * If action is VIEW_READ_CODE, it reads contentDescription Attribute.
	 * If action is SPEECH_READ_CODE, it reads speech.
	 * **/
	public void mTtsAction(int action, Object speech){
		switch(action){
		case VIEW_READ_CODE:
			textToSpeech.speak((View)speech);
			break;
		case SPEECH_READ_CODE:
			textToSpeech.speak((String)speech);
			break;
		}
	}
	/** 
	 * It reads control instructions
	 * **/
	public void mTtsActionControls(){
		textToSpeech.speak(getString(R.string.instructions_controls_text));
	}

	private void expandCell(int row, int col) {
		if (mineField.getCellValue(row, col) != 0) {
			mineField.setCellVisibility(row, col);
			if (mineField.getCellState(row, col) != CellStates.PUSHED && 
				mineField.getCellState(row, col) != CellStates.MINE &&
				mineField.getCellState(row, col) != CellStates.FLAGGED)
				mineField.setCellStatePushed(row, col);
		/*if not 0 and not visible*/
		} else if (mineField.getCellState(row, col) != CellStates.PUSHED
				&& mineField.getCellState(row, col) != CellStates.MINE
				&& mineField.getCellState(row, col) != CellStates.FLAGGED) {			
			
			mineField.setCellStatePushed(row, col);
			
			mineField.setCellVisibility(row, col);
			if (col - 1 >= 0)
				expandCell(row, col - 1);
			if (col + 1 < colN)
				expandCell(row, col + 1);
			if (row + 1 < rowN)
				expandCell(row + 1, col);
			if (row - 1 >= 0)
				expandCell(row - 1, col);
			if (col - 1 >= 0 && row - 1 >= 0)
				expandCell(row - 1, col - 1);
			if (col - 1 >= 0 && row + 1 < rowN)
				expandCell(row + 1, col - 1);
			if (col + 1 < colN && row - 1 >= 0)
				expandCell(row - 1, col + 1);
			if (col + 1 < colN && row + 1 < rowN)
				expandCell(row + 1, col + 1);
		}
	}

	private void showMines() {
		for (int row = 0; row < rowN; row++)
			for (int column = 0; column < colN; column++) {
				if (mineField.getCellState(row, column).equals(CellStates.MINE))
					mineField.setCellVisibility(row, column);
			}
	}
	
	/** Return a string for the tile at the given coordinates */
	public String getTileString(int row, int col) {
		int v = mineField.getCellValue(row, col);
		return String.valueOf(v);
	}
	
	public Cell getCell(int row, int col){
		return mineField.getCell(row, col);
	}

	public void pushCell(int selRow, int selCol) {
		if(flagMode){
			if(mineField.getCellState(selRow, selCol) == CellStates.FLAGGED)
				if(mineField.getCellValue(selRow, selCol) != -1)
					mineField.setCellState(selRow, selCol, CellStates.NOTPUSHED);
				else
					mineField.setCellState(selRow, selCol, CellStates.MINE);
			else
				if(mineField.getCellState(selRow, selCol) != CellStates.PUSHED)
					mineField.setCellState(selRow, selCol, CellStates.FLAGGED);	
		}
		else{
			if(mineField.getCellState(selRow, selCol) == CellStates.MINE){
					showMines();
			}
			else{
				if(mineField.getCellState(selRow, selCol) != CellStates.FLAGGED)
					expandCell(selRow, selCol);
			}
		}

	}
	
	public boolean isVisibleCell(int selRow, int selCol) {
		return mineField.getCellVisibility(selRow, selCol);
	}
	
	/**
	 * Switches the sound from on to off, or off to on
	 */
    public void switchFlag() {
		if (flagMode) {
			flagMode = false;
		} else {
			flagMode = true;
		}
	}

	public boolean isFlagMode() {
		return flagMode;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	}

	public void speakContextFocusedCell(int selRow, int selCol) {
		if (PrefsActivity.getCoordinates(this)){
			List<String> msg = new ArrayList<String>();
			
			if(selRow - 1 >= 0)
				msg.add(mineField.getCell(selRow - 1, selCol).toString());
			if(selRow - 1 >= 0 && selCol + 1 <= colN)
				msg.add(mineField.getCell(selRow - 1, selCol + 1).toString());		
			if(selCol + 1 <= colN)
				msg.add(mineField.getCell(selRow, selCol + 1).toString());	
			if(selRow + 1 <= rowN && selCol + 1 <= colN)
				msg.add(mineField.getCell(selRow + 1, selCol + 1).toString());	
			if(selRow + 1 <= rowN)
				msg.add(mineField.getCell(selRow + 1, selCol).toString());	
			if(selRow + 1 <= rowN && selCol - 1 >= 0)
				msg.add(mineField.getCell(selRow + 1, selCol - 1).toString());	
			if(selCol - 1 >= 0)
				msg.add(mineField.getCell(selRow, selCol - 1).toString());	
			if(selCol - 1 >= 0 && selRow - 1 >= 0)
				msg.add(mineField.getCell(selRow - 1, selCol - 1).toString());
			
			textToSpeech.speak(msg);
		}
	}
	
	/**
	 * ------------------------------------------------------------ 
	 * Musica
	 * ---------------------------------------------------------------
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.game);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}

	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
        textToSpeech.stop();
	}

	@Override
	public void onClick(View v) {
		if(focusedView != null){
			if(focusedView.getId() == v.getId())
				menuAction(v);
			else
				textToSpeech.speak(v);
		}
		else
			textToSpeech.speak(v);
		if(v != null)
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.TUTORIAL_EVENTS, MinesweeperAnalytics.CLICK, 
				"Button reading", 3);
		else
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.TUTORIAL_EVENTS, MinesweeperAnalytics.CLICK, 
					"Button Reading fail", 3);
	}

	@Override
	public boolean onLongClick(View v) {
		menuAction(v);
		if(v == null)
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.TUTORIAL_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
					"Fail", 3);
		return true;
	}

	private void menuAction(View v) {
		switch (v.getId()) {
			case R.id.win_button:
				setResult(MinesweeperActivity.EXIT_GAME_CODE);
				AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.TUTORIAL_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
						"Win game button", 3);
				break;
			case R.id.back_button:
				setResult(MinesweeperActivity.EXIT_GAME_CODE);
				AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.TUTORIAL_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
						"Back button", 3);
				break;
			case R.id.reset_button:
				setResult(MinesweeperActivity.RESET_CODE);
				AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.TUTORIAL_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
						"Lose game button", 3);
				break;					
		}
		MinesweeperTutorialActivity.this.finish();
	}


	public TTS getTTS() {
		return textToSpeech;
	}

	public void resetBoard() {
		mineField = new Board(0);
	}
	
}
