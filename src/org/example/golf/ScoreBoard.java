package org.example.golf;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ScoreBoard extends Entity {

	private int counter;
	private int record;
	
	public ScoreBoard(int x, int y, Bitmap img, Game game, List<Mask> mask,
			boolean animated, int frameCount) {
		super(x, y, img, game, mask, animated, frameCount);
		counter = 0;
		record = 0;
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint brush = new Paint();
		brush.setColor(Color.BLUE);
		canvas.drawText(Integer.toString(counter), this.x + 25, this.y + 25,brush);
		brush.setColor(Color.LTGRAY);
		canvas.drawText(Integer.toString(record), this.x + 25, this.y + 40,brush);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();	
	}
	
	public void incrementCounter(){
		counter++;
		if(counter > record)
			record = counter;
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

}
