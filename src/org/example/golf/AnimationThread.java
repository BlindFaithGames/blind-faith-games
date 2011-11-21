package org.example.golf;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class AnimationThread extends Thread {
	private SurfaceHolder surfaceHolder;
	private ISurface panel;
	private boolean run = false;
	
	public AnimationThread(SurfaceHolder surfaceHolder, ISurface panel) {
		this.surfaceHolder = surfaceHolder;
		this.panel = panel;
		
		panel.onInitalize();
	}
	
	public void setRunning(boolean value) {
		run = value;
	}
	
	private long timer;

	@Override
	public void run() {
		
		Canvas c;
	    while (run) {
	        c = null;
	        timer = System.currentTimeMillis();
        	panel.onUpdate(timer);

        	try {
	            c = surfaceHolder.lockCanvas(null);
	            synchronized (surfaceHolder) {
	                panel.onDraw(c);
	            }
	        } finally {
	            // do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
	            if (c != null) {
	                surfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
	    }    		
	}
}
