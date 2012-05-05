package es.eucm.blindfaithgames.engine.feedback;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Context;

public class AnalyticsManager {
	 public static String TRACK_ID;

	 private static AnalyticsManager analyticsManager = null;

	 private static GoogleAnalyticsTracker tracker;
	 private static final int frecuencyUpdateGoogle = 10;
	 private static Context context;
	 
	 public static String getUACodeAnalytics(){return TRACK_ID;}

	 public static AnalyticsManager getAnalyticsManager(Context context){
		 if(analyticsManager == null){
			   analyticsManager = new AnalyticsManager(context);
			   tracker = GoogleAnalyticsTracker.getInstance();
			   tracker.startNewSession(TRACK_ID, frecuencyUpdateGoogle , context);
		 }
	  	 return analyticsManager;
	 }
	  
	 public static AnalyticsManager getAnalyticsManager(){
	  return analyticsManager;
	 }
	 
	 public static GoogleAnalyticsTracker getTracker(){
		 if (tracker == null){
			   analyticsManager = new AnalyticsManager(context);
			   tracker = GoogleAnalyticsTracker.getInstance();
			   tracker.startNewSession(TRACK_ID, frecuencyUpdateGoogle , context);
		 }
		return tracker;
	 }
	 
	 public AnalyticsManager(Context context){
		 AnalyticsManager.context = context;  
	 }
	 
	 public void registerPage(String page, String parameter){
		getTracker().trackPageView("/" + page+ "/" + parameter);
	 }
	 
	 public void registerPage(String page){  
		 getTracker().trackPageView("/" + page);
	 }
	 
	 public void registerAction(String Category, String Action, String Label, int Value){
		 getTracker().trackEvent(Category, Action, Label, Value);
	 }
	 
	 public static void stopTracker(){
		 if(getTracker() != null)
			 getTracker().stopSession();
	 }

	public static void dispatch() {
		if(getTracker() != null)
			  getTracker().dispatch();
	}
}
