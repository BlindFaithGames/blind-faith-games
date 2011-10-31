package org.example.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez
 * This class implements the main activity
 */

public class Minesweeper extends Activity implements OnClickListener {
	private static final String TAG = "Minesweeper";
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;
	
	private int difficult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		View instructionsButton = findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);

		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}

	/**
	 * onClick manager
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.instructions_button:
			Intent j = new Intent(this, Instructions.class);
			startActivity(j);
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}

	/** Ask the user what difficulty level they want */
	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.new_game_title)
				.setItems(R.array.difficulty,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startGame(i);
							}
						}).show();
	}

	/** Start a new game with the given difficulty level */
	private void startGame(int i) {
		Log.d(TAG, "clicked on " + i);
		Intent intent = new Intent(Minesweeper.this, Game.class);
		intent.putExtra(Game.KEY_DIFFICULTY, i);
		difficult = i;
		startActivityForResult(intent, RESET_CODE);
	}

	/**
	 * Operates on the outcome of the game
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case (RESET_CODE):
			Intent nextIntent = new Intent(getApplicationContext(), Game.class);
			nextIntent.putExtra(Game.KEY_DIFFICULTY, difficult);
			startActivityForResult(nextIntent, RESET_CODE);
			break;
		case (EXIT_GAME_CODE): 
			break;
		case (RESULT_CANCELED): 
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * It creates the options menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/**
	 * Manages what to do depending on the selected item
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
			// More items go here (if any) ...
		}
		return false;
	}

/**
 * ------------------------------------------------------------ 
 * M�sica
 * ---------------------------------------------------------------
 */
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.main);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
}