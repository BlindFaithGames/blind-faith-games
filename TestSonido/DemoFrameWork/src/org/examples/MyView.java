package org.examples;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class MyView  extends View{

	private int width;
	
	private int height;
	
	private int imgH,imgW;
	
	private Bitmap img; 
	
	@SuppressWarnings("unused")
	private static final String TAG = "myView";

	private SoundManager sm;
	
	public MyView(Context context, SoundManager sm, int width, int height) {
		super(context);
		this.setBackgroundColor(Color.WHITE);
		requestFocus();
		setFocusableInTouchMode(true);
		this.sm = sm;
		
		img = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.dot);
		imgW =  img.getWidth();
		imgH =  img.getHeight();

		this.width = width;
		this.height = height;
	}
	
	public int getWidth1() {
		return width;
	}

	public int getHeight1() {
		return height;
	}

	public int getImgH() {
		return imgH;
	}

	public int getImgW() {
		return imgW;
	}

	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Set view dimensions
		width = w;
		height = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// Center
		canvas.drawBitmap(img, width/2 - imgW/2, height/2 - imgH/2, null);
		
		// Left
		canvas.drawBitmap(img,0,height/2 - imgH/2, null);
		
		// Right
		canvas.drawBitmap(img, width - imgW, height/2 - imgH/2, null);
		
		// Up
		canvas.drawBitmap(img, width/2 - imgW/2, 0, null);
		
		// Down
		canvas.drawBitmap(img, width/2 - imgW/2, height - imgH, null);
		
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		super.onTouchEvent(e);
		if(e.getAction() == MotionEvent.ACTION_MOVE){
			sm.setListenerPosition(e.getX(), e.getY(), 0);
		}
		return true;
	}
}
