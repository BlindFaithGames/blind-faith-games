package com.accgames.sound;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

/**
 * This class allows use the default voice synthesizer in the system to transcribe a string, 
 * and checks what is the synthesis engine installed in a device.
 * 
 * @author Gloria Pozuelo & Javier Álvarez 
 * @implements OnInitListener, Parcelable
 * 
 */
public class TTS implements TextToSpeech.OnInitListener, Parcelable {

	private static final String TAG = "Synthesizer"; 						// Debug
	private static final String appname = "IVONA Text-to-Speech HQ"; 		//
	private static final String SYSTEM_TTS = "com.svox.pico"; 				//
	public static final int QUEUE_FLUSH = TextToSpeech.QUEUE_FLUSH; 		// Mapping 
	public static final int QUEUE_ADD = TextToSpeech.QUEUE_ADD; 			// Mapping
	private TextToSpeech mTts; 												// Reference to TextToSpeech class
	private int queueMode; 													// Determines if the sound will be accumulated or flushed.
	private SubtitleManager subs; 											// References to be able to transcribe the text
	private boolean enabled;												// to disable the speech synthesis
	private String initialSpeech; 											// speech read at the creation of a class instance.
	private String lastSpeech;												// saves the last message read by TTS
	
	/**
	 * Creator for Parcelable interface.
	 * 
	 * */
	public static final Parcelable.Creator<TTS> CREATOR = new Parcelable.Creator<TTS>() {
		public TTS createFromParcel(Parcel in) {
			return new TTS(in);
		}

		public TTS[] newArray(int size) {
			return new TTS[size];
		}
	};

	/**
	 * Constructor used in Parcelable.
	 * */
	private TTS(Parcel in) {
		mTts = null;
		enabled = in.readInt() == 1;
		queueMode = in.readInt();
		
		subs = new SubtitleManager((SubtitleInfo)in.readParcelable(SubtitleInfo.class.getClassLoader()));
		
		lastSpeech = in.readString();
	}

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param ctxt current context where TTS is used.
	 * @param initialSpeech speech read at the creation of a class instance.
	 * 
	 * */
	public TTS(Context ctxt, int queueMode) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		initialSpeech = null;
		enabled = true;
		this.queueMode = queueMode;	
		
		subs = new SubtitleManager(ctxt);
		
		lastSpeech = "";
	}

	/**
	 * Class constructor.
	 * 
	 * @param ctxt current context where TTS is used.
	 * @param initialSpeech speech read at the creation of a class instance.
	 * @param queueMode determines if the sound will be accumulated or flushed.
	 * */
	public TTS(Context ctxt, String initialSpeech, int queueMode) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		this.initialSpeech = initialSpeech;
		enabled = true;
		this.queueMode = queueMode;
		
		subs = new SubtitleManager(ctxt);
		
		lastSpeech = "";
	}
	
	/**
	 * Constructor that will be used in transcription mode.
	 * 
	 * @param ctxt current context where TTS is used.
	 * @param initialSpeech speech read at the creation of a class instance.
	 * @param queueMode determines if the sound will be accumulated or flushed.
	 * @param sInfo info needed to instantiate SubtitleManager
	 * 
	 * */
	public TTS(Context ctxt, String initialSpeech, int queueMode, SubtitleInfo sInfo) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		this.initialSpeech = initialSpeech;
		enabled = true;
		this.queueMode = queueMode;
		
		subs = new SubtitleManager(ctxt, sInfo);
		
		lastSpeech = "";
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------

	public boolean isEnabled() {
		return enabled;
	}

