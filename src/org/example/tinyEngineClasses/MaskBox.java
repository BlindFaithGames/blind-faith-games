package org.example.tinyEngineClasses;


public class MaskBox extends Mask{

	private int width,height;
	
	public MaskBox(int offsetX, int offsetY, int width, int height){
		super(offsetX, offsetY);
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	public boolean isInMask(int x, int y) {
		return x >= this.x && x <= width+this.x && y >= this.y && y <= height+this.y;
	}

}
