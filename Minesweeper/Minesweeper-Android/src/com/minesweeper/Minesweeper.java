package com.minesweeper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.accgames.feedback.AnalyticsManager;
import com.accgames.feedback.Log;
import com.accgames.input.Input;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.TTS;
import com.minesweeper.game.Board;
import com.minesweeper.game.Cell;
import com.minesweeper.game.Cell.CellStates;
import com.minesweeper.game.MinesweeperAnalytics;
import com.minesweeper.game.MinesweeperView;

public class Minesweeper extends Activity implements OnFocusChangeListener, OnLongClickListener, OnClickListener, OnKeyListener {

	private static final String TAG = "Minesweeper";

	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private Dialog loseDialog, winDialog;

	public static final int SPEECH_READ_CODE = 0;
	public static final int VIEW_READ_CODE = 1;
	public static final int REPEAT_CODE = 2;
	
	private Board mineField;
	private int rowN;
	private int colN;
	
	private MinesweeperView minesweeperView;
	
	private TTS textToSpeech;
	
	private boolean flagMode;

	private View focusedView;

	private boolean blindInteraction;
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	
	private int counter;

	private boolean finished;
	
	/* Game states */
	public enum FINAL_STATE {
		WIN, LOSE
	};

	/**
	 * Ends the game depending on the game state
	 * @param state Game's state
	 */
	private void endGame(FINAL_STATE state) {
		if (state == FINAL_STATE.LOSE)
			showLoseDialog();
		if (state == FINAL_STATE.WIN)
			showWinDialog();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		int difficulty = getIntent().getIntExtra(MinesweeperActivity.KEY_DIFFICULTY, DIFFICULTY_EASY);
		blindInteraction =  PrefsActivity.getBlindInteraction(this);
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		// Start game
		mineField = new Board(difficulty);
		System.out.print(mineField);
		rowN = mineField.getNRow();
		colN = mineField.getNCol();
		
		counter = 0;
		finished = false;
		
		minesweeperView = new MinesweeperView(this, rowN, colN);
		setContentView(minesweeperView);
		minesweeperView.requestFocus();
		
		buildWinDialog();

		buildEndingDialog();	
	
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(this.getString(R.string.game_initial_TTStext));
		
		Log.getLog().addEntry(Minesweeper.TAG,PrefsActivity.configurationToString(this),
				Log.ONCREATE,Thread.currentThread().getStackTrace()[2].getMethodName(), mineField.getMines());
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.GAME_ACTIVITY);
		
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.MISCELLANEOUS, MinesweeperAnalytics.BOARD, 
					mineField.getMines(), 0);
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
	 * Show the dialog in the end of game. Dialog buttons are set to can use tts.
	 * */
	public void showLoseDialog() {
		loseDialog.show();
		mTtsAction(SPEECH_READ_CODE, getString(R.string.LoseDialogTitle) + ", "
									+ getString(R.string.LosePositiveButtonLabel) + ", "
									+ getString(R.string.LoseNegativeButtonLabel));
		Log.getLog().addEntry(Minesweeper.TAG,PrefsActivity.configurationToString(this),
				Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),"User lose " + mineField.getDifficulty());
		
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.GAME_EVENTS, 
				MinesweeperAnalytics.GAME_RESULT, MinesweeperAnalytics.GAME_RESULT_LOSE, 31);
	}

	/**
	 * Show the dialog in the end of game. Dialog buttons are set to can use tts.
	 * */
	public void showWinDialog() {
		winDialog.show();
		mTtsAction(SPEECH_READ_CODE, getString(R.string.WinDialogTitle) + "... "
								+ getString(R.string.WinDialogMessage) + " " 
								+ getString(R.string.WinPositiveButtonLabel));
		Log.getLog().addEntry(Minesweeper.TAG,PrefsActivity.configurationToString(this),
				Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),"User win " + mineField.getDifficulty());
		
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.GAME_EVENTS, 
				MinesweeperAnalytics.GAME_RESULT, MinesweeperAnalytics.GAME_RESULT_WIN, 21);
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
		case REPEAT_CODE:
			textToSpeech.repeatSpeak();
			break;
		}
	}
	/** 
	 * It reads control instructions
	 * **/
	public void mTtsActionControls(){
		textToSpeech.speak(getString(R.string.instructions_controls_text));
	}
	
	/**
	 * Builds the dialog shown at the end of the game, when the result is positive
	 */
	private void buildWinDialog() {
		Button b; TextView t;
		
		winDialog = new Dialog(this);
		winDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (PrefsActivity.getBlindMode(this))
			winDialog.setContentView(R.layout.blind_win_dialog);
		else
			winDialog.setContentView(R.layout.win_dialog);
		
		t = (TextView) winDialog.findViewById(R.id.win_dialog_textView);
		t.setTextSize(fontSize);
		t.setTypeface(font);
		b = (Button) winDialog.findViewById(R.id.win_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);
		
		winDialog.setOnKeyListener(this);
		
		winDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				Minesweeper.this.finish();
			}
		});
	}

	/**
	 * Builds the dialog shown at the end of the game, when the result is negative
	 */
	private void buildEndingDialog() {
		Button b; TextView t;
		
		loseDialog = new Dialog(this);
		loseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (PrefsActivity.getBlindMode(this))
			loseDialog.setContentView(R.layout.blind_lose_dialog);
		else
			loseDialog.setContentView(R.layout.lose_dialog);
		
		t = (TextView) loseDialog.findViewById(R.id.lose_dialog_textView);
		t.setTextSize(fontSize);
		t.setTypeface(font);
		b = (Button) loseDialog.findViewById(R.id.reset_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);
		b = (Button) loseDialog.findViewById(R.id.back_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);
		
		loseDialog.setOnKeyListener(this);
		
		loseDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				Minesweeper.this.finish();
			}
		});
	}
	
	private void expandCell(int row, int col) {
		counter++;
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

	public boolean pushCell(int selRow, int selCol) {
		counter = 0;
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
					endGame(FINAL_STATE.LOSE);
					return false;
			}
			else{
				if(mineField.getCellState(selRow, selCol) != CellStates.FLAGGED)
					expandCell(selRow, selCol);
			}
			if(mineField.isFinished()){
				endGame(FINAL_STATE.WIN);
				return false;
			}	
		}
		return true;

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
		List<String> msg = new ArrayList<String>();
		
		if(selRow - 1 >= 0)
			msg.add(mineField.getCell(selRow - 1, selCol).cellToString(this));
		if(selRow - 1 >= 0 && selCol + 1 <= colN)
			msg.add(mineField.getCell(selRow - 1, selCol + 1).cellToString(this));		
		if(selCol + 1 <= colN)
			msg.add(mineField.getCell(selRow, selCol + 1).cellToString(this));	
		if(selRow + 1 <= rowN && selCol + 1 <= colN)
			msg.add(mineField.getCell(selRow + 1, selCol + 1).cellToString(this));	
		if(selRow + 1 <= rowN)
			msg.add(mineField.getCell(selRow + 1, selCol).cellToString(this));	
		if(selRow + 1 <= rowN && selCol - 1 >= 0)
			msg.add(mineField.getCell(selRow + 1, selCol - 1).cellToString(this));	
		if(selCol - 1 >= 0)
			msg.add(mineField.getCell(selRow, selCol - 1).cellToString(this));	
		if(selCol - 1 >= 0 && selRow - 1 >= 0)
			msg.add(mineField.getCell(selRow - 1, selCol - 1).cellToString(this));
		
		textToSpeech.speak(msg);
	}
	
	/**
	 * ------------------------------------------------------------ 
	 * Musica
	 * ---------------------------------------------------------------
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Music.getInstanceMusic().play(this, R.raw.game, true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		loseDialog.dismiss();
		winDialog.dismiss();
		Music.getInstanceMusic().stop(R.raw.game);
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
		if (blindInteraction){
			if(focusedView != null){
				if(focusedView.getId() == v.getId())
					menuAction(v);
				else
					textToSpeech.speak(v);
			}
			else
				textToSpeech.speak(v);
		}
		else{
			menuAction(v);
		}
		if(v != null)
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.GAME_EVENTS, MinesweeperAnalytics.CLICK, 
				"Button Reading", 3);
		else
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.GAME_EVENTS, MinesweeperAnalytics.CLICK, 
						"Button Reading fail", 3);
	}

	@Override
	public boolean onLongClick(View v) {
		if (blindInteraction){
			menuAction(v);
			return true;
		}
		if (v == null)
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.GAME_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
					"Fail", 3);
		return false;
	}

	private void menuAction(View v) {
		switch (v.getId()) {
		case R.id.win_button:
			setResult(MinesweeperActivity.EXIT_GAME_CODE);
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.GAME_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
					"Win game button", 3);
			break;
		case R.id.back_button:
			setResult(MinesweeperActivity.EXIT_GAME_CODE);
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.GAME_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
					"Back button", 3);
			break;
		case R.id.reset_button:
			setResult(MinesweeperActivity.RESET_CODE);
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.GAME_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
					"Lose game button", 3);
			break;					
	}
		Minesweeper.this.finish();
	}
	
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode)
			return false;
		
		Integer k = Input.getKeyboard().getKeyByAction(KeyConfActivity.ACTION_REPEAT);
		if(k != null){
			if(keyCode == k){
				textToSpeech.repeatSpeak();
			}
		}	
		return true;
	}

	public int getCounter() {
		int aux = counter;
		counter = 0;
		return aux;
	}

	public boolean isFinished() {
		return finished;
	}
}
