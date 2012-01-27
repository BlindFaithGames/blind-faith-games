package org.example.golf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import org.example.R;
import org.example.activities.MainActivity;
import org.example.tinyEngineClasses.CustomBitmap;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskBox;
import org.example.tinyEngineClasses.MaskCircle;
import org.example.tinyEngineClasses.TTS;
import org.example.tinyEngineClasses.VolumeManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.view.View;

public class GolfGame extends Game {

	private static final int MAX_STAGES = 19;
	private boolean stageMode; // is in stage mode?
	private int stage; // stage number
	
	private Random numberGenerator;
	
	public GolfGame(int mode, View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		int record;
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		stage = 1;
		stageMode = (mode == 0);
		
		if(!stageMode){
			textToSpeech.setInitialSpeech(this.context.getString(R.string.game_initial_TTStext));
			record = loadRecord();
		}
		else{
			textToSpeech.setInitialSpeech(this.context.getString(R.string.game_initial_stage_TTStext));
			record = -1;
		}
		
		createEntities(record);
		
		// Set background image
		numberGenerator = new Random();
		Bitmap field;
		int img = 0;
		switch (numberGenerator.nextInt(4)){
		case 0:	 img = R.drawable.campogolf;
			break;
		case 1: img = R.drawable.campogolfluna;
			break;
		case 2: img = R.drawable.campogolfsubmarino;
			break;
		case 3: img =  R.drawable.campogolfnoche;
			break;
		}
		field = BitmapFactory.decodeResource(v.getResources(), img);
		field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT, SCREEN_WIDTH);
		Paint brush = new Paint();
		brush.setAlpha(175);
		editBackground(brush);
		setBackground(field);
	}
	
	/**
	 * Loads from internal file system the previous record in Free Mode.
	 * 
	 * */
	private int loadRecord() {
		FileInputStream fis;
		int record = -1;
		try { 
			fis = this.getContext().openFileInput(MainActivity.FILENAMEFREEMODE);
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
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.hoyo);
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(0,4*targetBitmap.getHeight()/5,4*targetBitmap.getWidth()/5	,targetBitmap.getHeight()/5));	
		Random positions = new Random();
		int ancho = SCREEN_WIDTH - targetBitmap.getWidth();
		int targetX = positions.nextInt(ancho);
		int targetY = SCREEN_HEIGHT * 5/100;
		this.addEntity(new Target(targetX, targetY, targetBitmap, this, targetMasks));
		
		// Ball
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.pelotagolf2);
		ArrayList<Mask> ballMasks = new ArrayList<Mask>();
		ballMasks.add(new MaskCircle(ballBitmap.getWidth()/(5*2),ballBitmap.getWidth()/(5*2),ballBitmap.getWidth()/(5*2)));     
		this.addEntity(new Dot(SCREEN_WIDTH / 2, SCREEN_HEIGHT - SCREEN_HEIGHT
				/ 3, record, ballBitmap, this, ballMasks, new Point(targetX
				+ targetBitmap.getWidth() / 2, targetY
				+ targetBitmap.getHeight()),context));
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(stageMode){
			Paint brush = new Paint();
			brush.setTextSize(30);
			canvas.drawText(Integer.toString(stage), SCREEN_WIDTH/2, 30, brush);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onVolUp");
		if (e != null){
			VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_RAISE);
		}else{
			e = Input.getInput().removeEvent("onVolDown");
			if (e != null)
				VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_LOWER);
		}
		
	}
	
	public void nextStage(int actualRecord){
		if(stageMode){
			stage++;
			if(stage == MAX_STAGES){
				Intent i = new Intent();
				i.putExtra(MainActivity.KEY_RESULTS, String.valueOf(actualRecord));
				this.context.setResult(MainActivity.EXIT_GAME_CODE, i);
				this.context.finish();
			}
			else{
				this.getTTS().speak("Stage number" + stage);
				// Change background image
				// Set background image
				Bitmap field;
				int img = 0;
				switch (numberGenerator.nextInt(4)){
				case 0:	 img = R.drawable.campogolf;
					break;
				case 1: img = R.drawable.campogolfluna;
					break;
				case 2: img = R.drawable.campogolfsubmarino;
					break;
				case 3: img =  R.drawable.campogolfnoche;
					break;
				}
				field = BitmapFactory.decodeResource(v.getResources(), img);
				field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT, SCREEN_WIDTH);
				Paint brush = new Paint();
				brush.setAlpha(175);
				editBackground(brush);
				setBackground(field);
			}
		}
	}
	
	public boolean isStageMode(){
		return stageMode;
	}
	
	public int getStage(){
		return stage;
	}
}