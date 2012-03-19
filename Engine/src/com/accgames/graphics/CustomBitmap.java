package com.accgames.graphics;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Class that provides methods to resize a bitmap.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 */

public class CustomBitmap {

	/**
	 * Scales bm depending of newHeight and newWidth.
	 * 
	 *  @param bm Bitmap which will be scaled.
	 *  @param newHeight New bitmap height.
	 *  @param newWidth New bitmap width.
	 *  @return Scaled bitmap.
	 * 
	 * */
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		
		Matrix matrix = new Matrix();
		// resize the bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}
	
}
