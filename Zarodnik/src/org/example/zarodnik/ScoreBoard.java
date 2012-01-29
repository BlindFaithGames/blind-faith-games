package org.example.zarodnik;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.example.R;
import org.example.activities.MainActivity;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

public class ScoreBoard extends Entity {

	private ZarodnikGame game;
	
	private int counter;
	private int record;
	
	private int fontSize;
	private Typeface font;
	private Paint brush;
	
	public ScoreBoard(int x, int y, int record,Bitmap img, Game game, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, animations, null, null);
		counter = 0;
		this.game = (ZarodnikGame) game;
		this.record = record;
		
		fontSize = RuntimeConfig.FONT_SIZE;
		
		font = Typeface.createFromAsset(this.game.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		
		brush = new Paint();
		brush.setTextSize(fontSize);
		if(font != null)
			brush.setTypeface(font);
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		brush.setARGB(255, 51, 51, 51);
		canvas.drawRect(new Rect(0,0,Game.SCREEN_WIDTH,fontSize + 10), brush);
		
		if(record >= 0){
			brush.setARGB(255, 51, 153, 255);
			canvas.drawText(Integer.toString(record), this.x, this.y, brush);
		}
		
		brush.setARGB(255, 0, 51, 101);
		canvas.drawText(Integer.toString(counter), this.x + fontSize*2, this.y, brush);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("speakRecord");
		if(e != null){
			this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.recordSpeech)
					+ " " + record );
			this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.counterSpeech)
					+ " " + counter);
			
		}
	}
	
	public void incrementCounter(){
		counter++;
		if(counter > record){
			record = counter;
			save();
			this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.newRecordSpeech) + counter);
		}
	}
	
	private void save() {
		FileOutputStream fos;
		try { 
			fos = this.game.getContext().openFileOutput(MainActivity.FILENAMEFREEMODE, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(record); 
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetCounter(){
		counter = 0;
	}
	
	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	public int getRecord() {
		return record;
	}

	public int getCounter() {
		return counter;
	}
	
	public void incrementCounter(int i) {
		counter += i;
	}

	public void decrementCounter(int i) {
		counter -= i;
	}

	@Override
	public void onRemove() {}

}
