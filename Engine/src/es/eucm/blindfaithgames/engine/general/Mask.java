package es.eucm.blindfaithgames.engine.general;

import android.graphics.Canvas;

/**
 * Abstract class that provides methods to implement mask with different shapes. 
 * 
 * @author Javier Álvarez & Gloria Pozuelo. 
 * 
 * */

public abstract class Mask {
	
	protected int offsetX,offsetY; // Offset between entity coordinates and mask coordinates
	protected int x,y; // Mask coordinates
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param offsetX Offset within x relative to the entity coordinates.
	 * @param offsetY Offset within y relative to the entity coordinates.
	 * 
	 * */
	public Mask(int offsetX, int offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------    
	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------    
	/**
	 * Checks if x and y are on the concrete mask area.
	 * 
	 * @param x the coordinate on the x axis.
	 * @param y the coordinate on the y axis.
	 * @return The result of the test.
	 * */
	public abstract boolean isInMask(int x, int y);
	
	/**
	 * To debug. It have to draw a mask skeleton.
	 * 
	 * @param canvas The surface that will be painted.
	 * 
	 * */
	public abstract void onDraw(Canvas canvas);
	
	/**
	 * Method that test if this and m collides calling isInMask method.
	 * @param m the mask to be tested with this.
	 * @return The result of the collision test.
	 * */
	public boolean collide(Mask m){
		
		if(m instanceof MaskCircle){
			MaskCircle circle = (MaskCircle) m;
			int cx = circle.getCenterX();
			int cy = circle.getCenterY();
			int r = circle.getRadius();
			int inc = 1; 
			int auxX,auxY;
			boolean found = false;
			double ang = 0;
			while(ang < 360 && !found){
					auxX = (int) (cx + r * Math.cos(ang));
					auxY = (int) (cy + r * Math.sin(ang));
					found = this.isInMask(auxX,auxY);
					ang=ang+inc;
			}	
			return found;
		}
		else
			if(m instanceof MaskBox){
				MaskBox box = (MaskBox) m;
				int auxX = box.getX();
				int x = box.getX();
				int y = box.getY();
				int auxY;
				boolean found = false;
				while(auxX < x + box.getWidth() && !found){
					auxY = box.getY(); 
					while(auxY < y + box.getHeight() && !found){
						found = this.isInMask(auxX,auxY);
						auxY++;
					}
					auxX++;
				}
				return found;
			}
		return false;
	}
	
	/**
	 * Updates the mask coordinates from the entity coordinates.
	 * @param x the coordinate on the x of the entity associated to this mask.
	 * @param y the coordinate on the y axis of the entity associated to this mask.
	 * */
	public void onUpdate(int x, int y) {
		this.x = x + offsetX;
		this.y = y + offsetY;
	}
	
}
