package org.example.tinyEngineClasses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class SpriteMap {
	
	private HashMap<String, AnimatedSprite> map; 	// mapa de animaciones del spriteMap
	private AnimatedSprite currentAnim; 			// animaci�n en ejecuci�n
	private Bitmap bitmap;							// Imagen cargada
	
	private int frame; 								// frame actual en la animaci�n en ejecuci�n (de 0 a frameCount-1).
	private int lastFrame; 							/* sprite en el que termina la �ltima animaci�n 
													ejecutada (de 0 a nCol*nRow-1).*/
	private int step; 								/* pasos del juego ejecutados desde la �ltima actualizaci�n de 
													la animaci�n en curso.*/
	private int nCol; 								// n�mero de columnas del spriteMap.
	private int nRow; 								// n�mero de filas del spriteMap.
	private boolean finished; 						// indica si la animaci�n actual ha terminado.
	
	private int currentFrame;
	
	public SpriteMap(int rows, int cols, Bitmap bitmap, int frame){
		this.bitmap = bitmap;
		nRow = rows;
		nCol = cols;
		
		this.frame = frame;
		finished = false;
		step = 0;
		lastFrame = frame;
	}
	
	
	/**
	 * A�ade una animaci�n a la tabla cre�ndola desde cero.
	 * @param bitmap
	 * @param name
	 * @param frameList
	 * @param framesPerStep
	 * @param loop
	 */
	private void addAnim(Bitmap bitmap, String name, Vector<Integer> frameList, int framesPerStep, boolean loop){
		AnimatedSprite aux = new AnimatedSprite();
		aux.Initialize(bitmap, bitmap.getHeight(), bitmap.getWidth(), framesPerStep);
		map.put(name, aux);
	}

	// Manda ejecutar una animaci�n de la tabla.
	private void playAnim(String name){
		// Guardamos la animaci�n actual
		AnimatedSprite oldAnim = currentAnim;

		// Buscamos la siguiente
		Iterator it = map.values().iterator();
		Map.Entry e = null;
		boolean found = false;
		while (!found && it.hasNext()){
			found = it.next().equals(map.get(name));
		}
		
		if (!found)
			currentAnim = oldAnim; // Could not find animation
		else {
			currentAnim = map.get(name);
			if (oldAnim != null) {
				// Si hab�a animaci�n anterior puede que sea la misma
				// y habr� que borrar la _oldAnim
				if (!oldAnim.equals(currentAnim))	{
					currentFrame = 0; // Reseteamos la animaci�n porque es diferente
					finished = false;
				}
			}
			else{
				// Como no hab�a anim anterior, reiniciamos
				frame = 0; // Reseteamos la animaci�n porque es diferente
				finished = false;
			}
		}
	}

	/* Manda ejecutar una animaci�n de la tabla, pero alterando su velocidad y su
	 * repetici�n durante esta ejecuci�n (velocidad expresada en frames por paso).
	 */
	private void playAnim(String name, int framesPerStep, boolean loop) {	
		playAnim(name);
		
		currentAnim.setFrameDelay(framesPerStep);
//		currentAnim.setLoop(loop);
	}


	/* Si hay alguna animaci�n ejecut�ndose, la para y fija el �ltimo frame por el
	 * que iba esta.
	 */
	private void stopAnim() {
		if (currentAnim != null) {
			currentAnim.stop();
		}
	}

	private void onUpdate() {
		if (currentAnim != null){
			currentAnim.onUpdate();
		}
	}


	private void onDraw(int x, int y, Canvas canvas) {
		int w = bitmap.getWidth() / nCol;
		int h = bitmap.getHeight() / nRow;
		if (currentAnim != null) {
			currentAnim.onDraw(w, h, canvas);
		}
	}
}