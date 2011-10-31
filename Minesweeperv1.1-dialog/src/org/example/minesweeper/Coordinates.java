package org.example.minesweeper;

import android.content.Context;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez
 * This class implements the coordinates manager of the game
 */

public class Coordinates {
	/**get coordinates state from settings menu**/
	public static boolean getCoordinates (Context context){
		if (Prefs.getCoor(context))
			return true;
		else return false;
	};
}
