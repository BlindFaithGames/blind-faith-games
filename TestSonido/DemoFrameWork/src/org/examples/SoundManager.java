package org.examples;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.app.Activity;
import android.util.Log;

public class SoundManager {
	
	private static final String TAG = "SoundManager";

	private SoundEnv env;

	private Source left;
	private Source right;
	private Source up;
	private Source down;
	private Source center;
	
	public void initializeSounds(Activity a, MyView v) {
		try {
			/* First we obtain the instance of the sound environment. */
			this.env = SoundEnv.getInstance(a);

			/*
			 * Now we load the sounds into the memory that we want to play
			 * later. Each sound has to be buffered once only. To add new sound
			 * copy them into the assets folder of the Android project.
			 * Currently only mono .wav files are supported.
			 */
			Buffer upDown = env.addBuffer("upDown");
			Buffer rightLeft = env.addBuffer("rightLeft");
			Buffer center = env.addBuffer("center");
			/*
			 * To actually play a sound and place it somewhere in the sound
			 * environment, we have to create sources. Each source has its own
			 * parameters, such as 3D position or pitch. Several sources can
			 * share a single buffer.
			 */
			this.up = env.addSource(upDown);
			this.down = env.addSource(upDown);
			this.left = env.addSource(rightLeft);
			this.right = env.addSource(rightLeft);
			this.center = env.addSource(center);

			// Now we spread the sounds throughout the sound room.
			
			int width = v.getWidth1();
			int height = v.getHeight1();
			int imgH = v.getImgH();
			int imgW = v.getImgW();
			
			this.up.setPosition(width/2 - imgW/2 + 20, 20, 0);
			
			this.down.setPosition(width/2 - imgW/2 + 20, height - imgH + 20, 0);
			
			this.left.setPosition(20,height/2 - imgH/2 + 20, 0);
			
			this.right.setPosition(width - imgW + 20, height/2 - imgH/2 + 20, 0);
			
			this.center.setPosition(width/2 - imgW/2 + 20, height/2 - imgH/2 + 20, 0);
			
			this.up.setPitch(5.1f);

			this.left.setPitch(0.3f);
			
			this.env.setListenerOrientation(20);
		} catch (Exception e) {
			Log.e(TAG, "could not initialise OpenAL4Android", e);
		}
	}

	public void playAllSounds(){
		this.up.play(true);
		this.down.play(true);
		this.left.play(true);
		this.right.play(true);
		this.center.play(true);
	}
	
	public void stopAllSounds(){
		this.up.stop();
		this.down.stop();
		this.left.stop();
		this.right.stop();
		this.center.stop();
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

	public void setListenerPosition(float x, float y, float z) {
		env.setListenerPos(x, y, z);
	}
}
