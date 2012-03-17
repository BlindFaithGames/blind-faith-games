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
 * 
 * 
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez 
 * This class implements OnInitListener Interface
 * 
 */
public class TTS implements TextToSpeech.OnInitListener, Parcelable {

	private static final String TAG = "Synthesizer";

	private static final String appname = "IVONA Text-to-Speech HQ";

	private static final String SYSTEM_TTS = "com.svox.pico";
	
	public static final int QUEUE_FLUSH = TextToSpeech.QUEUE_FLUSH;

	public static final int QUEUE_ADD = TextToSpeech.QUEUE_ADD;

	private TextToSpeech mTts;

	private int queueMode;
	
	private SubtitleManager subs;

	private boolean enabled;
	
	private String initialSpeech;

	public static final Parcelable.Creator<TTS> CREATOR = new Parcelable.Creator<TTS>() {
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
		
		subs = new SubtitleManager((SubtitleInfo)in.readParcelable(SubtitleInfo.class.getClassLoader()));
	}

	public TTS(Context ctxt, int queueMode) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		initialSpeech = null;
		enabled = true;
		this.queueMode = queueMode;	
		
		subs = new SubtitleManager(ctxt);
	}

	public TTS(Context ctxt, String initialSpeech, int queueMode) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		this.initialSpeech = initialSpeech;
		enabled = true;
		this.queueMode = queueMode;
		
		subs = new SubtitleManager(ctxt);
	}
	
	public TTS(Context ctxt, String initialSpeech, int queueMode, SubtitleInfo sInfo) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		this.initialSpeech = initialSpeech;
		enabled = true;
		this.queueMode = queueMode;
		
		subs = new SubtitleManager(ctxt, sInfo);
	}

	public void setContext(Context ctxt) {
		if (isInstalled(ctxt)){
			this.mTts = new TextToSpeech(ctxt, this);
			subs.setContext(ctxt);
		}
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
	
	public void enableTranscription(SubtitleInfo sInfo){
		if(subs != null){
			subs.setEnabled(true);
			if(sInfo != null)
				subs.setsInfo(sInfo);
		}
	}
	
	public void disableTranscription(){
		if(subs != null){
			subs.setEnabled(false);
		}
	}
	
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
				if (initialSpeech != null && enabled){
					mTts.speak(initialSpeech, TextToSpeech.QUEUE_FLUSH, null);
					subs.showSubtitle(initialSpeech);
				}
			}

		} else {
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}
	}

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

	public static boolean isInstalled(Context ctx) {
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public void setmTts(TextToSpeech mTts) {
		this.mTts = mTts;
	}

	public void speak(String msg) {
		if (enabled){
			mTts.speak(msg, queueMode, null);
			subs.showSubtitle(msg);
		}
	}

	public void speak(View v) {
		if (enabled){
			mTts.speak(v.getContentDescription().toString(), queueMode, null);
			subs.showSubtitle(v.getContentDescription().toString());
		}
	}

	public void speak(List<String> msg) {
		if (enabled) {
			Iterator<String> it = msg.iterator();
			while (it.hasNext()){
				mTts.speak(" " + it.next() + " ", QUEUE_ADD, null);
				subs.showSubtitle(" " + it.next() + " ");
			}
		}
	}

	public void stop() {
		mTts.stop();
		mTts.shutdown();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(enabled ? 1 : 0);
		dest.writeInt(queueMode);
		dest.writeParcelable(subs.getsInfo(), flags);
	}
}
