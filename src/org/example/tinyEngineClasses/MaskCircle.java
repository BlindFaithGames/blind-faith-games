package org.example.tinyEngineClasses;

public class MaskCircle  implements Mask{
	
	private float cx,cy;
	private float radius;
	
	
	
	public MaskCircle(float cx, float cy, float radius){
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
	}
	
	public boolean isInCircle(float x, float y){
		return Math.sqrt(Math.pow((x-cx),2) + Math.pow((x-cy),2)) <= radius;
	}
	
	public boolean collide(Mask m){
		
		if(m instanceof MaskCircle)
			
		else
			if(m instanceof MaskBox)
	}
	
}
