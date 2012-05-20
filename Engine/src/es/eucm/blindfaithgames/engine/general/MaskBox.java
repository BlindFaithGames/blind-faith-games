package es.eucm.blindfaithgames.engine.general;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;

/**
 * Mask with a rectangular shape.
 * 
 * @author Javier Álvarez & Gloria Pozuelo. 
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
	
	
	/**
	 * Saves mask state
	 * @param savedInstanceState 
	 * */
	public void onSavedInstance(Bundle savedInstanceState, int i, int j) {
		super.onSavedInstance(savedInstanceState, i, j);
		savedInstanceState.putInt(i + " " + j + " mask_width", width);
		savedInstanceState.putInt(i + " " + j + " mask_height", height);
	}
	
	/**
	 * Restore mask state
	 * @param savedInstanceState 
	 * */
	public void onRestoreSavedInstance(Bundle savedInstanceState, int i, int j) {
		super.onRestoreSavedInstance(savedInstanceState, i, j);
		width = savedInstanceState.getInt(i + " " + j + " mask_width");
		height = savedInstanceState.getInt(i + " " + j + " mask_height");
	}
}
