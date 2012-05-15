	package es.eucm.blindfaithgames.engine.general;


import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class provides a Custom surface view controlled within other thread
 * 
 * @author Javier Álvarez & Gloria Pozuelo. 
 * 
 * @implements SurfaceHolder.Callback, ISurface.
 * 
 * */

public abstract class DrawablePanel extends SurfaceView implements SurfaceHolder.Callback, ISurface {
	
	private AnimationThread thread;
	/**
	 * Unique constructor of the class.
	 * 
	 * @param context Activity which is associated to this view.
	 * 
	 * */
	public DrawablePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		requestFocus();
		setFocusableInTouchMode(true);
	}
	
	 @Override
	 public void onDraw(Canvas canvas) {
	 }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.thread = new AnimationThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    boolean retry = true;
	    thread.setRunning(false);
	    while (retry) {
	        try {
	            thread.join();
	            retry = false;
	        } catch (InterruptedException e) {
	        }
	    }			
	}
}