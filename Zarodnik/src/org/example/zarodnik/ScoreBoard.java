package org.example.zarodnik;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.example.R;
import org.example.activities.MainActivity;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ScoreBoard extends Entity {

	private ZarodnikGame game;
	
	private int counter;
	private int record;
	
	public ScoreBoard(int x, int y, int record,Bitmap img, Game game, List<Mask> mask,
			boolean animated, int frameCount) {
		super(x, y, img, game, mask, animated, frameCount);
		counter = 0;
		this.game = (ZarodnikGame) game;
		this.record = record;
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint brush = new Paint();
		brush.setColor(Color.BLUE);
		brush.setTextSize(30);
		canvas.drawText(Integer.toString(counter), this.x + getImgWidth()/3, this.y + getImgHeight()/3 + 30,brush);
		if(record >= 0){
			brush.setColor(Color.RED);
			canvas.drawText(Integer.toString(record), this.x + getImgWidth()/3, this.y + getImgHeight()/3,brush);
		}
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

}
