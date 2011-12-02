package org.example.tinyEngineClasses;

import android.graphics.Canvas;

public abstract class Mask {
	
	protected int offsetX,offsetY; // offset between entity coordinates and mask coordinates
	protected int x,y; // Mask coordinates
	
	public Mask(int offsetX, int offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public abstract boolean isInMask(int x, int y);
	public abstract void onDraw(Canvas canvas);
	
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void onUpdate(int x, int y) {
		this.x = x + offsetX;
		this.y = y + offsetY;
	}
	
	
}
