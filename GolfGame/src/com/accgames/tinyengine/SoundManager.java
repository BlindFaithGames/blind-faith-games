package com.accgames.tinyengine;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundManager {
	private SoundPool soundPool;
	private int soundID;
	private static HashMap<Integer, Integer> soundMap;
	boolean loaded = false;
	private static Context c;
	private AudioManager audioManager;
	private float volume;

	public SoundManager(Context context){
		// Load the sound
		c = context;
		if (soundPool !=null) soundPool.release();
		soundPool = new SoundPool(9, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					loaded = true;
				}
			});
		// Getting the user sound settings
		audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = actualVolume / maxVolume;
		
		soundMap = new HashMap<Integer,Integer>();
	}
	
	public void addSound(int index, int SoundID, Context mCon)
	{
	    int id = soundPool.load(mCon, SoundID, index);
		soundMap.put(index, id);
	}
	public void setPlay(int sound, int loop, float rate, int side){
		soundPool.setLoop(sound, loop);
		soundPool.setPriority(sound, 1);
		soundPool.setRate(sound, rate);
		if (side==1)soundPool.setVolume(sound, (float)0.0, volume);
		else if(side==2)soundPool.setVolume(sound, (float)0.0, volume);
		else soundPool.setVolume(sound, volume, volume);
	}
	
	public void play(int sound){
		stopSound(0);stopSound(1);
		// Is the sound loaded already?
		if (loaded) {
			soundID=soundMap.get(sound);
			soundPool.play(soundID, volume, volume, 1, 0, 1f);
			}
	}
	public void playLooped(int sound, int loop, float rate, int side){
		stopSound(0);stopSound(1);
		if (loaded){
			soundID=soundMap.get(sound);
			//id, left volume, right volume, priority of sound, loop, rate of play
			if (side==0) soundPool.play(soundID, 0.0f, volume, 1, loop, rate);
			else soundPool.play(soundID, volume, 0.0f, 1, loop, rate);
		}
		stopSound(sound);
	}
	
	public void stopSound(int sound){
		soundID=soundMap.get(sound);
		soundPool.stop(soundID);
	}

	public boolean isWiredHeadsetOn() {
		return  audioManager.isWiredHeadsetOn();
	}
}