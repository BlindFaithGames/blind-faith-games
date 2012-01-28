package org.example.zarodnik;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.view.View;

public class ZarodnikGame extends Game {
	
	private static String prey_sound = "cat";
	
	
	public ZarodnikGame(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		int record;
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		textToSpeech.setInitialSpeech(this.context.getString(R.string.game_initial_TTStext));
		record = loadRecord();
		
		createEntities(record);
		
		// Set background image
		Bitmap field = BitmapFactory.decodeResource(v.getResources(), R.drawable.field);
		field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT, SCREEN_WIDTH);
		Paint brush = new Paint();
		//brush.setAlpha(175);
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
		// Game entities: predators and preys
		// TODO create predator
		Bitmap predatorBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.predator);
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(0,4*predatorBitmap.getHeight()/5,4*predatorBitmap.getWidth()/5,predatorBitmap.getHeight()/5));	
		Random positions = new Random();
		int ancho = SCREEN_WIDTH - predatorBitmap.getWidth();
		int alto = SCREEN_HEIGHT - predatorBitmap.getHeight();
		int targetX = positions.nextInt(ancho);
		int targetY = positions.nextInt(alto);
		this.addEntity(new Predator(targetX, targetY, predatorBitmap, this, targetMasks, 0, null, null));
		
		// Prey
		Bitmap preyBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.prey);
		ArrayList<Mask> preyMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(0,4*preyBitmap.getHeight()/5,4*preyBitmap.getWidth()/5,preyBitmap.getHeight()/5));	
		positions = new Random();
		int preyX = positions.nextInt(ancho);
		int preyY = positions.nextInt(alto);
		this.addEntity(new Prey(preyX, preyY, preyBitmap, this, preyMasks, 0, prey_sound, new Point(0,0)));
		
		// Player
		Bitmap playerBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.player);
		ArrayList<Mask> ballMasks = new ArrayList<Mask>();
		ballMasks.add(new MaskCircle(playerBitmap.getWidth()/(5*2),playerBitmap.getWidth()/(5*2),playerBitmap.getWidth()/(5*2)));     
		this.addEntity(new Dot(SCREEN_WIDTH / 2, SCREEN_HEIGHT - SCREEN_HEIGHT
				/ 3, record, playerBitmap, this, ballMasks, context, 0, null, null));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
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

}
