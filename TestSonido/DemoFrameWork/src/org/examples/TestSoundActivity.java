package org.examples;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;


public class TestSoundActivity extends Activity {

	private final static String	TAG	= "TestSoundActivity";
	private SoundManager sm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(TAG, "onCreate()");
		
		sm = new SoundManager();
		
		Display display = this.getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		int height = display.getHeight();
		
		MyView v = new MyView(this,sm,width,height);
		
		this.setContentView(v);

		sm.initializeSounds(this,v);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
		sm =	 new SoundManager();
		
		Display display = this.getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		int height = display.getHeight();
		
		MyView v = new MyView(this,sm,width,height);
		
		this.setContentView(v);
		
		sm.initializeSounds(this,v);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume(");

		/*
		 * Start playing all sources. 'true' as parameter specifies that the
		 * sounds shall be played as a loop.
		 */
		this.sm.playAllSounds();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause()");

		// Stop all sounds
		this.sm.stopAllSounds();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");

		// Be nice with the system and release all resources
		this.sm.stopAllSources();
		this.sm.release();
	}

	@Override
	public void onLowMemory() {
		this.sm.onLowMemory();
	}
}
