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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez
 * This class implements the logic game
 */

public class Game extends Activity implements OnClickListener, OnLongClickListener {
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
	private int coorX;
	private int coorY;
	private CellStates coorState;
	private ImageButton but;
	
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
		
		int difficult = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);

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
	 * Sets every ImageButton label
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
				blocks[row][column].setOnLongClickListener(this);
				blocks[row][column].setPadding(blockPadding, blockPadding,
						blockPadding, blockPadding);
				if (Coordinates.getCoordinates(this))
					blocks[row][column].setContentDescription("row "+row+ " column "+column+", no pushed  ");
				else blocks[row][column].setContentDescription("no pushed  ");
				tableRow.addView(blocks[row][column]);
			}
			uiBoard.addView(tableRow, new TableLayout.LayoutParams(
					(blockDimension + 2 * blockPadding) * colN, blockDimension
							+ 2 * blockPadding));
		}

	}

	/**
	 * onClick manager, focus change
	 */
	public void onClick(View v) {
		v.requestFocus(v.getId());
	}
	public boolean onLongClick(View v) {
		onClickMineFieldButton(v);
		return true;
	}
	
	/**
	 * Manage the game states changes when an event happens.
	 */
	private void onClickMineFieldButton(View v) {
		setCoor(v);
		openExploreDialog(v);
	}
	/**
	 * Gets image button position and other features, and sets them in global parameters
	 * @param view from onClick
	 */
	private void setCoor(View v){
		but = (ImageButton) v;

		int[] pos = searchButton(but.getId());
		coorX = pos[0];
		coorY = pos[1];
		
		coorState = mineField.getCellState(coorX, coorY);
	}
	/**
	 * Manages game states when exploration mode is chosen
	 */
	private void explore(){
		switch (coorState) {
		case FLAGGED:
				blocks[coorX][coorY].setImageBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.normalcell));
				mineField.setCellState(coorX, coorY,CellStates.NO_PUSHED);	
			break;
		case PUSHED: // if it's pushed it don't do nothing 
			break;
		default:
			blocks[coorX][coorY].setImageBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.cellflag));
				
			mineField.setCellState(coorX, coorY,CellStates.FLAGGED);	
			break;
		}
	}
	/**
	 * Manages game states when exploration mode is chosen
	 */
	private void dig(){
		switch (coorState) {
		case NO_PUSHED:
			expandCell(coorX, coorY, but);
			if (mineField.isFinished())
				endGame(FINAL_STATE.WIN);
			break;
		case PUSHED: // if it's pushed it don't do nothing 
			break;
		case MINE:
			for (int rows = 0; rows < rowN; rows++)
				for (int column = 0; column < colN; column++) {
					coorState = mineField.getCellState(rows, column);
					if (coorState.equals(CellStates.MINE))
					blocks[rows][column].setImageBitmap(BitmapFactory
							.decodeResource(getResources(),
									R.drawable.minecell));
				}
			endGame(FINAL_STATE.LOSE);
			break;
	}
	}
	
	/**
	 * Builds the dialog shown when a long click occurs, which gives the choice whether to explore or dig
	 */
	
	private void openExploreDialog(View v) {
		new AlertDialog.Builder(this)
				.setMessage("Choose an option")
				.setCancelable(false)
				.setPositiveButton("Explore",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								explore();
							}
						})
				.setNegativeButton("Dig",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dig();
							}
						}).show();
	}
	
	/**
	 * Builds the dialog shown at the end of the game, when the result is positive
	 */
	private void buildWinDialog() {
		winDialog = new AlertDialog.Builder(this)
				.setTitle("CONGRATULATIONS")
				.setMessage("You won!")
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
	 * Shows the number of mines close to the cell given by its coordinates and sets each ImageButton label
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param imgButton Button image
	 */
	private void showCellValue(int x, int y, ImageButton imgButton) {

		switch (mineField.getCellValue(x, y)) {
		case (0):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.pushedcell));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", no mines  ");
			else imgButton.setContentDescription("no mines  ");
			break;
		case (1):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell1));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 1 mine  ");
			else imgButton.setContentDescription("1 mines  ");
			break;
		case (2):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell2));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 2 mines  ");
			else imgButton.setContentDescription("2 mines  ");
			break;
		case (3):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell3));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 3 mines  ");
			else imgButton.setContentDescription("3 mines  ");
			break;
		case (4):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell4));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 4 mines  ");
			else imgButton.setContentDescription("4 mines  ");
			break;
		case (5):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell5));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 5 mines  ");
			else imgButton.setContentDescription("5 mines  ");
			break;
		case (6):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell6)); 
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 6 mines  ");
			else imgButton.setContentDescription("6 mines  ");
			break;
		case (7):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell7));
			if (Coordinates.getCoordinates(this))
					imgButton.setContentDescription("row "+x+ " column "+y+", 7 mines  ");
			else imgButton.setContentDescription("7 mines  ");
			break;
		case (8):
			imgButton.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.cell8));
			if (Coordinates.getCoordinates(this))
				imgButton.setContentDescription("row "+x+ " column "+y+", 8 mines  ");
			else imgButton.setContentDescription("8 mines  ");
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