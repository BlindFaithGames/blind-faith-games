package org.example.golf;

import java.util.ArrayList;

import org.example.R;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskCircle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class GolfGame extends Game{
	
	public GolfGame(View v) {
		super(v);
		// Game entities: dot and target
		// Dot
		ArrayList<Mask> dotMasks = new ArrayList<Mask>();
		dotMasks.add(new MaskCircle(40, 40, 40));
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball);
		this.addEntity(new Dot(15,15, ballBitmap, this, dotMasks));
		// Target
		ArrayList<Mask> targetMasks = new ArrayList<Mask>();
		targetMasks.add(new MaskCircle(10, 10, 10));
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball);
		this.addEntity(new Target(15,15, targetBitmap, this, targetMasks));
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw background
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
}
