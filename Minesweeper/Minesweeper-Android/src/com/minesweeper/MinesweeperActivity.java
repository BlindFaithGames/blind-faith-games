/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.minesweeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.example.minesweeper.Input;
import org.example.minesweeper.Music;
import org.example.minesweeper.TTS;
import org.example.minesweeper.XML.KeyboardWriter;
import org.example.minesweeper.XML.XMLKeyboard;
import org.example.others.Entry;
import org.example.others.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.minesweeper.client.MyRequestFactory;
import com.minesweeper.client.MyRequestFactory.EntryRequest;
import com.minesweeper.client.MyRequestFactory.LogRequest;
import com.minesweeper.shared.EntryProxy;
import com.minesweeper.shared.LogProxy;

/**
 * Main activity - requests "Hello, World" messages from the server and provides
 * a menu item to invoke the accounts activity.
 */
public class MinesweeperActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {
	/**
	 * Tag for logging.
	 */
	private static final String TAG = "MainMenu";
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;

	public static final String KEY_TTS = "org.example.game.TTS";
	public static final String KEY_DIFFICULTY = "org.example.game.difficulty";

	private int difficult;
	private TTS textToSpeech;
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;

	// To know if the user has started a game or not
	private boolean gamed = false;

	private AsyncTask<Void, Void, String> task;

	// Screen size
	private int width;
	private int height;

	/**
	 * The current context.
	 */
	private Context mContext = this;

	/**
	 * A {@link BroadcastReceiver} to receive the response from a register or
	 * unregister request, and to update the UI.
	 */
	private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String accountName = intent
					.getStringExtra(DeviceRegistrar.ACCOUNT_NAME_EXTRA);
			int status = intent.getIntExtra(DeviceRegistrar.STATUS_EXTRA,
					DeviceRegistrar.ERROR_STATUS);
			String message = null;
			String connectionStatus = Util.DISCONNECTED;
			if (status == DeviceRegistrar.REGISTERED_STATUS) {
				message = getResources().getString(
						R.string.registration_succeeded);
				connectionStatus = Util.CONNECTED;
			} else if (status == DeviceRegistrar.UNREGISTERED_STATUS) {
				message = getResources().getString(
						R.string.unregistration_succeeded);
			} else {
				message = getResources().getString(R.string.registration_error);
			}

			// Set connection status
			SharedPreferences prefs = Util.getSharedPreferences(mContext);
			prefs.edit().putString(Util.CONNECTION_STATUS, connectionStatus)
					.commit();

