package org.example.minesweeper;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Manages one,double,triple tap
 * 
 * 
 * */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {

	private MinesweeperView minesweeperView;
	
	private Long timeTaps[]; // time (system time) of each tap
	private int tapsN; // number of taps

	private MotionEvent eventTaps[]; // tap events  

	
	private Runnable RunnableEvent= new Runnable() {
		public void run() {
			// triple tap
			if(timeTaps[1] != 0  && timeTaps[0] != 0 && timeTaps[2] != 0){
				minesweeperView.onTripleTapAction(eventTaps[2]);
			}else// double tap
				if(timeTaps[1] != 0  && timeTaps[0] != 0){
					minesweeperView.onDoubleTapAction(eventTaps[1]);
				}
				else // one tap
					if(timeTaps[0] != 0){
						minesweeperView.onTapAction(eventTaps[0]);
					}
			// Consumed the event reset all
			for(int i = 0; i < 3; i++){
				timeTaps[i] = (long) 0;
				eventTaps[i] = null;
				tapsN = 0;
			}
		}
	};


	public GestureListener(MinesweeperView minesweeperView){
		super();
		this.minesweeperView = minesweeperView;
		timeTaps = new Long[3];
		for(int i = 0; i < 3; i++)
			timeTaps[i] = (long) 0;
		eventTaps = new MotionEvent[3];
		tapsN = 0;
	}
	
    @Override
    public boolean onDown(MotionEvent event) {
    	timeTaps[tapsN] = System.currentTimeMillis();
    	eventTaps[tapsN] = event;
    	if(tapsN < 2)
    		tapsN++;
    	Handler myHandler = new Handler();
    	myHandler.postDelayed(RunnableEvent, 750);
        return true;
    }
}
