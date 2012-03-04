package com.accgames.tinyengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class MaskCircle  extends Mask{
	
	private int radius;
	
	public MaskCircle(int offsetX, int offsetY, int radius){
		super(offsetX,offsetY);
		this.radius = radius;
	}
	
	@Override
	public boolean isInMask(int x, int y) {
		return Math.sqrt((x-this.x)*(x-this.x) + (y-this.y)*(y-this.y)) <= radius;
	}
	public int getRadius(){return radius;}

	public int getCenterY() {
		return this.y;
	}

	public int getCenterX() {
		return this.x;
	}
	
	//  For debug
	public void onDraw(Canvas canvas){
		Paint brush = new Paint();
		brush.setColor(Color.GREEN);
		brush.setStyle(Style.STROKE);
		canvas.drawCircle(this.x, this.y, radius, brush);
	}
}
