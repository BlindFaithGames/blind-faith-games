package com.accgames.sound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 
 * This class implements the music manager using MediaPlayer class.
 * 
 * 
 *
 */

public class Music {
	private static HashMap<Integer, MediaPlayer> sounds = null; // Map<ResourceId, Mediaplayer Object> Links a resource witch a Mediaplayer object

	private static SubtitleManager subs; // Manages the transcription of a game music
	
	private static Music m = null; // singleton
	
	/**
	 * Private constructor for singleton pattern.
	 * 
	 * */
	private Music(){
		sounds = new HashMap<Integer, MediaPlayer>();
		subs = new SubtitleManager();
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 	
	/**
	 * Singleton getter.
	 * 
	 * */
	public static Music getInstanceMusic(){
		if (m == null)
			return m = new Music();
		else 
			return m;
	}
	
// ----------------------------------------------------------- Others ----------------------------------------------------------- 
	/**
	 * Enables the subtitle system in the context "c".
	 * 
	 * @param context Context currently active.
	 * @param sInfo All information needed to play subtitles.
	 * 
	 * */
	public void enableTranscription(Context c, SubtitleInfo sInfo){
		subs = new SubtitleManager(c, sInfo);
		subs.setEnabled(true);
		if(sInfo != null)
				subs.setsInfo(sInfo);

	}
	/**
	 * 
	 * Disables the subtitle system in the current context. If It has already disabled, It do nothing.
	 * 
	 * */
	public void disableTranscription(){
		if(subs != null){
			subs.setEnabled(false);
		}
	}
	
	/** 
	 * Stops old song and starts new one. 
	 * 
	 * @param context Context where the sound will be played.
	 * @param resource Sound which will be played.
	 * @param looping Indicates if the sound will be reset if finishes. 
	 * 
	 * */
	public void play(Context context, int resource, boolean looping) {
		// Start music only if not disabled in preferences
		stop(resource);
		
		MediaPlayer mp;
		mp = MediaPlayer.create(context, resource);
		sounds.put(resource, mp);
		mp.setLooping(looping);
		mp.start();
		
		if(subs != null){
			String aux = subs.getOnomatopeia(resource);
			subs.setDuration(mp.getDuration()/1000);
			if(aux != null)
				subs.showSubtitle(aux);
			else
				subs.showSubtitle(Integer.toString(resource));
		}
	}
	
	/** 
	 * 
	 * Stops old song and starts new one.
	 * 
	 * @param context Context where the sound will be played.
	 * @param resource Sound which will be played.
	 * @param looping If it's true the sound will be reset after finish. 
	 * 
	 *  */
	public void playWithBlock(Context context, int resource, boolean looping) {
		stop(resource);
		
		MediaPlayer mp;
		mp = MediaPlayer.create(context, resource);
		sounds.put(resource, mp);
		mp.setLooping(looping);
		mp.start();
		
		if(subs != null){
			String aux = subs.getOnomatopeia(resource);
			subs.setDuration(mp.getDuration()/1000);
			if(aux != null)
				subs.showSubtitle(aux);
			else
				subs.showSubtitle(Integer.toString(resource));
		}
		
		while(mp.isPlaying()){};
	}

	/**
	 * Changes the volume of a sound.
	 * 
	 * @param leftVolume the volume of the left speaker.
	 * @param rightVolume the volume of the left speaker.
	 * @param resource Sound id in the application.
	 * 
	 * */
	public void setVolume(float leftVolume, float rightVolume, int resource){
		sounds.get(resource).setVolume(leftVolume, rightVolume);
	}
	
	/** 
	 * 
	 * Stops a music mapped with id "resource". 
	 * 
	 * @param resource Sound id in the application.
	 * 
	 * */
	public void stop(int resource) {
		MediaPlayer mp = sounds.get(resource);
		if (mp != null) {
				mp.stop();
				mp.release();
				mp = null;
				sounds.remove(resource);
		}
	}

	/**
	 * Checks if a resource given by parameter is being played.
	 * 
	 * @param resource Sound id in the application.
	 * 
	 * */
	public boolean isPlaying(int resource) {
			MediaPlayer mp  = sounds.get(resource);
		 	if(mp != null){
		 		return mp.isPlaying();
		 	}
		 		return false;
	}
	

	/**
	 * 
	 * Stops all sound source currently active in the class.
	 * 
	 * */
	public void stopAllResources() {
		if(sounds != null){
			Integer n;
			Set<Integer> keys = sounds.keySet();
			Iterator<Integer> it = keys.iterator();
			while(it.hasNext()){
				n = it.next();
				MediaPlayer mp = sounds.get(n);
				if (mp != null) {
						mp.stop();
						mp.release();
						mp = null;
						sounds.remove(n);
				}
			}
		}
	}
}
