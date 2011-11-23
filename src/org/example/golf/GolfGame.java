package org.example.golf;

import java.util.ArrayList;

import org.example.R;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskCircle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GolfGame extends Game{
	
	public GolfGame(View v) {
		super(v);
		// Game entities: dot and target
		// Dot
		ArrayList<Mask> dotMasks = new ArrayList<Mask>();
		dotMasks.add(new MaskCircle(40, 40, 1));                              // ajustar esto bien
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball);
		this.addEntity(new Dot(150,300, ballBitmap, this, dotMasks));
		// Target
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskCircle(10, 10, 10));			// ajustar esto bien (x,y)  y m�scaras...
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.hole);
		this.addEntity(new Target(150,0, targetBitmap, this, targetMasks));
	}

	@Override
	public void onDraw(Canvas canvas) {
		// Draw background
		Paint brush = new Paint();
		brush.setColor(Color.GREEN);
		canvas.drawRect(0, 0, v.getWidth(), v.getHeight(), brush);
		
		super.onDraw(canvas);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
}