package org.example.golf;

import java.util.ArrayList;
import java.util.List;

import org.example.R;
import org.example.tinyEngineClasses.Game;
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
		ArrayList<MaskCircle> dotMasks = new ArrayList<MaskCircle>();
		dotMasks.add(new MaskCircle(40, 40, 40));
		Bitmap ballBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball);
		this.addEntity(new Dot(15,15, ballBitmap, this, (List)dotMasks));
		// Target
		ArrayList<MaskCircle> targetMasks = new ArrayList<MaskCircle>();
		targetMasks.add(new MaskCircle(10, 10, 10));
		Bitmap targetBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ball);
		this.addEntity(new Dot(15,15, ballBitmap, this, (List)targetMasks));
	}

	/**Crea entidades propias del juego y demas, pinta fondo y... continuara*/
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onUpdate() {
		// TODO Auto-generated method stub
		
	}
	
}
