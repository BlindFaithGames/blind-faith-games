package com.accgames.tinyEngineClasses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the music manager of the game
 */

public class Music {
	private static HashMap<Integer, MediaPlayer> sounds = null;

	private static Music m = null;
	
	private Music(){
		sounds = new HashMap<Integer, MediaPlayer>();
	}
	
	public static Music getInstanceMusic(){
		if (m == null)
			return m = new Music();
		else 
			return m;
	}
	
	/** Stop old song and start new one */
	public void play(Context context, int resource, boolean looping) {
		// Start music only if not disabled in preferences
		stop(resource);
		
		MediaPlayer mp;
		mp = MediaPlayer.create(context, resource);
		sounds.put(resource, mp);
		mp.setLooping(looping);
		mp.start();
	}
	
	/** Stop old song and start new one */
	public void playWithBlock(Context context, int resource, boolean looping) {
		stop(resource);
		
		MediaPlayer mp;
		mp = MediaPlayer.create(context, resource);
		sounds.put(resource, mp);
		mp.setLooping(looping);
		mp.start();
		
		while(mp.isPlaying()){};
	}


	public void setVolume(float leftVolume, float rightVolume, int resource){
		sounds.get(resource).setVolume(leftVolume, rightVolume);
	}
	
	/** Stop the music */
	public void stop(int resource) {
		MediaPlayer mp = sounds.get(resource);
		if (mp != null) {
				mp.stop();
				mp.release();
				mp = null;
				sounds.remove(resource);
		}
	}

	public boolean isPlaying(int sound) {
			MediaPlayer mp  = sounds.get(sound);
		 	if(mp != null){
		 		return mp.isPlaying();
		 	}
		 		return false;
	}
	
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
