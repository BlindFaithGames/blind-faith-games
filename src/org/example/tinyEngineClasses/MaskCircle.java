package org.example.tinyEngineClasses;

public class MaskCircle  extends Mask{
	
	private double cx,cy;
	private double radius;
	
	public MaskCircle(double cx, double cy, double radius){
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
	}
	
	@Override
	public boolean isInMask(double x, double y) {
		return Math.sqrt(Math.pow((x-cx),2) + Math.pow((x-cy),2)) <= radius;
	}
	public double getRadius(){return radius;}

	public double getCenterY() {
		return cx;
	}

	public double getCenterX() {
		return cy;
	}
	
}
