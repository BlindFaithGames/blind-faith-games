package org.example.tinyEngineClasses;

public abstract class Mask {
	
	public abstract boolean isInMask(double x, double y);
	
	public boolean collide(Mask m){
		
		if(m instanceof MaskCircle){
			MaskCircle circle = (MaskCircle) m;
			double cx = circle.getCenterX();
			double cy = circle.getCenterY();
			double r = circle.getRadius();
			double inc = 0.5d; // hardcodeo a tope!!
			double x,y;
			boolean found = false;
			while(r>0 && !found){
				double ang = 0;
				while(ang < 360 && !found){
					x = cx + r * Math.cos(ang);
					y = cy + r * Math.sin(ang);
					found = this.isInMask(x,y);
					ang=ang+inc;
				}	
				r--;
			}
			return found;
		}
		else
			if(m instanceof MaskBox){
				MaskBox box = (MaskBox) m;
				double x = box.getLeft();
				double y;
				boolean found = false;
				while(x < box.getRight() && !found){
					y = box.getTop(); 
					while(y < box.getBottom() && !found){
						found = this.isInMask(x,y);
						y--;
					}
					x++;
				}
				return found;
			}
		return false;
	}
}
