package org.example.tinyEngineClasses;

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
		return this.x;
	}

	public int getCenterX() {
		return this.y;
	}
}