// ----------------------------------------------------------- Setters ----------------------------------------------------------
	public void setContext(Context ctxt) {
		if (isInstalled(ctxt)){
			this.mTts = new TextToSpeech(ctxt, this);
			subs.setContext(ctxt);
		}
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setInitialSpeech(String initialSpeech) {
		this.initialSpeech = initialSpeech;
	}

	public void setQueueMode(int queueMode) {
		this.queueMode = queueMode;
	}
	
	public void setmTts(TextToSpeech mTts) {
		this.mTts = mTts;
	}

// ----------------------------------------------------------- Others ----------------------------------------------------------

	/**
	 * Initialize the TTS in the current context.
	 * @param status indicates if the initialization is successful. It can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
	 * 
	 * */
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			int result = mTts.setLanguage(Locale.getDefault());
			// Try this someday for some interesting results.
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Language data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
			} else {
				// The TTS engine has been successfully initialized.
				if (initialSpeech != null && enabled){
					lastSpeech = initialSpeech;
					mTts.speak(initialSpeech, TextToSpeech.QUEUE_FLUSH, null);
					subs.showSubtitle("TTS:" + initialSpeech);
				}
			}

		} else {
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}
	}

	/**
	 * Checks if only is installed Pico TTS.
	 * 
	 * @param c Current context where TTS is used.
	 * 
	 * @result the search result.
	 * */
	public static boolean isBestTTSInstalled(Context c) {
		// Checks if IVONA is installed.
		List<PackageInfo> packs = c.getPackageManager().getInstalledPackages(0);
		int i = 0;
		boolean found = false, enabled = false;
		while (!found && i < packs.size()) {
			PackageInfo p = packs.get(i);
			Log.d(TAG, p.applicationInfo.loadLabel(c.getPackageManager()).toString());
			found = appname.equals(p.applicationInfo.loadLabel(c.getPackageManager()).toString());
			i++;
		}
		// If it's not installed check if Pico TTS is the only one
		if (!found) {
			Intent intent = new Intent("android.intent.action.START_TTS_ENGINE");
			ResolveInfo[] enginesArray = new ResolveInfo[0];
			PackageManager pm = c.getPackageManager();
			enginesArray = pm.queryIntentActivities(intent, 0).toArray(enginesArray);
			int j = 0;
			while (!enabled && j < enginesArray.length) {
				// If it's Pico TTS and is enabled
				enabled = enginesArray[j].activityInfo.packageName.equals(SYSTEM_TTS) && enginesArray.length == 1;
				j++;
			}
			return !enabled;
		} else
			return true;
	}

	/**
	 * Checks if any tts engine is installed.
	 * 
	 * @param ctx the application context where the tts is working.
	 * 
	 * @return the search result.
	 * */
	public static boolean isInstalled(Context ctx) {
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * Reads a message given by parameter.
	 *
	 * @param msg the message that will be read.
	 * */
	public void speak(String msg) {
		if (enabled){
			lastSpeech = msg;
			mTts.speak(msg, queueMode, null);
			subs.showSubtitle("TTS:" + msg);
		}
	}

	/**
	 * Reads a view given by parameter. Based in contentDescription attribute.
	 *
	 * @param v the v that will be read.
	 * */
	public void speak(View v) {
		if (enabled){
			lastSpeech = v.getContentDescription().toString();
			mTts.speak(v.getContentDescription().toString(), queueMode, null);
			subs.showSubtitle("TTS:" + v.getContentDescription().toString());
		}
	}

	/**
	 * Reads a message list given by parameter.
	 *
	 * @param msg the message list that will be read.
	 * */
	public void speak(List<String> msg) {
		String s;
		if (enabled) {
			Iterator<String> it = msg.iterator();
			while (it.hasNext()){
				s = it.next();
				lastSpeech = " " + s + " ";
				mTts.speak(" " +  s + " ", QUEUE_ADD, null);
				subs.showSubtitle(" " + "TTS:" +  it.next() + " ");
			}
		}
	}
	
	/**
	 * Reads again the last message that has been spoken by TTS.
	 *
	 * */
	public void repeatSpeak(){
		if (enabled && lastSpeech != null){
			mTts.speak(lastSpeech, queueMode, null);
			subs.showSubtitle("TTS:" + lastSpeech);
		}
	}

	/**
	 * Stops the tts action.
	 * 
	 * */
	public void stop() {
		mTts.stop();
		mTts.shutdown();
	}
	
	/**
	 * Enables the speech transcription.
	 * 
	 * */
	public void enableTranscription(SubtitleInfo sInfo){
		if(subs != null){
			subs.setEnabled(true);
			if(sInfo != null)
				subs.setsInfo(sInfo);
		}
	}
	
	/**
	 * Disables the speech transcription.
	 * 
	 * */
	public void disableTranscription(){
		if(subs != null){
			subs.setEnabled(false);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(enabled ? 1 : 0);
		dest.writeInt(queueMode);
		dest.writeParcelable(subs.getsInfo(), flags);
		dest.writeString(lastSpeech);
	}
}
