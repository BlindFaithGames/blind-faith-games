package com.accgames.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * 
 * This class manages the device volume.
 * 
 *  @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class VolumeManager {
	
	private static AudioManager amanager; // Singleton
	
	/**
	 * Given a context, returns a instance of AudioManager Class.
	 *
	 * @param c context where the manager will be used.
	 * 
	 * @return instance of Audio Class.
	 * */
	public static AudioManager getAudioManager(Context c) {
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		return amanager;
	}
	
	/**
	 * Sets the manager volume in the context c, with an increment incr.
	 * 
	 *  @param c context where the manager will be used.
	 *  @param incr volume change.
	 * 
	 * */
	public static void setVolume(Context c,int incr){
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		amanager.setStreamVolume(AudioManager.STREAM_MUSIC,incr , AudioManager.FLAG_SHOW_UI);
	}
	
	/**
	 * Gets the manager in the context c.
	 *
	 * @param c context where the manager will be used.
	 * 
	 * @return stream volume.
	 * 
	 * */
	public static float getVolume(Context c){
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		return amanager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}
	
	/**
	 * Adjusts the volume of a particular stream by one step in a direction.
	 * 
	 *  @param c context where the manager will be used.
	 *  @param direction The direction to adjust the volume. One of ADJUST_LOWER, ADJUST_RAISE, or ADJUST_SAME.
	 *  
	 * */
	public static void adjustStreamVolume(Context c, int direction){
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				direction, AudioManager.FLAG_SHOW_UI);
	}

	/**
	 * Returns the maximum volume index for a particular stream.
	 * 
	 * @param c context where the manager will be used.
	 * 
	 * @return the maximum valid volume index for the stream.
	 * 
	 * */
	public static float getVolumeMax(Activity c) {
		if (amanager == null) {
			amanager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		}
		return amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
	
}
