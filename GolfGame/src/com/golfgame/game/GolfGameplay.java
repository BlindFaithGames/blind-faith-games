package com.golfgame.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import com.accgames.general.Game;
import com.accgames.general.GameState;
import com.accgames.general.Mask;
import com.accgames.general.MaskBox;
import com.accgames.general.MaskCircle;
import com.accgames.graphics.CustomBitmap;
import com.accgames.input.Input;
import com.accgames.input.Input.EventType;
import com.accgames.sound.TTS;
import com.accgames.sound.VolumeManager;
import com.golfgame.R;
import com.golfgame.activities.MainActivity;
import com.golfgame.activities.SettingsActivity;

public class GolfGameplay extends GameState implements OnCancelListener {

	public enum steps {
		STEP0, STEP1, STEP2, STEP3, STEP4, STEP5, STEP6, STEP7, STEP8, STEP9
	};

	public static steps state;
	private static final int MAX_STAGES = 19;
	private boolean stageMode; // is in stage mode?
	private boolean tutorial; // is game tutorial?
	private int stage; // stage number

	private Random numberGenerator;

	private Dialog step[] = new Dialog[10];

	private TTS textToSpeech;
	private Resources res;
	private boolean onUp, infoTarget, vibration, soundFeedback, doppler;
	private static Handler handler;

