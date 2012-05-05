package es.eucm.blindfaithgames.engine.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.app.Activity;

/**
 * This class manages everything about 3D sound.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */
public class Sound3DManager {
	
	private SoundEnv env; // OpenAL bridge

	protected Map<String,List<Source>> sources; // Map<soundName, setSources>
	
	protected Map<String,Buffer> buffers; // openAL bridge stuff
	
	private static Sound3DManager sm = null; // singleton
	
	/**
	 * Unique constructor of the class
	 * 
	 * @param a Context where the class is instantiated.
	 * 
	 * */
	public Sound3DManager(Activity a){
		sources = new HashMap<String,List<Source>>();
		buffers = new HashMap<String, Buffer>();
		this.env = SoundEnv.getInstance(a);
		this.env.setListenerOrientation(20);
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 	
	/**
	 * Gets a unique instance of Sound3DManager. Singleton pattern.
	 * 
	 * @return An instance of Sound3DManager.
	 * 
	 * */
	public static Sound3DManager getSoundManager(Activity a){
		if(sm == null){
			return sm = new  Sound3DManager(a);
		}else{
			return sm;
		}
	}
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	/**
	 * To indicate where the listener is on the screen.
	 * 
	 * */
	public void setListenerPosition(float x, float y, float z) {
		env.setListenerPos(x, y, z);
	}
	
	/**
	 * To indicate where the listener is looking in the space.
	 * 
	 * */
	public void setListenerOrientation(float heading) {
		env.setListenerOrientation(heading);
	}
	
// ----------------------------------------------------------- Others ----------------------------------------------------------- 	
	/**
	 * Adds a new source to the 3D sound engine using a sound name which exists in the assets directory.
	 * 
	 * @param soundName Source name.
	 * 
	 * @return the source created.
	 * 
	 * */
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
	
	/**
	 * Called when the amount of memory is low.
	 * 
	 * */
	public void onLowMemory() {
		this.env.onLowMemory();
	}

	/**
	 * Stops all sound sources.
	 * 
	 * */
	public void stopAllSources() {
		this.env.stopAllSources();
	}
	
	/**
	 * Releases memory if it's needed.
	 * 
	 * */
	public void release() {
		this.env.release();
	}
	
	/**
	 * Plays every 3d sound source created by the application.
	 * 
	 * */
	public void playAllSources() {
		this.env.playAllSources(true);
	}
}
