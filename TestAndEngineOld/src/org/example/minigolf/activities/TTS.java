package org.example.minigolf.activities;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

/**
 * 
 * 
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez This class
 *         implements OnInitListener Interface
 * 
 */
public class TTS implements TextToSpeech.OnInitListener, Parcelable{

	private static final String TAG = "Synthesizer";
	
	public static final int QUEUE_FLUSH = TextToSpeech.QUEUE_FLUSH;
	
	public static final int QUEUE_ADD = TextToSpeech.QUEUE_ADD;

	private TextToSpeech mTts;

	private int queueMode;
	
	private boolean enabled;
	
	private String initialSpeech;
	
	
	 public static final Parcelable.Creator<TTS> CREATOR= new Parcelable.Creator<TTS>() {
		 public TTS createFromParcel(Parcel in) {
		     return new TTS(in);
		 }
	
		 public TTS[] newArray(int size) {
		     return new TTS[size];
		 }
	 };

	private TTS(Parcel in) {
		mTts = null;
		enabled = in.readInt() == 1;
		queueMode = in.readInt();
	}

	public TTS(Context ctxt, int queueMode) {
		if(isInstalled(ctxt))
			this.mTts =  new TextToSpeech(ctxt, this);
		
		initialSpeech = null;
		enabled = true;
		this.queueMode = queueMode;
	}

	public TTS(Context ctxt, String initialSpeech, int queueMode) {
		if(isInstalled(ctxt))
			this.mTts =  new TextToSpeech(ctxt, this);
		
		this.initialSpeech = initialSpeech;
		enabled = true;
		this.queueMode = queueMode;
	}
	
	public void setContext(Context ctxt){
		if(isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);
	};
	
	public boolean isEnabled() {
		return enabled;
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
	
	@Override
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			int result = mTts.setLanguage(Locale.US);
			// Try this someday for some interesting results.
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
			} else {
				// The TTS engine has been successfully initialized.
				if (initialSpeech != null && enabled)
					mTts.speak(initialSpeech, TextToSpeech.QUEUE_FLUSH, null);
			}

		} else {
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}
	}

	public static boolean isInstalled(Context ctx) {
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public void setmTts(TextToSpeech mTts) {
		this.mTts = mTts;
	}
	
	public void speak(String msg){
		if(enabled)
			mTts.speak(msg, queueMode, null);
	}
	
	public void speak(View v){
		if(enabled)
			mTts.speak(v.getContentDescription().toString(), queueMode, null);
	}
	
	public void speak(List<String> msg){
		if(enabled){
			Iterator<String> it = msg.iterator();
			while(it.hasNext())
				mTts.speak(it.next(), QUEUE_ADD, null);
		}
	}

	public void stop() {
		mTts.stop();
		mTts.shutdown();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(enabled ? 1 : 0);
		dest.writeInt(queueMode);
	}
}
