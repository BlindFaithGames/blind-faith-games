package es.eucm.blindfaithgames.engine.feedback;

import org.acra.ErrorReporter;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

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
		 try {
			 getTracker().trackPageView("/" + page + "/" + parameter);
		 } catch(Exception e) {
			 ErrorReporter.getInstance().putCustomData("Error Inesperado", e.getMessage() + " " + 
					 						e.getStackTrace() + " " + page + " " + parameter);
			 ErrorReporter.getInstance().handleSilentException(new Exception("Null Pointer Register Page - Parameter"));
		 }
	 }
	 
	 public void registerPage(String page){  
		 try {
			 getTracker().trackPageView("/" + page);
		 } catch(Exception e) {
			 ErrorReporter.getInstance().putCustomData("Error Inesperado", e.getMessage() + " " + e.getStackTrace() + " " + page);
			 ErrorReporter.getInstance().handleSilentException(new Exception("Null Pointer Register Page"));
		 }
	 }
	 
	 public void registerAction(String Category, String Action, String Label, int Value){
		 try {
			 getTracker().trackEvent(Category, Action, Label, Value);
		 } catch(Exception e) {
			 ErrorReporter.getInstance().putCustomData("Error Inesperado", e.getMessage() + " " + 
					 						e.getStackTrace() + " " + Category + " " + Action + " " + Label + " " + Value);
			 ErrorReporter.getInstance().handleSilentException(new Exception("Null Pointer Register Action"));
		 }
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