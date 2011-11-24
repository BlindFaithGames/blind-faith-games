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
import android.view.View;

public class GolfGame extends Game{
	
	public GolfGame(View v) {
		super(v);
		// Game entities: dot and target
		// Dot
		ArrayList<Mask> dotMasks = new ArrayList<Mask>();
		dotMasks.add(new MaskCircle(25,25,25));                           
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.explosion);
		this.addEntity(new Dot(150,500, ballBitmap, this, dotMasks));
		// Target
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskBox(0,0,60,60));			
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