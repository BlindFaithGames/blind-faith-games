package org.example.minigolf.activities;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the music manager of the game
 */

public class Music {
	private static MediaPlayer mp = null;

	/** Stop old song and start new one */
	public static void play(Context context, int resource) {
		stop(context);
		// Start music only if not disabled in preferences
			mp = MediaPlayer.create(context, resource);
			mp.setLooping(true);
			mp.start();
	}

	/** Stop the music */
	public static void stop(Context context) {
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}
}
