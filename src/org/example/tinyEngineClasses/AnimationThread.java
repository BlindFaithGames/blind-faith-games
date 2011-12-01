package org.example.tinyEngineClasses;



import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class AnimationThread extends Thread {
	
	public static int DELAY = 1000/60;
	public static int FRAMES_PER_SECOND = 30;
	public static int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
	
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
	
	@Override
	public void run() {
		float fps = 0;
		int counter = 0;
		long next_game_tick = System.currentTimeMillis();
		long sleep_time = 0;
		Canvas c;
		long initialTime = System.currentTimeMillis();
	    while (run) {
	        c = null;
	    	next_game_tick += SKIP_TICKS;
	    	sleep_time = next_game_tick - System.currentTimeMillis();
	    	
	        if(sleep_time >= 0) {
	            try {
	    			Thread.sleep(sleep_time);
	    		} catch (InterruptedException e) {
	    			e.printStackTrace();
	    		}
	        }

        	try {
	            c = surfaceHolder.lockCanvas(null);
	            synchronized (surfaceHolder) {
	            	panel.onDraw(c);
	            	c.drawText("FPS "+ fps, 10, 10, new Paint());
	            }
	        } finally {
	            // do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
	            if (c != null) {
	                surfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
        	panel.onUpdate();
        	counter++;
        	long actualTime = System.currentTimeMillis();
	        if(actualTime - initialTime >= 1000) {
	        	 fps = counter;
	        	 counter = 0;
	        	 initialTime = actualTime;
	        }
	    }    		
	}
}




	
    
    

	


