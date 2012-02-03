package org.example.golf;

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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class ScoreBoard extends Entity {

	private GolfGame game;
	
	private int counter;
	private int record;
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	private Paint brush;
	
	public ScoreBoard(int x, int y, int record,Bitmap img, Game game, List<Mask> mask,
			boolean animated, int frameCount) {
		super(x, y, img, game, mask, animated, frameCount);
		counter = 0;
		this.game = (GolfGame) game;
		this.record = record;
		
		font = Typeface.createFromAsset(this.game.getContext().getAssets(), RuntimeConfig.FONT_PATH);  
		scale = this.game.getContext().getResources().getDisplayMetrics().density;
		fontSize =  (this.game.getContext().getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		
		brush = new Paint();
		
		brush.setTextSize(fontSize);
		brush.setTypeface(font);
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		brush.setARGB(255, 51, 51, 51);
		canvas.drawRect(new Rect(this.x ,(int) (this.y) , (int) (this.x + (int) 2 * fontSize), (int) (this.y + fontSize * 2.3)), brush);
		
		if(this.game.isStageMode()){
			brush.setColor(Color.BLUE);
			canvas.drawText(Integer.toString(counter), this.x, (float) (this.y + fontSize * 1.5), brush);
		}
		else{
			brush.setColor(Color.BLUE);
			canvas.drawText(Integer.toString(counter), this.x, this.y + fontSize * 2, brush);
			
			if(record >= 0){
				brush.setColor(Color.RED);
				canvas.drawText(Integer.toString(record), this.x, this.y + fontSize,brush);
			}
		}
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("speakRecord");
		if(e != null){
			if(this.game.isStageMode()){
			this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.counterSpeech)
									+ " " + counter);
			}
			else{
				this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.recordSpeech)
						+ " " + record );
				this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.counterSpeech)
						+ " " + counter);
			}
		}
	}
	
	public void incrementCounter(){
		counter++;
		if(counter > record){
			record = counter;
			if(!game.isStageMode())
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
