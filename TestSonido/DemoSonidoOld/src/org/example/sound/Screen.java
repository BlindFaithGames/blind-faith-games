package org.example.sound;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class Screen extends View{
	
	public Screen(Context c){
		super(c);
	}
	
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		canvas.drawColor(Color.BLUE);
		canvas.drawLine(canvas.getWidth()/2, 0, canvas.getWidth()/2, canvas.getHeight(), paint);	
	}
	
	
		
	
}