			// Display a notification
			Util.generateNotification(mContext,
					String.format(message, accountName));
		}
	};

	/**
	 * Begins the activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Register a receiver to provide register/unregister notifications
		registerReceiver(mUpdateUIReceiver, new IntentFilter(
				Util.UPDATE_UI_INTENT));

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setScreenContent(R.layout.main);
	}

	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences prefs = Util.getSharedPreferences(mContext);
		String connectionStatus = prefs.getString(Util.CONNECTION_STATUS,
				Util.DISCONNECTED);
		if (Util.DISCONNECTED.equals(connectionStatus)) {
			startActivity(new Intent(this, AccountsActivity.class));
		}

		Music.play(this, R.raw.main);

		textToSpeech.setEnabled(PrefsActivity.getTTS(this));

		textToSpeech.speak(this.getString(R.string.main_menu_initial_TTStext));

		Log.getLog().addEntry(MinesweeperActivity.TAG,
				PrefsActivity.configurationToString(this), Log.NONE,
				Thread.currentThread().getStackTrace()[2].getMethodName(), "");
	}

	/**
	 * Shuts down the activity and turns off TTS engine
	 */
	@Override
	public void onDestroy() {
		unregisterReceiver(mUpdateUIReceiver);
		textToSpeech.stop();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
	}

	// Manage UI Screens

	/**
	 * Sets the screen content based on the screen id.
	 */
	private void setScreenContent(int screenId) {
		setContentView(screenId);
		switch (screenId) {
		case R.layout.main:
			setMainScreenContent();
			break;
		}
	}

	private void setMainScreenContent() {
		setContentView(R.layout.main);

		keyboard = Input.getInstance();
		this.fillXMLKeyboard();

		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		newButton.setOnFocusChangeListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		aboutButton.setOnFocusChangeListener(this);
		View instructionsButton = findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setOnFocusChangeListener(this);
		View formButton = findViewById(R.id.form_button);
		formButton.setOnClickListener(this);
		formButton.setOnFocusChangeListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		exitButton.setOnFocusChangeListener(this);

		checkFolderApp("minesweeper.xml");

		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, "Main Menu minesweeper "
				+ newButton.getContentDescription() + " "
				+ instructionsButton.getContentDescription() + " "
				+ aboutButton.getContentDescription() + " "
				+ formButton.getContentDescription() + " "
				+ exitButton.getContentDescription(), TTS.QUEUE_FLUSH);

		textToSpeech.setEnabled(PrefsActivity.getTTS(this));

		Display display = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();

		Log.getLog().setTag("Minesweeper");
		// Log.getLog().addEntry(MinesweeperActivity.TAG,PrefsActivity.configurationToString(this),
		// Log.DEVICE,Thread.currentThread().getStackTrace()[2].getMethodName(),
		// Build.DEVICE + " " + Build.MODEL + " " + Build.MANUFACTURER
		// + " " + Build.BRAND + " " + Build.HARDWARE + " " + width + " " +
		// height);
	}

	/**
	 * Default keyboard config
	 */
	private void fillXMLKeyboard() {
		keyboard.addObject(24, "zoom");
		keyboard.addObject(25, "exploration");
		keyboard.addObject(82, "instructions");
		keyboard.addObject(84, "coordinates");
		keyboard.addObject(5, "context");
		keyboard.setNum(5);
	}

	private void checkFolderApp(String file) {
		File f = new File(file);
		if (f == null || (!f.exists() && !f.mkdir())) {
			if (writer == null)
				writer = new KeyboardWriter();
			try {
				FileOutputStream fos = openFileOutput(file, 3);
				writer.saveEditedKeyboard(keyboard.getNum(),
						keyboard.getKeyList(), fos);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * onClick manager
	 */
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, AboutActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.instructions_button:
			// The user checks the instructions.
			Log.getLog().addEntry(MinesweeperActivity.TAG,
					PrefsActivity.configurationToString(this),
					Log.INSTRUCTIONS,
					Thread.currentThread().getStackTrace()[2].getMethodName(),
					"Instructions");
			openInstructionsDialog();
			break;
		case R.id.new_button:
			// Device information
			Log.getLog().addEntry(
					MinesweeperActivity.TAG,
					PrefsActivity.configurationToString(this),
					Log.DEVICE,
					Thread.currentThread().getStackTrace()[2].getMethodName(),
					Build.DEVICE + " " + Build.MODEL + " " + Build.MANUFACTURER
							+ " " + Build.BRAND + " " + Build.HARDWARE + " "
							+ width + " " + height);
			
			openNewGameDialog();
			break;
		case R.id.form_button:
			i = new Intent(this, FormActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.exit_button:
			// Si ya ha jugado una partida y sale, no aporta ninguna información
			if (!gamed) {
				Log.getLog().addEntry(
						MinesweeperActivity.TAG,
						PrefsActivity.configurationToString(this),
						Log.EXIT,
						Thread.currentThread().getStackTrace()[2]
								.getMethodName(), "Exit");
				sendLog();
			}
			finish();
			break;
		}
	}

	/**
	 * OnFocusChangeListener Interface
	 * */
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			textToSpeech.speak(v);
		}
	}

	/** Ask the user what difficulty level want */
	private void openNewGameDialog() {
		Builder newGameAlertDialogBuilder = new AlertDialog.Builder(this)
				.setTitle(R.string.new_game_title).setItems(R.array.difficulty,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startGame(i);
							}
						});
		AlertDialog newGameAlertDialog = newGameAlertDialogBuilder.create();
		newGameAlertDialog.show();
		ListView l = newGameAlertDialog.getListView();
		l.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView option = (TextView) view;
				textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				textToSpeech.speak((String) option.getText());
				textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		textToSpeech.speak(this
				.getString(R.string.alert_dialog_difficulty_TTStext)
				+ " "
				+ this.getString(R.string.easy_label)
				+ " "
				+ this.getString(R.string.medium_label)
				+ " "
				+ this.getString(R.string.hard_label));
	}

	/** Ask the user what type of instructions */
	private void openInstructionsDialog() {
		Builder instructionsAlertDialogBuilder = new AlertDialog.Builder(this)
				.setTitle(R.string.instructions_title).setItems(
						R.array.instructions,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startInstructions(i);
							}
						});
		AlertDialog instructionsAlertDialog = instructionsAlertDialogBuilder
				.create();
		instructionsAlertDialog.show();
		ListView l = instructionsAlertDialog.getListView();

		textToSpeech.speak(this
				.getString(R.string.alert_dialog_instructions_TTStext)
				+ this.getString(R.string.instructions_general_label)
				+ " "
				+ this.getString(R.string.instructions_controls_label));

		l.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView option = (TextView) view;
				textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				textToSpeech.speak((String) option.getText());
				textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/** Start a new game with the given difficulty level */
	private void startGame(int i) {
		gamed = true;
		Intent intent = new Intent(mContext, Minesweeper.class);
		intent.putExtra(KEY_TTS, textToSpeech);
		intent.putExtra(KEY_DIFFICULTY, i);
		difficult = i;
		startActivityForResult(intent, RESET_CODE);
	}

	/** Start an Instructions screen, with the option given: controls or general **/
	private void startInstructions(int i) {
		Intent intent;
		if (i == 0)
			intent = new Intent(mContext, InstructionsGeneralActivity.class);
		else
			intent = new Intent(mContext, InstructionsControlsActivity.class);

		intent.putExtra(KEY_TTS, textToSpeech);

		startActivity(intent);
	}

	/**
	 * Operates on the outcome of the game
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case (RESET_CODE):
			Intent nextIntent = new Intent(getApplicationContext(),
					Minesweeper.class);
			nextIntent.putExtra(KEY_TTS, textToSpeech);
			nextIntent.putExtra(KEY_DIFFICULTY, difficult);
			sendLog();
			startActivityForResult(nextIntent, RESET_CODE);
			break;
		case (EXIT_GAME_CODE):
			sendLog();
			break;
		case (RESULT_CANCELED):
			break;
		default:
			finish();
			break;
		}
	}

	private synchronized void sendLog() {
		// Use an AsyncTask to avoid blocking the UI thread
		task = new AsyncTask<Void, Void, String>() {
			private String message;

			@Override
			protected String doInBackground(Void... arg0) {
				MyRequestFactory factory = (MyRequestFactory) Util
						.getRequestFactory(mContext, MyRequestFactory.class);
				LogRequest request = factory.logRequest();

				LogProxy log = request.create(LogProxy.class);

				log.setTag(Log.getLog().getTag());
				log.setLogEntries(sendEntries());
				log.setFormAnswers(Log.getLog().getFormAnswers());
				log.setComment(Log.getLog().getComment());

				request.createLog(log).fire(new Receiver<LogProxy>() {
                    @Override
                    public void onFailure(ServerFailure error) {
                        message = "Failure: " + error.getMessage();
                        synchronized (Log.getLog()){
                        	Log.getLog().notify();
                        }
                    }
                    @Override
                    public void onSuccess(LogProxy l) {
                        message = "We have received your log succesfully";
                        synchronized (Log.getLog()){
                        	Log.getLog().notify();
                        }
                    }
                });
				return message;
			}

			@Override
            protected void onPostExecute(String result) {
				 Log.getLog().getLogEntries().clear();
            }
        }.execute();
        synchronized (Log.getLog()){
			try {
				Log.getLog().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}

	private List<Long> sendEntries() {
		MyRequestFactory factory = (MyRequestFactory) Util.getRequestFactory(
				mContext, MyRequestFactory.class);
		EntryRequest request;

		Set<Integer> entries = Log.getLog().getEntryKeys();
		Iterator<Integer> it = entries.iterator();
		Integer entryN;
		Entry e;
		EntryProxy entry;
		List<Long> result = new ArrayList<Long>();
		while (it.hasNext()) {
			entryN = it.next();
			e = Log.getLog().getEntry(entryN);

			request = factory.entryRequest();
			entry = request.create(EntryProxy.class);

			entry.setComment(e.getComment());
			entry.setPath(e.getPath());
			entry.setTag(e.getTag());
			entry.setType(e.getType());
			entry.setConfigurationSettings(e.getConfigurationSettings());
			entry.setTimestamp(e.getTimestamp());

			EntryReceiver r = new EntryReceiver();
			request.createEntry(entry).fire(r);
			Long n = new Long(r.getResult());
			result.add(n);
		}
		return result;
	}

	public boolean onMenuOpened(int featureId, Menu menu) {
		textToSpeech.speak(this
				.getString(R.string.accounts_menu_initial_TTStext));
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		textToSpeech.speak(this
				.getString(R.string.settings_menu_initial_TTStext));
		textToSpeech.speak(this
				.getString(R.string.key_configuration_menu_initial_TTStext));
		textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
		return true;
	}

	/**
	 * Manages what to do depending on the selected item
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.settings:
			i = new Intent(this, PrefsActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			return true;
		case R.id.keyConf:
			i = new Intent(this, KeyConfActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			return true;
		case R.id.account:
			i = new Intent(this, AccountsActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			return true;
		}
		return false;
	}
}