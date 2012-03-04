package com.accgames.tinyengine;

import android.content.Context;

import com.golfgame.R;

public class SoundConfig {

	SoundManager SM;
	public enum Distance {V_CLOSE,CLOSE, FAR, V_FAR, TARGET}
	private Distance dist;
	private enum Side {RIGHT, LEFT, TARGET}
	private Side side;
	private Context theContext;
	private Game theGame;

	public SoundConfig(Context c, Game g){
		theContext = c;
		theGame = g;
		SM = new SoundManager(theContext);
        SM.addSound(0, R.raw.missed, theContext);
        SM.addSound(1, R.raw.left, theContext);
        SM.addSound(2, R.raw.right, theContext);
        SM.addSound(3, R.raw.bing, theContext);
	}
	
	/**
	 * Methods to play different sounds according to distance and
	 * side of the missed shot
	 **/
	public void playVClose(int side){
		SM.playLooped(0, 6, 2.0f, side);
	}
	public void playClose(int side){
		SM.playLooped(0, 5, 1.5f, side);
	}
	public void playFar(int side){
		SM.playLooped(0, 4, 1.0f, side);
	}
	public void playVFar(int side){
		SM.playLooped(0, 3, 0.5f, side);
	}
	
	public void playTarget(){
		SM.play(3);
	}
	
	public Distance playSound(int targetPos, int x){
		dist = getDist(x, targetPos);
		side = getSide(x, targetPos);
		switch (side){
			case RIGHT: playResult(0);break;
			case LEFT:  playResult(1);break;
			case TARGET: SM.play(1);break; 
	}
		return dist;
	}
	
	/**
	 * Returns how far is the missed shot, referring to the target
	 **/
	public Distance getDist(int x, int targetPos){
    	Distance d=null;
    	int loc = Math.abs(targetPos - x);
    	int width =theGame.getView().getWidth();
    	if (0<loc&&loc<width/8) d=Distance.V_CLOSE;
    	else if (width/8<loc&&loc<width/4) d=Distance.CLOSE;
    	else if (width/4<loc&&loc<width*3/8) d=Distance.FAR;
    	else d=Distance.V_FAR;
    	return d;		
    }
	
	/**
	 * Returns witch side is the missed shot, referring to the target
	 **/
	public Side getSide(int x, int targetPos){
    	Side s = null;
    	int location = targetPos - x;
    	if (location>0) s=Side.RIGHT;
    	else if (location<0) s=Side.LEFT;
    	else s=Side.TARGET;
    		return s;
    }
	
	public void playResult(int vol){
    	switch (dist){
			case CLOSE: 	playClose(vol);break;
			case V_CLOSE: 	playVClose(vol);break;
			case FAR: 		playFar(vol);break;
			case V_FAR: 	playVFar(vol);break;
		}
	}
	
	public boolean isWiredHeadsetOn(){
		return SM.isWiredHeadsetOn();
	}
}

