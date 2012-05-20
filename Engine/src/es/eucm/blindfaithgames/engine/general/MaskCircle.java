package es.eucm.blindfaithgames.engine.general;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;


/**
 * Mask with a circular shape.
 * 
 * @author Javier Álvarez & Gloria Pozuelo. 
 * 
 * @extends Mask.
 * 
 * */
public class MaskCircle  extends Mask{
	
	private int radius; // mask radius
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param offsetX Offset within x relative to the entity coordinates.
	 * @param offsetY Offset within y relative to the entity coordinates.
	 * @param radius mask radius.
	 * 
	 * */
	public MaskCircle(int offsetX, int offsetY, int radius){
		super(offsetX,offsetY);
		this.radius = radius;
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------   
	public int getRadius(){return radius;}

	public int getCenterY() {
		return this.y;
	}

	public int getCenterX() {
		return this.x;
	}

// ----------------------------------------------------------- Others -----------------------------------------------------------   	
	@Override
	public boolean isInMask(int x, int y) {
		return Math.sqrt((x-this.x)*(x-this.x) + (y-this.y)*(y-this.y)) <= radius;
	}
	
	//  For debug
	public void onDraw(Canvas canvas){
		Paint brush = new Paint();
		brush.setColor(Color.GREEN);
		brush.setStyle(Style.STROKE);
		canvas.drawCircle(this.x, this.y, radius, brush);
	}
	
	public Object clone(Object o){
		MaskCircle m1, m2;
		m1 = (MaskCircle) o;
		m2 = new MaskCircle(m1.offsetX,m1.offsetY,m1.radius);
		return m2;
	}
	
	/**
	 * Saves mask state
	 * @param savedInstanceState 
	 * */
	public void onSavedInstance(Bundle savedInstanceState, int i, int j) {
		super.onSavedInstance(savedInstanceState,i,j);
		savedInstanceState.putInt(i + " " + j + " mask_radius", radius);
	}
	
	public void onRestoreSavedInstance(Bundle savedInstanceState, int i, int j) {
		super.onRestoreSavedInstance(savedInstanceState,i,j);
		radius = savedInstanceState.getInt(i + " " + j + " mask_radius");
	}
}
