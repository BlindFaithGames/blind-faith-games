package org.example.golf;

import java.util.ArrayList;
import java.util.Random;

import org.example.R;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskBox;
import org.example.tinyEngineClasses.MaskCircle;
import org.example.tinyEngineClasses.Music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class GolfGame extends Game {

	public GolfGame(View v, Context c) {
		super(v,c);
		
		// Game entities: ball and target
		// Target
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.hole);
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(0,0,targetBitmap.getWidth(),targetBitmap.getHeight()));	
		Random positions = new Random();
		int ancho = 300 - targetBitmap.getWidth();
		int targetX = positions.nextInt(ancho);
		this.addEntity(new Target(targetX, 0, targetBitmap, this, targetMasks));
		
		// Ball
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball1);
		ArrayList<Mask> ballMasks = new ArrayList<Mask>();
		ballMasks.add(new MaskCircle(65/2,65/2,65/2));                           
		this.addEntity(new Dot(200,500, ballBitmap, this, ballMasks, new Point(targetX,0)));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		// Draw background
		// Draw grass
		Bitmap grass = BitmapFactory.decodeResource(v.getResources(),R.drawable.field);
	    int ancho = (v.getWidth() / grass.getWidth()) + 1;
	    int alto = ((v.getHeight()-(v.getHeight()/3)) / grass.getHeight()) + 1;
	    for(int i = 0; i < ancho; i++){
            for(int j = 0; j < alto; j++){
            		canvas.drawBitmap(grass, grass.getWidth()*i, grass.getHeight()*j, null);
            }
	     }
	    // Draw concrete
		Bitmap ground = BitmapFactory.decodeResource(v.getResources(),R.drawable.ground);
		int initialRow = alto;
	    ancho = (v.getWidth() / ground.getWidth()) + 1;
	    alto = (v.getHeight() / ground.getHeight()) + 1;
	    for(int i=0;i<ancho;i++){
            for(int j=initialRow;j<alto;j++){
            		canvas.drawBitmap(ground, ground.getWidth()*i, ground.getHeight()*j, null);
            }
	     }
		super.onDraw(canvas);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().getEvent("onScroll");
		if(e != null){

		}
	}
	
}