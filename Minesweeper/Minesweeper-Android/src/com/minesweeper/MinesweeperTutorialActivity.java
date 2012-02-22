package com.minesweeper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.example.minesweeper.Board;
import org.example.minesweeper.Cell;
import org.example.minesweeper.Cell.CellStates;
import org.example.minesweeper.MinesweeperTutorialView;
import org.example.minesweeper.Music;
import org.example.minesweeper.TTS;
import org.example.minesweeper.XML.KeyboardReader;
import org.example.minesweeper.XML.XMLKeyboard;
import org.example.others.AnalyticsManager;
import org.example.others.Log;
import org.example.others.MinesweeperAnalytics;
import org.example.others.RuntimeConfig;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MinesweeperTutorialActivity extends Activity implements OnFocusChangeListener, OnLongClickListener, OnClickListener {

	private static final String TAG = "Minesweeper";

	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private Dialog loseDialog,winDialog;

	public static final int SPEECH_READ_CODE = 0;
	public static final int VIEW_READ_CODE = 1;
	
	private Board mineField;
	private int rowN;
	private int colN;
	
	private MinesweeperTutorialView minesweeperTutorialView;
	
	private TTS textToSpeech;
	
	private boolean flagMode;

	private View focusedView;
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	
	/* Game states */
	public enum FINAL_STATE {
		WIN, LOSE
	};


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		int difficulty = getIntent().getIntExtra(MinesweeperActivity.KEY_DIFFICULTY, DIFFICULTY_EASY);
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		
		// Start game
		mineField = new Board(difficulty);
		System.out.print(mineField);
		rowN = mineField.getNRow();
		colN = mineField.getNCol();
		
		// Cargamos el teclado del XML
		KeyboardReader reader = new KeyboardReader();
		
		try {
			FileInputStream fis = openFileInput("minesweeper.xml");
			XMLKeyboard keyboard = reader.loadEditedKeyboard(fis);

			minesweeperTutorialView = new MinesweeperTutorialView(this, rowN, colN, keyboard);
			setContentView(minesweeperTutorialView);
			minesweeperTutorialView.requestFocus();
			
			buildWinDialog();

			buildEndingDialog();	
		
			// Initialize TTS engine
			textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
			textToSpeech.setContext(this);
			textToSpeech.setInitialSpeech(this.getString(R.string.tut_intro_text) + ". "  + 
										  this.getString(R.string.tut_drag_text) + ". "  +  
										  this.getString(R.string.tut_step));
			this.textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Log.getLog().addEntry(MinesweeperTutorialActivity.TAG,PrefsActivity.configurationToString(this),
				Log.ONCREATE,Thread.currentThread().getStackTrace()[2].getMethodName(), mineField.getMines());
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.GAME_ACTIVITY);
		
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.MISCELLANEOUS, MinesweeperAnalytics.BOARD, 
					mineField.getMines(), 3);
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
		mTtsAction(SPEECH_READ_CODE, "Dialog: A mine!! " 
											+ getString(R.string.LoseDialogTitle) + " "
											+ getString(R.string.LosePositiveButtonLabel) + " "
											+ getString(R.string.LoseNegativeButtonLabel));
		Log.getLog().addEntry(MinesweeperTutorialActivity.TAG,PrefsActivity.configurationToString(this),
				Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),"User lose " + mineField.getDifficulty());
		
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.GAME_EVENTS, 
				MinesweeperAnalytics.GAME_RESULT, MinesweeperAnalytics.GAME_RESULT_LOSE, 31);
	}

	/**
	 * Show the dialog in the end of game. Dialog buttons are set to can use tts.
	 * */
	public void showWinDialog() {
		winDialog.show();
		mTtsAction(SPEECH_READ_CODE, "Dialog " 
								+ getString(R.string.WinDialogTitle) 
								+ getString(R.string.WinDialogMessage)  
								+ getString(R.string.WinPositiveButtonLabel));
		Log.getLog().addEntry(MinesweeperTutorialActivity.TAG,PrefsActivity.configurationToString(this),
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
		
		winDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				MinesweeperTutorialActivity.this.finish();
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
		
		loseDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				MinesweeperTutorialActivity.this.finish();
			}
		});
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
		loseDialog.dismiss();
		winDialog.dismiss();
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
	}

	@Override
	public boolean onLongClick(View v) {
		menuAction(v);
		return true;
	}

	private void menuAction(View v) {
		switch (v.getId()) {
			case R.id.win_button:
			case R.id.back_button:
				setResult(MinesweeperActivity.EXIT_GAME_CODE);
				break;
			case R.id.reset_button:
				setResult(MinesweeperActivity.RESET_CODE);
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
