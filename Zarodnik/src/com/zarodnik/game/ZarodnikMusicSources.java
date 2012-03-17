package com.zarodnik.game;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.zarodnik.R;

public class ZarodnikMusicSources {
	
	private static final int appear = R.raw.appear;
	private static final int apple_bite  = R.raw.apple_bite;
	private static final int barn_beat = R.raw.barn_beat;
	private static final int bip = R.raw.bip;
	private static final int bubble = R.raw.bubble;
	private static final int die = R.raw.die;
	private static final int frost_walz = R.raw.frost_walz;
	private static final int predator = R.raw.predator;
	private static final int prey = R.raw.prey;
	private static final int prey_dead = R.raw.prey_dead;
	private static final int radio = R.raw.radio;
	private static final int start = R.raw.start;
	private static final int the_path_of_the_goblin = R.raw.the_path_of_the_goblin_king;
	
	public static Map<Integer, String> getMap(Context c) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		result.put(appear, c.getString(R.string.appear_ono));
		result.put(apple_bite, c.getString(R.string.apple_bite_ono));
		result.put(barn_beat, c.getString(R.string.barn_beat_ono));
		result.put(bip, c.getString(R.string.bip_ono));
		result.put(bubble, c.getString(R.string.bubble_ono));
		result.put(die, c.getString(R.string.die_ono));
		result.put(frost_walz, c.getString(R.string.frost_walz_ono));
		result.put(predator, c.getString(R.string.predator_ono));
		result.put(prey, c.getString(R.string.prey_ono));
		result.put(prey_dead, c.getString(R.string.prey_dead_ono));
		result.put(radio, c.getString(R.string.radio_ono));
		result.put(start, c.getString(R.string.start_ono));
		result.put(the_path_of_the_goblin, c.getString(R.string.the_path_of_the_goblin_ono));
		
		return result;
	}
}
