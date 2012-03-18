package com.accgames.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.app.Activity;
import android.content.Context;

public class Sound3DManager {
	
	private SoundEnv env;

	protected Map<String,List<Source>> sources;
	
	protected Map<String,Buffer> buffers;
	
	private static SubtitleInfo sInfo;

	private static Sound3DManager sm = null;
	
	public Sound3DManager(Activity a){
		sources = new HashMap<String,List<Source>>();
		buffers = new HashMap<String, Buffer>();
		this.env = SoundEnv.getInstance(a);
		this.env.setListenerOrientation(20);
	}
	
	public static Sound3DManager getSoundManager(Activity a){
		if(sm == null){
			return sm = new  Sound3DManager(a);
		}else{
			return sm;
		}
	}

	public void setListenerPosition(float x, float y, float z) {
		env.setListenerPos(x, y, z);
	}
	
	public void setListenerOrientation(float heading) {
		env.setListenerOrientation(heading);
	}
	
	public Source addSource(String soundName){
		Source source = null;
		Buffer b; List<Source> lAux;
		if(soundName != null){
			b = buffers.get(soundName);
			if(b == null){
				try {
					b = env.addBuffer(soundName);
					buffers.put(soundName, b);
					source = env.addSource(b);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				source = env.addSource(b);
				lAux = sources.get(soundName);
				if(lAux == null){
					lAux = new ArrayList<Source>();
					lAux.add(source);
					sources.put(soundName,lAux);
				}
				else{
					lAux.add(source);
				}
			}
			
		}
		return source;
	}
	
	public void onLowMemory() {
		this.env.onLowMemory();
	}

	public void stopAllSources() {
		this.env.stopAllSources();
	}

	public void release() {
		this.env.release();
	}

	public void playAllSources() {
		this.env.playAllSources(true);
	}
}
