package com.accgames.tinyEngineClasses;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/** La peor clase de la historia
 * 
 * */

public class VolumeManager {
	private static AudioManager amanager;
	
	public static AudioManager getAudioManager(Context c) {
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		return amanager;
	}
	
	public static void setVolume(Context c,int incr){
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		amanager.setStreamVolume(AudioManager.STREAM_MUSIC,incr , AudioManager.FLAG_SHOW_UI);
	}
	
	public static float getVolume(Context c){
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		return amanager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}
	
	public static void adjustStreamVolume(Context c, int direction){
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				direction, AudioManager.FLAG_SHOW_UI);
	}

	public static float getVolumeMax(Activity c) {
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		return amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
	
}
