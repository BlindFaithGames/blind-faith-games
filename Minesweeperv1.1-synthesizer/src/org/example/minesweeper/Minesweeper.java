package org.example.minesweeper;

import org.example.minesweeper.Cell.CellStates;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez This class
 *         implements the logic game
 */

public class Minesweeper extends Activity implements OnClickListener,
		OnFocusChangeListener/*, OnLongClickListener*/ {
	private static final String TAG = "Minesweeper";

	public static final String KEY_DIFFICULTY = "org.example.minesweeper.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private static final int SPEECH_READ_CODE = 0;
	private static final int VIEW_READ_CODE = 1;
	private static final int IMAGE_BUTTON_READ_CODE = 3;

	private TableLayout uiBoard;
	private ImageButton blocks[][];
	private AlertDialog loseDialog, winDialog;
	private Chronometer chrono;

	private Board mineField;
	private int rowN;
	private int colN;

	private boolean flagMode;

	private TextToSpeech mTts;

	/* Game states */
	public enum FINAL_STATE {
		WIN, LOSE
	};

	/**
	 * Show the dialog in the end of game. Dialog buttons are set to can use
	 * tts.
	 * */
	public void showLoseDialog() {
		loseDialog.show();
		Button buttonPositive = loseDialog
				.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonPositive
				.setContentDescription(getString(R.string.LosePositiveButtonLabel));
		buttonPositive.setOnFocusChangeListener(this);
		Button buttonNegative = loseDialog
				.getButton(DialogInterface.BUTTON_NEGATIVE);
		buttonNegative
				.setContentDescription(getString(R.string.LoseNegativeButtonLabel));
		buttonNegative.setOnFocusChangeListener(this);
		mTtsAction(null, SPEECH_READ_CODE, "AlerDialog: A mine!! "
				+ getString(R.string.LoseDialogTitle) + " "
				+ getString(R.string.LosePositiveButtonLabel) + " "
				+ getString(R.string.LoseNegativeButtonLabel));
	}

	/**
	 * Show the dialog in the end of game. Dialog buttons are set to can use
	 * tts.
	 * */
	public void showWinDialog() {
		winDialog.show();
		Button buttonPositive = winDialog
				.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonPositive
				.setContentDescription(getString(R.string.WinPositiveButtonLabel));
		buttonPositive.setOnFocusChangeListener(this);
		mTtsAction(null, SPEECH_READ_CODE, "AlerDialog "
				+ getString(R.string.WinDialogTitle)
				+ getString(R.string.WinDialogMessage)
				+ " Congratulations you win Options: "
				+ getString(R.string.WinPositiveButtonLabel));
	}

	/**
	 * Ends the game depending on the game state
	 * 
	 * @param state
	 *            Game's state
	 */
	private void endGame(FINAL_STATE state) {
		chrono.stop();
		if (state == FINAL_STATE.LOSE)
			showLoseDialog();
		if (state == FINAL_STATE.WIN)
			showWinDialog();
	}

	/**
	 * Game creation
	 */
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate"); // Debug
		super.onCreate(savedInstanceState);
		setContentView(R.layout.minefield);

		int difficult = getIntent()
				.getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);

		// Start game
		mineField = new Board(difficult);
		rowN = mineField.getNRow();
		colN = mineField.getNCol();

		initializeUI();

		chrono.start();

		OnInitTTS initialize = new OnInitTTS(mTts);
		if (OnInitTTS.isInstalled(this)) {
			// Success! create de TTS instance
			mTts = new TextToSpeech(this, initialize);
			initialize.setmTts(mTts);
		}
		else{		
            // missing data, install it
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
		}
	}

	/**
	 * Used in onCreate() to set up the UI elements
	 */
	public void initializeUI() {
		// get views from ID's
		chrono = (Chronometer) findViewById(R.id.chrono);
		buildMineField();
		buildEndingDialog();
		buildWinDialog();
	}

	/**
	 * Builds the minefield, allocating the mines and the number of mines
	 * through the board game
	 */
	private void buildMineField() {
		View uiBoa = findViewById(R.id.tableLayout1);
		uiBoard = (TableLayout) uiBoa;
		blocks = new ImageButton[rowN][colN];
		int id = 0;
		for (int row = 0; row < rowN; row++) {
			TableRow tableRow = new TableRow(this);
			int blockPadding = 10;
			int blockDimension = 10;
			tableRow.setLayoutParams(new LayoutParams(
					(blockDimension + 2 * blockPadding) * colN, blockDimension
							+ 2 * blockPadding));

			for (int column = 0; column < colN; column++) {
				blocks[row][column] = new ImageButton(this);
				blocks[row][column].setImageResource(R.drawable.normalcell);
				blocks[row][column].setId(id);
				id++;
				//blocks[row][column].setBackgroundColor(Color.TRANSPARENT);
				// blocks[row][column].setOnFocusChangeListener(this);
				blocks[row][column].setOnClickListener(this);
				//blocks[row][column].setOnLongClickListener(this);
				blocks[row][column].setPadding(blockPadding, blockPadding,
						blockPadding, blockPadding);
				tableRow.addView(blocks[row][column]);
			}
			uiBoard.addView(tableRow, new TableLayout.LayoutParams(
					(blockDimension + 2 * blockPadding) * colN, blockDimension
							+ 2 * blockPadding));
		}

	}

	/**
	 * onClick manager
	 */
	public void onClick(View v) {
		if (v instanceof ImageButton)
			onClickMineFieldButton(v);
//			mTtsAction(v, IMAGE_BUTTON_READ_CODE, null);
//		else
//			mTtsAction(v, VIEW_READ_CODE, null);
	}

	/**
	 * onFocusChange Interface
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (v instanceof ImageButton)
				mTtsAction(v, IMAGE_BUTTON_READ_CODE, null);
			else
				mTtsAction(v, VIEW_READ_CODE, null);
		}
	}

	/**
	 * If action is IMAGE_BUTTON_READ_CODE, it reads cell state and value. If
	 * action is VIEW_READ_CODE, it reads contentDescription Attribute. If
	 * action is SPEECH_READ_CODE, it reads speech.
	 * **/
	private void mTtsAction(View v, int action, String speech) {
		if (mTts != null) {

			switch (action) {
			case IMAGE_BUTTON_READ_CODE:
				ImageButton imgButton = (ImageButton) v;

				int[] pos = searchButton(imgButton.getId());
				int row = pos[0];
				int col = pos[1];

				if (mineField.getCellState(row, col) == Cell.CellStates.PUSHED)
					mTts.speak(
							"Position: "
									+ row
									+ " "
									+ col
									+ " "
									+ " State "
									+ Cell.stateToString(mineField
											.getCellState(row, col)) + " "
									+ mineField.getCellValue(row, col),
							TextToSpeech.QUEUE_FLUSH, null);
				else {
					mTts.speak("Position: " + row + " " + col + " " + " State "
							+ Cell.stateToString(Cell.CellStates.NO_PUSHED),
							TextToSpeech.QUEUE_FLUSH, null);
				}
				break;
			case VIEW_READ_CODE:
				mTts.speak(v.getContentDescription().toString(),
						TextToSpeech.QUEUE_FLUSH, null);
				break;
			case SPEECH_READ_CODE:
				mTts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
				break;
			}
		}

	}

	/**
	 * onLongClick Interface
	 */
