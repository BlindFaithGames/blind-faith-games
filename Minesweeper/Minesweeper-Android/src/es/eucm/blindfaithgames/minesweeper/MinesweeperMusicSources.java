package es.eucm.blindfaithgames.minesweeper;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class MinesweeperMusicSources {
	
	private static final int other_side = R.raw.main;
	private static final int passing_action  = R.raw.game;

	
	public static Map<Integer, String> getMap(Context c) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		result.put(other_side, c.getString(R.string.other_side));
		result.put(passing_action, c.getString(R.string.passing_action));

		return result;
	}
}
