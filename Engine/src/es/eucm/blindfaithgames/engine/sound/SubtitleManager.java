package es.eucm.blindfaithgames.engine.sound;

import java.util.LinkedList;

import org.acra.ErrorReporter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class manages the transcription system both to music and voice synthesizer.
 * 
 * */
public class SubtitleManager {
	
	private static Toast subtitles; // Toast used on the screen
	private static LinkedList<String> subsQueue; // List where the message are been accumulated.
	private SubtitleInfo sInfo; // information to custom toast.
	
	private Activity context; // context where the subtitle manager is working.
	
	// To increase the time that the toast is showed
	private int toast_long; 
	private static Handler mHandler = new Handler();
	
	/**
	 * Default constructor
	 * */
	public SubtitleManager() {
		this.sInfo = new SubtitleInfo();
		mHandler = new Handler();
		subsQueue = new LinkedList<String>();
	}
	
	/**
	 * To instantiate a default toast.
	 * 
	 * */
	public SubtitleManager(Context c) {
		this.context = (Activity) c;
		sInfo = new SubtitleInfo();
		mHandler = new Handler();
		subtitles = createToast(c, sInfo);
		subsQueue = new LinkedList<String>();
	}

	/**
	 * To instantiate a custom toast.
	 * 
	 * */
	public SubtitleManager(SubtitleInfo sInfo) {
		this.sInfo = sInfo;
		mHandler = new Handler();
		subsQueue = new LinkedList<String>();
	}
	
	/**
	 * To instantiate a custom toast in a given context.
	 * 
	 * */
	public SubtitleManager(Context c, SubtitleInfo sInfo) {
		this.context = (Activity) c;
		this.sInfo = sInfo;
		mHandler = new Handler();
		subtitles = createToast(c, sInfo);
		subsQueue = new LinkedList<String>();
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	public SubtitleInfo getsInfo() {
		return sInfo;
	}

	public String getOnomatopeia(int resource) {
		return sInfo.getOnomatopeia(resource);
	}

// ----------------------------------------------------------- Setters -----------------------------------------------------------	
	public void setContext(Context ctxt) {
		this.context = (Activity) ctxt;
		subtitles = createToast(ctxt,sInfo);
	}
	
	public void setEnabled(boolean enabled) {
		sInfo.setEnabled(enabled);
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
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	
	/**
	 * Creates a new toast in the context c with custom preferences sInfo. If sInfo is null creates a default toast.
	 * 
	 * @param c context where the subtitle manager is working.
	 * @param sInfo information to custom toast.
	 * 
	 * @return a new instance of toast.
	 * 
	 * */
	private Toast createToast(Context c, SubtitleInfo sInfo) {
		Toast toast = null;
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
			if(sInfo != null)
				ErrorReporter.getInstance().handleSilentException(new Exception("Fallo en creación de toast \n" + e.getMessage() + "\n" + 
						e.getStackTrace() + "\n sInfo: " + sInfo.toString()));
			else
				 ErrorReporter.getInstance().handleSilentException(new Exception("Fallo en creación de toast \n" + e.getMessage() + "\n" + 
							e.getStackTrace()));
		}

		return toast;
	}
	
	/**
	 * Updates the toast with msg based in sInfo if it's a custom toast.
	 * 
	 * @param toast the toast that will be updated.
	 * @param msg the text used to update.
	 * @param sInfo information about the toast visualization.
	 * 
	 * */
	private void updateToastText(Toast toast, String msg, SubtitleInfo sInfo) {
		if(toast != null){
			try{
				View layout;
				LayoutInflater inflater = context.getLayoutInflater();
				layout = inflater.inflate(sInfo.getResourceId(),
				                               (ViewGroup) context.findViewById(sInfo.getViewGroupRoot()));
	
				TextView text = (TextView) layout.findViewById(sInfo.getId_text());
				text.setText(msg);
				
				toast.setGravity(sInfo.getGravity(), sInfo.getxOffset(), sInfo.getyOffset());
				toast.setDuration(sInfo.getDuration());
			toast.setView(layout);
			} catch (Exception e) {
				if(sInfo != null)
					ErrorReporter.getInstance().handleSilentException(new Exception("Fallo en creación de toast \n" + e.getMessage() + "\n" + 
							e.getStackTrace() + "\n sInfo: " + sInfo.toString()));
				else
					 ErrorReporter.getInstance().handleSilentException(new Exception("Fallo en creación de toast \n" + e.getMessage() + "\n" + 
								e.getStackTrace()));
			}
		}
	}
	
	/**
	 * Shows msg in a toast on the screen.
	 * 
	 * @param msg message that will be showed.
	 * */
	public void showSubtitle(String msg){
		if(msg != null && msg.length() < 200 && sInfo.isEnabled()){
			if(subsQueue.isEmpty())
				displayMyToast(msg);
			subsQueue.offer(msg);
		}
	}
	
	/**
	 * To refresh the toast an control its duration in seconds.
	 * 
	 * */
	private Runnable extendStatusMessageLengthRunnable = new Runnable() {
		@Override
	    public void run() {
			
		    //Show the toast for another interval.
			if(subtitles != null)
				subtitles.show();
		    
		    toast_long--;
		    
		   if(!subsQueue.isEmpty()) {
			    String s = subsQueue.poll(); 
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
	
	/**
	 * Updates the toast text and shows it.
	 * 
	 * @param msg message that will be showed..
	 * */
	public void displayMyToast(String msg) {
		mHandler.removeCallbacks(extendStatusMessageLengthRunnable);
		
		toast_long = msg.length();
		
		updateToastText(subtitles, msg, sInfo);
		
		if(subtitles != null)
			subtitles.show();
		  
		mHandler.postDelayed(extendStatusMessageLengthRunnable,100L);
	}
}
