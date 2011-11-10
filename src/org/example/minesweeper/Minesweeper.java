package org.example.minesweeper;

import org.example.minesweeper.Cell.CellStates;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

public class Minesweeper extends Activity implements OnFocusChangeListener {

	private static final String TAG = "Minesweeper";

	public static final String KEY_DIFFICULTY = "org.example.minesweeper.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private AlertDialog loseDialog,winDialog ;

	public static final int SPEECH_READ_CODE = 0;
	public static final int VIEW_READ_CODE = 1;
	
	private Board mineField;
	private int rowN;
	private int colN;
	
	private MinesweeperView minesweeperView;
	
	private TextToSpeech mTts;
	
	private boolean flagMode;
	
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
		Log.d(TAG, "onCreate"); // Debug
		int difficult = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		
		// Start game
		mineField = new Board(difficult);
		System.out.print(mineField);
		rowN = mineField.getNRow();
		colN = mineField.getNCol();

		minesweeperView = new MinesweeperView(this, rowN, colN);
		setContentView(minesweeperView);
		minesweeperView.requestFocus();
		
		buildWinDialog();
		buildEndingDialog();
		
	
		OnInitTTS initialize = new OnInitTTS(mTts);
		if(OnInitTTS.isInstalled(this) && Prefs.getTTS(this)){
			mTts = new TextToSpeech(this,initialize);
			initialize.setmTts(mTts);
		}
	}
	
	/**
	 * onFocusChange Interface
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			mTtsAction(v,VIEW_READ_CODE,null);	
		}
	}
	
	/**
	 * Show the dialog in the end of game. Dialog buttons are set to can use tts.
	 * */
	public void showLoseDialog() {
		loseDialog.show();
		Button buttonPositive = loseDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonPositive.setContentDescription(getString(R.string.LosePositiveButtonLabel));
		buttonPositive.setOnFocusChangeListener(this);
		Button buttonNegative = loseDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		buttonNegative.setContentDescription(getString(R.string.LoseNegativeButtonLabel));
		buttonNegative.setOnFocusChangeListener(this);
		mTtsAction(null, SPEECH_READ_CODE, "AlerDialog: A mine!! " 
											+ getString(R.string.LoseDialogTitle) + " "
											+ getString(R.string.LosePositiveButtonLabel) + " "
											+ getString(R.string.LoseNegativeButtonLabel));
	}

	/**
	 * Show the dialog in the end of game. Dialog buttons are set to can use tts.
	 * */
	public void showWinDialog() {
		winDialog.show();
		Button buttonPositive = winDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonPositive.setContentDescription(getString(R.string.WinPositiveButtonLabel));
		buttonPositive.setOnFocusChangeListener(this);
		mTtsAction(null, SPEECH_READ_CODE, "AlerDialog " 
								+ getString(R.string.WinDialogTitle) 
								+ getString(R.string.WinDialogMessage)  
								+ getString(R.string.WinPositiveButtonLabel));
	}
	
	/**
	 * If action is IMAGE_BUTTON_READ_CODE, it reads cell state and value.
	 * If action is VIEW_READ_CODE, it reads contentDescription Attribute.
	 * If action is SPEECH_READ_CODE, it reads speech.
	 * **/
	public void mTtsAction(View v, int action, String speech){
		if(mTts != null){
			switch(action){
			case VIEW_READ_CODE:
				mTts.speak(v.getContentDescription().toString(),TextToSpeech.QUEUE_ADD,null);
				break;
			case SPEECH_READ_CODE:
				mTts.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
				break;
			}
		}
	}
	/** 
	 * It reads control instructions
	 * **/
	public void mTtsActionControls(){
		mTts.speak(getString(R.string.instructions_controls_text),TextToSpeech.QUEUE_FLUSH,null);
	}
	/**
	 * Builds the dialog shown at the end of the game, when the result is positive
	 */
	private void buildWinDialog() {
		winDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.WinDialogTitle)
				.setMessage(R.string.WinDialogMessage)
				.setIcon(R.drawable.win)
				.setPositiveButton(R.string.WinPositiveButtonLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setResult(Game.EXIT_GAME_CODE);
								Minesweeper.this.finish();
							}
						}).create();
	}

	/**
	 * Builds the dialog shown at the end of the game, when the result is negative
	 */
	private void buildEndingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.LoseDialogTitle)
				.setCancelable(false)
				.setPositiveButton(R.string.LosePositiveButtonLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setResult(Game.RESET_CODE);
								Minesweeper.this.finish();
							}
						})
				.setNegativeButton(R.string.LoseNegativeButtonLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setResult(Game.EXIT_GAME_CODE);
								Minesweeper.this.finish();
							}
						});
		loseDialog = builder.create();
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
	protected String getTileString(int row, int col) {
		int v = mineField.getCellValue(row, col);
		return String.valueOf(v);
	}
	
	public Cell getCell(int row, int col){
		return mineField.getCell(row, col);
	}

	public void pushCell(int selRow, int selCol) {
		if(flagMode){
			if(mineField.getCellState(selRow, selCol) == CellStates.FLAGGED) 
					mineField.setCellState(selRow, selCol,CellStates.NOTPUSHED);	
			else
				if(mineField.getCellState(selRow, selCol) != CellStates.PUSHED)
					mineField.setCellState(selRow, selCol,CellStates.FLAGGED);	
		}
		else{
			if(mineField.getCellState(selRow, selCol) == CellStates.MINE){
					showMines();
					endGame(FINAL_STATE.LOSE);
			}
			else{
				if(mineField.getCellState(selRow, selCol) != CellStates.FLAGGED)
					expandCell(selRow, selCol);
			}
			if(mineField.isFinished()){
				endGame(FINAL_STATE.WIN);
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
	  Log.d(TAG, "Cambio de orientaci�n de pantalla");
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
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
	}
	
	
}
