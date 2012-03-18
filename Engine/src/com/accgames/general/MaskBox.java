package com.accgames.general;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * Mask with a rectangular shape.
 * 
 * @author Javier �lvarez & Gloria Pozuelo. 
 * 
 * @extends Mask.
 * 
 * */
public class MaskBox extends Mask{

	private int width,height; // width and height of the rectangle relatives to Mask x and Mask y
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param offsetX Offset within x relative to the entity coordinates.
	 * @param offsetY Offset within y relative to the entity coordinates.
	 * @param width rectangle width relatives to Mask x and Mask y.
	 * @param height rectangle height relatives to Mask x and Mask y.
	 * 
	 * */
	public MaskBox(int offsetX, int offsetY, int width, int height){
		super(offsetX, offsetY);
		this.width = width;
		this.height = height;
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------    
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
// ----------------------------------------------------------- Others -----------------------------------------------------------    	
	@Override
	public boolean isInMask(int x, int y) {
		return x >= this.x && x <= width+this.x && y >= this.y && y <= height+this.y;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		Paint brush = new Paint();
		brush.setColor(Color.GREEN);
		brush.setStyle(Style.STROKE);
		canvas.drawRect(this.x,this.y, this.x + width, this.y + height, brush);
	}
	
	public Object clone(Object o){
		MaskBox m1, m2;
		m1 = (MaskBox) o;
		m2 = new MaskBox(m1.offsetX, m1.offsetY, m1.width, m1.height);
		return m2;
	}
}
