package org.example.minesweeper;

import org.example.minesweeper.Cell.CellStates;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez
 * This class implements the logic game
 */

public class Game extends Activity implements OnClickListener {
	private static final String TAG = "Minesweeper";

	public static final String KEY_DIFFICULTY = "org.example.minesweeper.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private TableLayout uiBoard;
	private ImageButton blocks[][];
	private AlertDialog endingDialog, winDialog;
	private Chronometer chrono;

	private Board mineField;
	private int rowN;
	private int colN;

	private boolean flagMode;

	/* Game states */
	public enum FINAL_STATE {
		WIN, LOSE
	};

	private Runnable RunnableGameOver = new Runnable() {
		public void run() {
			endingDialog.show();
		}
	};

	private Runnable RunnableWin = new Runnable() {
		public void run() {
			winDialog.show();
		}
	};

	/**
	 * Ends the game depending on the game state
	 * @param state Game's state
	 */
	private void endGame(FINAL_STATE state) {
		chrono.stop();
		Handler myHandler = new Handler();
		if (state == FINAL_STATE.LOSE)
			myHandler.postDelayed(RunnableGameOver, 0);
		if (state == FINAL_STATE.WIN)
			myHandler.postDelayed(RunnableWin, 0);

	}

	/**
	 * Game creation
	 */
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate"); // Debug
		super.onCreate(savedInstanceState);
		setContentView(R.layout.minefield);
		
		
		View flagButton = findViewById(R.id.flagButton);
		flagButton.setOnClickListener(this);
		
		int difficult = getIntent()
				.getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);

		// Start game
		mineField = new Board(difficult);
		rowN = mineField.getNRow();
		colN = mineField.getNCol();

		initializeUI();

		chrono.start();
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
	 * Builds the minefield, allocating the mines and the number of mines through the board game
	 */
	private void buildMineField() {
		uiBoard = (TableLayout) findViewById(R.id.tableLayout1);
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
				//blocks[row][column].setBackgroundColor(Color.rgb(32, 178, 170)); 

				blocks[row][column].setOnClickListener(this);
				blocks[row][column].setPadding(blockPadding, blockPadding,
						blockPadding, blockPadding);
				blocks[row][column].setContentDescription("row "+row+ " column "+column+", no pushed  ");
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
		switch (v.getId()) {
		case R.id.flagButton:
			ToggleButton flagButton = (ToggleButton) v;
			flagMode = flagButton.isChecked();	
			break;
		default: // By default, a minefield ImageButton is expected 
			onClickMineFieldButton(v);
			break;
		}
	}
	
	/**
	 * Manage the game states changes when an event happens.
	 */
	private void onClickMineFieldButton(View v) {
		ImageButton imgButton = (ImageButton) v;

		int[] pos = searchButton(imgButton.getId());
		int row = pos[0];
		int col = pos[1];

		CellStates state = mineField.getCellState(row, col);		
		
		if(flagMode){
			switch (state) {
				case FLAGGED:
						blocks[row][col].setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.normalcell));
						mineField.setCellState(row, col,CellStates.NO_PUSHED);	
					break;
				case PUSHED: // if it's pushed it don't do nothing 
					break;
				default:
					blocks[row][col].setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.cellflag));
						
					mineField.setCellState(row, col,CellStates.FLAGGED);	
					break;
			}
		}else{
			switch (state) {
			
				case NO_PUSHED:
					expandCell(row, col, imgButton);
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
					endGame(FINAL_STATE.LOSE);
					break;
			}
		}
		
	}

	/**
	 * Builds the dialog shown at the end of the game, when the result is positive
	 */
	private void buildWinDialog() {
		winDialog = new AlertDialog.Builder(this)
				.setTitle("CONGRATULATIONS")
				.setMessage("This time you win!")
				.setIcon(R.drawable.win)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setResult(Minesweeper.EXIT_GAME_CODE);
								Game.this.finish();
							}
						}).create();

	}

	/**
	 * Builds the dialog shown at the end of the game, when the result is negative
	 */
	private void buildEndingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Game Over")
				.setCancelable(false)
				.setPositiveButton("Reset",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setResult(Minesweeper.RESET_CODE);
								Game.this.finish();
							}
						})
				.setNegativeButton("Back to the main menu",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setResult(Minesweeper.EXIT_GAME_CODE);
								Game.this.finish();
							}
						});
		endingDialog = builder.create();
	}

	/**
	 * Returns the button coordinates given by the id
	 * @param id Button identifier
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
	 * @param row Button row
	 * @param col Button column
	 * @param imgButton The button image
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
			// TODO: a�adir diagonales xD
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
				expandCell(row - 1, col - 1, blocks[row - 1][col -1]);
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
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param imgButton Button image
	 */
	private void showCellValue(int x, int y, ImageButton imgButton) {

		switch (mineField.getCellValue(x, y)) {
		case (0):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			imgButton.setContentDescription("row "+x+ " column "+y+", no mines  ");
			break;
		case (1):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell1));
			imgButton.setContentDescription("row "+x+ " column "+y+", 1 mines  ");
			break;
		case (2):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell2));
			imgButton.setContentDescription("row "+x+ " column "+y+", 2 mines  ");
			break;
		case (3):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell3));
			imgButton.setContentDescription("row "+x+ " column "+y+", 3 mines  ");
			break;
		case (4):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			imgButton.setContentDescription("row "+x+ " column "+y+", 4 mines  ");
			break;
		case (5):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			imgButton.setContentDescription("row "+x+ " column "+y+", 5 mines  ");
			break;
		case (6):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			imgButton.setContentDescription("row "+x+ " column "+y+", 6 mines  ");
			break;
		case (7):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			imgButton.setContentDescription("row "+x+ " column "+y+", 7 mines  ");
			break;
		case (8):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			imgButton.setContentDescription("row "+x+ " column "+y+", 8 mines  ");
			break;
		}

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
}
