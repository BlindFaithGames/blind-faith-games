package org.example.tinyEngineClasses;

import android.graphics.Rect;

public class MaskBox extends Mask{

	private Rect mask;
	
	private int left,right,top,bottom;
	
	public MaskBox(int left, int top, int right, int bottom){
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.mask = new Rect(left, top, right, bottom);
	}

	public int getBottom() {
		return bottom;
	}

	public int getLeft() {
		return left;
	}
	
	public int getRight() {
		return right;
	}
	
	public int getTop() {
		return top;
	}
	
	@Override
	public boolean isInMask(double x, double y) {
		return x >= left && x <= right && y <= top && y >= bottom;
	}
}
