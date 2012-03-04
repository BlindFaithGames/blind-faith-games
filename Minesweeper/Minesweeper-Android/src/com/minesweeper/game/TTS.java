package com.minesweeper.game;

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
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez This class
 *         implements OnInitListener Interface
 * 
 */
public class TTS implements TextToSpeech.OnInitListener, Parcelable {

	private static final String TAG = "Synthesizer";

	public static final int QUEUE_FLUSH = TextToSpeech.QUEUE_FLUSH;

	public static final int QUEUE_ADD = TextToSpeech.QUEUE_ADD;

	private TextToSpeech mTts;

	private int queueMode;

	private boolean enabled;

	private String initialSpeech;

	private static final String appname = "IVONA Text-to-Speech HQ";

	private static final String SYSTEM_TTS = "com.svox.pico";

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
	}

	public TTS(Context ctxt, int queueMode) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		initialSpeech = null;
		enabled = true;
		this.queueMode = queueMode;
	}

	public TTS(Context ctxt, String initialSpeech, int queueMode) {
		if (isInstalled(ctxt))
			this.mTts = new TextToSpeech(ctxt, this);

		this.initialSpeech = initialSpeech;
		enabled = true;
		this.queueMode = queueMode;
	}

	public void setContext(Context ctxt) {
		if (isInstalled(ctxt))
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
		if (enabled)
			mTts.speak(msg, queueMode, null);
	}

	public void speak(View v) {
		if (enabled)
			mTts.speak(v.getContentDescription().toString(), queueMode, null);
	}

	public void speak(List<String> msg) {
		if (enabled) {
			Iterator<String> it = msg.iterator();
			while (it.hasNext())
				mTts.speak(" " + it.next() + " ", QUEUE_ADD, null);
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
	}

//	public static boolean isTalkBackAccessibilityEnabled(Context c) {
//		int accessibilityEnabled = 0;
//		boolean accessibilityFound = false;
//		final String TALKBACK_ACCESSIBILITY_SERVICE = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService";
//
//		try {
//			accessibilityEnabled = Settings.Secure.getInt(
//					c.getContentResolver(),
//					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
//			Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
//		} catch (SettingNotFoundException e) {
//			Log.d(TAG,
//					"Error finding setting, default accessibility to not found: "
//							+ e.getMessage());
//		}
//
//		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
//				':');
//
//		if (accessibilityEnabled == 1) {
//			Log.d(TAG, "***ACCESSIBILIY IS ENABLED***: ");
//
//			String settingValue = Settings.Secure.getString(
//					c.getContentResolver(),
//					Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//			Log.d(TAG, "Setting: " + settingValue);
//
//			if (settingValue != null) {
//				TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
//				splitter.setString(settingValue);
//				while (splitter.hasNext()) {
//					String accessabilityService = splitter.next();
//					Log.d(TAG, "Setting: " + accessabilityService);
//					if (accessabilityService
//							.equalsIgnoreCase(TALKBACK_ACCESSIBILITY_SERVICE)) {
//						Log.d(TAG,
//								"We've found the correct setting - accessibility is switched on!");
//						return true;
//					}
//				}
//			}
//
//			Log.d(TAG, "***END***");
//		} else {
//			Log.d(TAG, "***ACCESSIBILIY IS DISABLED***");
//		}
//		return accessibilityFound;
//	}
}