//	@Override
//	public boolean onLongClick(View v) {
//		if (v instanceof ImageButton) {
//			onClickMineFieldButton(v);
//		} else
//			mTtsAction(v, VIEW_READ_CODE, null);
//		return true;
//	}

	/**
	 * Manages the game states changes when an event happens.
	 */
	private void onClickMineFieldButton(View v) {
		ImageButton imgButton = (ImageButton) v;

		int[] pos = searchButton(imgButton.getId());
		int row = pos[0];
		int col = pos[1];

		CellStates state = mineField.getCellState(row, col);

		if (flagMode) {
			switch (state) {
			case FLAGGED:
				blocks[row][col].setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.normalcell));
				mineField.setCellState(row, col, CellStates.NO_PUSHED);
				mTtsAction(v, IMAGE_BUTTON_READ_CODE, null);
				break;
			case PUSHED:
				break;
			default:
				blocks[row][col].setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.cellflag));
				mineField.setCellState(row, col, CellStates.FLAGGED);
				mTtsAction(v, IMAGE_BUTTON_READ_CODE, null);
				break;
			}
		} else {
			switch (state) {
			case NO_PUSHED:
				expandCell(row, col, imgButton);
				mTtsAction(v, IMAGE_BUTTON_READ_CODE, null);
				if (mineField.isFinished())
					endGame(FINAL_STATE.WIN);
				break;
			case MINE:
				for (int rows = 0; rows < rowN; rows++)
					for (int column = 0; column < colN; column++) {
						state = mineField.getCellState(rows, column);
						if (state.equals(CellStates.MINE))
							blocks[rows][column].setImageBitmap(BitmapFactory
									.decodeResource(getResources(),
											R.drawable.minecell));
					}
				mTtsAction(v, IMAGE_BUTTON_READ_CODE, null);
				endGame(FINAL_STATE.LOSE);
				break;
			}
		}
	}

	/**
	 * Builds the dialog shown at the end of the game, when the result is
	 * positive
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
	 * Builds the dialog shown at the end of the game, when the result is
	 * negative
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

	/**
	 * Returns the button coordinates given by the id
	 * 
	 * @param id
	 *            Button identifier
	 * @return An array with the button coordinates
	 */
	private int[] searchButton(int id) {
		int row = 0, col = 0;
		boolean found = false;
		int[] pos = null;
		while (row < rowN && !found) {
			col = 0;
			while (col < colN && !found) {
				found = blocks[row][col].getId() == id;
				if (found) {
					pos = new int[2];
					pos[0] = row;
					pos[1] = col;
				}
				col++;
			}
			row++;
		}
		return pos;
	}

	/**
	 * Calculates the cells that has to be shown when a touch is detected
	 * 
	 * @param row
	 *            Button row
	 * @param col
	 *            Button column
	 * @param imgButton
	 *            The button image
	 */
	private void expandCell(int row, int col, ImageButton imgButton) {
		if (mineField.getCellValue(row, col) != 0) {
			if (mineField.getCellState(row, col) != CellStates.PUSHED)
				mineField.setCellStatePushed(row, col);
			showCellValue(row, col, imgButton);
		} else if (mineField.getCellState(row, col) != CellStates.PUSHED
				&& mineField.getCellState(row, col) != CellStates.MINE) {
			if (mineField.getCellState(row, col) != CellStates.PUSHED)
				mineField.setCellStatePushed(row, col);
			showCellValue(row, col, imgButton);
			if (col - 1 >= 0)
				expandCell(row, col - 1, blocks[row][col - 1]);
			if (col + 1 < colN)
				expandCell(row, col + 1, blocks[row][col + 1]);
			if (row + 1 < rowN)
				expandCell(row + 1, col, blocks[row + 1][col]);
			if (row - 1 >= 0)
				expandCell(row - 1, col, blocks[row - 1][col]);
			// Diagonals
			if (col - 1 >= 0 && row - 1 >= 0)
				expandCell(row - 1, col - 1, blocks[row - 1][col - 1]);
			if (col - 1 >= 0 && row + 1 < rowN)
				expandCell(row + 1, col - 1, blocks[row + 1][col - 1]);
			if (col + 1 < colN && row - 1 >= 0)
				expandCell(row - 1, col + 1, blocks[row - 1][col + 1]);
			if (col + 1 < colN && row + 1 < rowN)
				expandCell(row + 1, col + 1, blocks[row + 1][col + 1]);
		}
	}

	/**
	 * Shows the number of mines close to the cell given by its coordinates
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param imgButton
	 *            Button image
	 */
	private void showCellValue(int x, int y, ImageButton imgButton) {

		switch (mineField.getCellValue(x, y)) {
		case (0):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			break;
		case (1):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell1));
			break;
		case (2):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell2));
			break;
		case (3):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell3));
			break;
		case (4):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			break;
		case (5):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			break;
		case (6):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			break;
		case (7):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			break;
		case (8):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			break;
		}

	}

	/**
	 * It creates the options menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.gamemenu, menu);
		return true;
	}

	/**
	 * Changes the button state
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		// Update the sound menu item to display whether sound is on or off
//		MenuItem flagsItem = menu.getItem(0);
//		flagsItem.setTitle(getString(R.string.explore)
//				+ (flagMode ? " On" : " Off"));
		switchFlag();
		mTtsAction(null, SPEECH_READ_CODE, "Menu "
				+ getString(R.string.explore) + (flagMode ? " On" : " Off"));
		return true;
	}

	/**
	 * Manages what to do depending on the selected item
	 */
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.flags:
//			switchFlag();
//			mTtsAction(null, SPEECH_READ_CODE, getString(R.string.explore)
//					+ (flagMode ? " Off" : " On"));
//			return true;
//			// More items go here (if any) ...
//		}
//		return false;
//	}

	/**
	 * Switches the sound from on to off, or off to on
	 */
	private void switchFlag() {
		if (flagMode) {
			flagMode = false;
		} else {
			flagMode = true;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "Cambio de orientacion de pantalla");
	}

	/**
	 * When search button is pushed, text-to-speech indicates the current position
	 */
	public boolean onSearchRequested(){
		if (this.getCurrentFocus() instanceof ImageButton){
			ImageButton imgButton = (ImageButton) this.getCurrentFocus();

			int[] pos = searchButton(imgButton.getId());
			int row = pos[0];
			int col = pos[1];
			if (mineField.getCellState(row, col) == Cell.CellStates.PUSHED)
				mTts.speak(
						"Position: "
								+ row
								+ " "
								+ col
								+ " "
								+ " State "
								+ Cell.stateToString(mineField
										.getCellState(row, col)) + " "
								+ mineField.getCellValue(row, col),
						TextToSpeech.QUEUE_FLUSH, null);
			else {
				mTts.speak("Position: " + row + " " + col + " " + " State "
						+ Cell.stateToString(Cell.CellStates.NO_PUSHED),
						TextToSpeech.QUEUE_FLUSH, null);
			}
		}
		
		return true;
	}
	
	public void onBackPressed() {
		// when the back button is pressed, ask the user if they really want
		// to end their game
		AlertDialog backDialog = (new AlertDialog.Builder(this))
				// .setTitle("Game Over")
				.setMessage(R.string.confirm_quit)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Minesweeper.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		backDialog.show();
		mTtsAction(null, SPEECH_READ_CODE, "AlerDialog: Confirm quit?"
				+ getString(R.string.confirm_quit) + " " + "Yes" + " " + "No");
	}

	/**
	 * ------------------------------------------------------------ Musica
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
	 * Turns off TTS engine
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
