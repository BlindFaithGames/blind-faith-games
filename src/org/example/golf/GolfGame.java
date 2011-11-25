package org.example.golf;

import java.util.ArrayList;

import org.example.R;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskBox;
import org.example.tinyEngineClasses.MaskCircle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class GolfGame extends Game {

	private static final int NCOL = 4;

	public GolfGame(View v) {
		super(v);
		// Game entities: dot and target
		// Dot
		ArrayList<Mask> dotMasks = new ArrayList<Mask>();
		dotMasks.add(new MaskCircle(65,65,65));                           
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball1);
		this.addEntity(new Dot(200,500, ballBitmap, this, dotMasks));
		// Target
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(-150,0,500,60));			
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.hole);
		this.addEntity(new Target(200,0, targetBitmap, this, targetMasks));
	}
	@Override
	public void onDraw(Canvas canvas) {
		// Draw background
		Paint brush = new Paint();	 

		//canvas.drawRect(new Rect(0,v.getHeight()-v.getHeight()/3,v.getWidth(),v.getHeight()),brush);
		//brush.setColor(Color.rgb(135, 206, 250));
		//canvas.drawRect(new Rect(0,0,v.getWidth(),v.getHeight()-v.getHeight()/3),brush);

		Bitmap grass = BitmapFactory.decodeResource(v.getResources(),R.drawable.field);
	    float ancho = (v.getWidth() / grass.getWidth()) + 1;
	    float alto = (v.getHeight() / grass.getHeight()) + 1;
	    for(int i = 0; i < ancho; i++){
            for(int j = 0; j < alto; j++){
            		canvas.drawBitmap(grass, grass.getWidth()*i, grass.getHeight()*j, null);
            }
	     }
	    
		brush.setColor(Color.rgb(50, 205, 50));
		canvas.drawLine(0, v.getHeight()-v.getHeight()/3, v.getWidth(), v.getHeight()-v.getHeight()/3, brush);
		
	    
		super.onDraw(canvas);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

}