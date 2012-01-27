package org.pielot.helloopenal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelloOpenAL extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		System.loadLibrary("openal");
		System.loadLibrary("openaltest");
		int ret = play("/sdcard/wav/lake.wav");
		Log.i("HelloOpenAL", "" + ret);
	}

	private native int play(String filename);
}