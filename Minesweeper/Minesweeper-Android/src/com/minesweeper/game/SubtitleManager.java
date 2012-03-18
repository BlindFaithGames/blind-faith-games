package com.minesweeper.game;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SubtitleManager {
	
	private static Toast subtitles;
	private static LinkedList<String> subsQueue;
	private SubtitleInfo sInfo;
	
	private Activity context;
	
	// To increase the time that the toast is showed
	private int toast_long; 
	private static Handler mHandler = new Handler();

	public SubtitleManager(Context c, SubtitleInfo sInfo) {
		this.context = (Activity) c;
		this.sInfo = sInfo;
		mHandler = new Handler();
		subtitles = createToast(c, sInfo);
		subsQueue = new LinkedList<String>();
	}
	
	public SubtitleManager(Context c) {
		this.context = (Activity) c;
		sInfo = new SubtitleInfo();
		mHandler = new Handler();
		subtitles = createToast(c, sInfo);
		subsQueue = new LinkedList<String>();
	}

	public SubtitleManager(SubtitleInfo sInfo) {
		this.sInfo = sInfo;
		mHandler = new Handler();
		subsQueue = new LinkedList<String>();
	}

	public SubtitleManager() {
		this.sInfo = new SubtitleInfo();
		mHandler = new Handler();
		subsQueue = new LinkedList<String>();
	}

	public SubtitleInfo getsInfo() {
		return sInfo;
	}

	public String getOnomatopeia(int resource) {
		return sInfo.getOnomatopeia(resource);
	}
	
	public void setEnabled(boolean enabled) {
		sInfo.setEnabled(enabled);
	}
	
	public void setCustomToast(Toast customToast) {
		subtitles = customToast;
	}
	
	public void setGravity(int gravity) {
		sInfo.setGravity(gravity);
	}
	
	public void setDuration(int duration) {
		sInfo.setDuration(duration);
	}

	public void setsInfo(SubtitleInfo sInfo) {
		this.sInfo = sInfo;
	}
	
	public void setOffset(int xOffset, int yOffset) {
		sInfo.setOffset(xOffset,yOffset);
	}
	
	public void setContext(Context ctxt) {
		this.context = (Activity) ctxt;
		subtitles = createToast(ctxt,sInfo);
	}
	
	private Toast createToast(Context c, SubtitleInfo sInfo) {
		Toast toast;
		if(sInfo.getResourceId() == -1){
			toast = Toast.makeText(c, "", sInfo.getDuration());
		}else{
			View layout;
			try {
				LayoutInflater inflater = context.getLayoutInflater();
				layout = inflater.inflate(sInfo.getResourceId(),
				                               (ViewGroup) context.findViewById(sInfo.getViewGroupRoot()));

				TextView text = (TextView) layout.findViewById(sInfo.getId_text());
				text.setText("");
				
				toast = new Toast(c);
				toast.setGravity(sInfo.getGravity(), sInfo.getxOffset(), sInfo.getyOffset());
				toast.setDuration(sInfo.getDuration());
				toast.setView(layout);
				
			} catch (Exception e) {
				toast = Toast.makeText(c, "", sInfo.getDuration());
			}
		}
		
		return toast;
	}
	
	private void updateToastText(Toast toast, String msg, SubtitleInfo sInfo) {
		if(sInfo.getResourceId() == -1){
			toast.setText(msg);
		}else{
			View layout;
			try {
				LayoutInflater inflater = context.getLayoutInflater();
				layout = inflater.inflate(sInfo.getResourceId(),
				                               (ViewGroup) context.findViewById(sInfo.getViewGroupRoot()));

				TextView text = (TextView) layout.findViewById(sInfo.getId_text());
				text.setText(msg);
				
				toast.setGravity(sInfo.getGravity(), sInfo.getxOffset(), sInfo.getyOffset());
				toast.setDuration(sInfo.getDuration());
				toast.setView(layout);
				
			} catch (Exception e) {
				toast.setText(msg);
			}
		}
	}
	
	public void showSubtitle(String msg){
		if(msg != null && msg.length() < 200 && sInfo.isEnabled()){
			if(subsQueue.isEmpty())
				displayMyToast(msg);
			subsQueue.offer(msg);
		}
	}
	
	private Runnable extendStatusMessageLengthRunnable = new Runnable() {
		@Override
	    public void run() {
			
		    //Show the toast for another interval.
		    subtitles.show();
		    
		    toast_long--;
		    
		   if(!subsQueue.isEmpty()) {String s = subsQueue.poll(); 
		    while(!subsQueue.isEmpty()){
		    			s +=  ", " + subsQueue.poll();
		    }
		    if(s != null)
		    	displayMyToast(s);
		    
		   }else{if(toast_long > 0){
			    	mHandler.postDelayed(extendStatusMessageLengthRunnable,100L);
			}
			else{
			    String s = subsQueue.poll(); 
			    while(!subsQueue.isEmpty()){
			    			s +=  ", " + subsQueue.poll();
			    }
			    if(s != null)
			    	displayMyToast(s);
			}
		   }
	   }
	};
	 
	public void displayMyToast(String msg) {
		mHandler.removeCallbacks(extendStatusMessageLengthRunnable);
		
		toast_long = msg.length();//getsInfo().getDuration()/100;
		
		updateToastText(subtitles, msg, sInfo);
		subtitles.show();
		  
		mHandler.postDelayed(extendStatusMessageLengthRunnable,100L);
	}
}