	public GolfGameplay(int mode, View v, TTS textToSpeech, Context c, Game game) {
		super(v, c, textToSpeech, game);

		int record;

		stage = 1;
		stageMode = (mode == 0);

		tutorial = (mode == 2);

		if (!stageMode) {
			textToSpeech.setInitialSpeech(this.context
					.getString(R.string.game_initial_TTStext));
			record = loadRecord();
		} else {
			textToSpeech.setInitialSpeech(this.context
					.getString(R.string.game_initial_stage_TTStext));
			record = -1;
		}
		if (tutorial) {
			stageMode = false;
			res = this.getContext().getResources();

			state = steps.STEP0;
			handler = new Handler();
			createDialogs(c);
			step[0].show();
			textToSpeech.setInitialSpeech(res.getString(R.string.tutorial_step0_dialog_select));
			textToSpeech.disableTranscription();
			record = -1;
		}

		createEntities(record);

		this.textToSpeech = textToSpeech;

		// Set background image
		numberGenerator = new Random();
		Bitmap field;
		int img = 0;
		switch (numberGenerator.nextInt(4)) {
		case 0:
			img = R.drawable.campogolf;
			break;
		case 1:
			img = R.drawable.campogolfluna;
			break;
		case 2:
			img = R.drawable.campogolfsubmarino;
			break;
		case 3:
			img = R.drawable.campogolfnoche;
			break;
		}
		field = BitmapFactory.decodeResource(v.getResources(), img);
		field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT,
				SCREEN_WIDTH);
		Paint brush = new Paint();
		brush.setAlpha(175);
		editBackground(brush);
		setBackground(field);
	}

	public boolean isTutorialMode() {
		return tutorial;
	}

	public steps getStep() {
		return state;
	}

	private void createDialogs(Context c) {
		for (int i = 0; i < step.length; i++) {
			step[i] = new Dialog(c);
			step[i].requestWindowFeature(Window.FEATURE_NO_TITLE);
			switch (i) {
			case 0: step[i].setContentView(R.layout.tutorial_step0);
				break;
			case 1: step[i].setContentView(R.layout.tutorial_step1);
				break;
			case 2: step[i].setContentView(R.layout.tutorial_step2);
				break;
			case 3: step[i].setContentView(R.layout.tutorial_step3);
				break;
			case 4: step[i].setContentView(R.layout.tutorial_step4);
				break;
			case 5: step[i].setContentView(R.layout.tutorial_step5);
				break;
			case 6: step[i].setContentView(R.layout.tutorial_step6);
				break;
			case 7: step[i].setContentView(R.layout.tutorial_step7);
				break;
			case 8: step[i].setContentView(R.layout.tutorial_step8);
				break;
			case 9: step[i].setContentView(R.layout.tutorial_step9);
				break;
			}
			step[i].setCanceledOnTouchOutside(true);
			step[i].setOnCancelListener(this);
		}
	}

	/**
	 * Loads from internal file system the previous record in Free Mode.
	 * 
	 * */
	private int loadRecord() {
		FileInputStream fis;
		int record = -1;
		try {
			fis = this.getContext()
					.openFileInput(MainActivity.FILENAMEFREEMODE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object f = ois.readObject();
			record = (Integer) f;
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (record == -1)
			return 0;
		else
			return record;
	}

	/**
	 * Instantiates the entities in the game.
	 * 
	 * */
	private void createEntities(int record) {
		// Game entities: ball and target
		// Target
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(),
				R.drawable.hole);
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(0, 4 * targetBitmap.getHeight() / 5,
				4 * targetBitmap.getWidth() / 5, targetBitmap.getHeight() / 5));
		Random positions = new Random();
		int width = SCREEN_WIDTH - targetBitmap.getWidth();
		int targetX = positions.nextInt(width);
		int targetY = SCREEN_HEIGHT / 10;
		this.addEntity(new Target(targetX, targetY, targetBitmap, this,
				targetMasks));

		// Ball
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(),
				R.drawable.golf_ball);
		ArrayList<Mask> ballMasks = new ArrayList<Mask>();
		ballMasks.add(new MaskCircle(ballBitmap.getWidth() / 2, ballBitmap
				.getWidth() / 2, ballBitmap.getWidth() / 2));
		this.addEntity(new Dot(SCREEN_WIDTH / 2, SCREEN_HEIGHT - SCREEN_HEIGHT
				/ 3, record, ballBitmap, this, ballMasks, new Point(targetX + 2
				* targetBitmap.getWidth() / 5, targetY + 4
				* targetBitmap.getWidth() / 5), context));
	}

	@Override
	public void onDraw(Canvas canvas) {
		Paint brush = new Paint();
		brush.setARGB(250, 210, 180, 140);
		canvas.drawRect(0, GameState.SCREEN_HEIGHT - GameState.SCREEN_HEIGHT
				/ 3, GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT - 5, brush);

		if (stageMode) {
			brush = new Paint();
			brush.setTextSize(30);
			canvas.drawText(Integer.toString(stage), SCREEN_WIDTH / 2, 30,
					brush);
		}

		super.onDraw(canvas);
	}

	/**
	 * Checks if y is in the shot area. It's supposed that x is out of there
	 * 
	 * */
	public boolean inShotArea(float y) {
		int height = getView().getHeight();
		return (y < height - 5) && (y > (height - height / 3));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onVolUp");
		if (e != null) {
			VolumeManager.adjustStreamVolume(this.context,
					AudioManager.ADJUST_RAISE);
		} else {
			e = Input.getInput().removeEvent("onVolDown");
			if (e != null)
				VolumeManager.adjustStreamVolume(this.context,
						AudioManager.ADJUST_LOWER);
		}
		if (tutorial) {
			e = Input.getInput().getEvent("onDown");
			if (e != null && state == steps.STEP2) {
				Input.getInput().remove("onDown");
				this.nextState();
			}
		}
	}

	public void nextStage(int actualRecord) {
		if (stageMode) {
			stage++;
			if (stage == MAX_STAGES) {
				Intent i = new Intent();
				i.putExtra(MainActivity.KEY_RESULTS,
						String.valueOf(actualRecord));
				this.context.setResult(MainActivity.EXIT_GAME_CODE, i);
				this.context.finish();
			} else {
				this.getTTS().speak(
						this.getContext().getString(R.string.stage_speech)
								+ " " + stage);
				// Change background image
				// Set background image
				Bitmap field;
				int img = 0;
				switch (numberGenerator.nextInt(4)) {
				case 0:
					img = R.drawable.campogolf;
					break;
				case 1:
					img = R.drawable.campogolfluna;
					break;
				case 2:
					img = R.drawable.campogolfsubmarino;
					break;
				case 3:
					img = R.drawable.campogolfnoche;
					break;
				}
				field = BitmapFactory.decodeResource(v.getResources(), img);
				field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT,
						SCREEN_WIDTH);
				Paint brush = new Paint();
				brush.setAlpha(175);
				editBackground(brush);
				setBackground(field);
			}
		}
	}

	public boolean isStageMode() {
		return stageMode;
	}

	public void nextState() {
		switch (state) {
		case STEP0: 
			state = steps.STEP1;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[0].dismiss();
					step[1].show();
					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step1_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP1:
			state = steps.STEP2;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[1].dismiss();
					step[2].show();
					if (SettingsActivity.getOnUp(GolfGameplay.this.getContext())){
						SettingsActivity.setOnUp(false);
						onUp = true;
					} else onUp = false;
					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step2_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP2:
			state = steps.STEP3;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[2].dismiss();
					step[3].show();
					if (!SettingsActivity.getOnUp(GolfGameplay.this.getContext())){
						SettingsActivity.setOnUp(true);
					} 
					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step3_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP3:
			state = steps.STEP4;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[3].dismiss();
					step[4].show();
					SettingsActivity.setOnUp(onUp);		
					if (!SettingsActivity.getNotifyTarget(GolfGameplay.this.getContext())){
						SettingsActivity.setInfoTarget(true);
						infoTarget = false;
					} else infoTarget = true;
					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step4_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP4:
			state = steps.STEP5;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[4].dismiss();
					step[5].show();
					SettingsActivity.setInfoTarget(infoTarget);
					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step5_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP5:
			state = steps.STEP6;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[5].dismiss();
					step[6].show();
					if (!SettingsActivity.getVibrationFeedback(GolfGameplay.this.getContext())){
						SettingsActivity.setVibration(true);
						vibration = false;
					} else vibration = true;

					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step6_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP6:
			state = steps.STEP7;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[6].dismiss();
					step[7].show();
					SettingsActivity.setVibration(vibration);
					if (!SettingsActivity.getSoundFeedBack(GolfGameplay.this.getContext())){
						SettingsActivity.setSoundFeedback(true);
						soundFeedback = false;
					} else soundFeedback = true;

					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step7_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP7:
			state = steps.STEP8;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[7].dismiss();
					step[8].show();
					SettingsActivity.setSoundFeedback(soundFeedback);
					if (!SettingsActivity.getDopplerEffect(GolfGameplay.this.getContext())){
						SettingsActivity.setDoppler(true);
						doppler = false;
					} else doppler = true;

					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step8_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		case STEP8:
			state = steps.STEP9;
			handler.post(new Runnable() {
				@Override
				public void run() {
					step[8].dismiss();
					step[9].show();
					SettingsActivity.setDoppler(doppler);
					textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
					textToSpeech.speak(res.getString(R.string.tutorial_step9_dialog_select));
					textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				}
			});
			break;
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (dialog.equals(step[0]) || dialog.equals(step[2]) || dialog.equals(step[5])) {
			this.nextState();
		} else if (dialog.equals(step[9])) {
			this.context.finish();
		}
	}

}