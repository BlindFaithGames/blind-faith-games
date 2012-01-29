package org.example.tinyEngineClasses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class SpriteMap {
	
	private HashMap<String, AnimatedSprite> map; 	// mapa de animaciones del spriteMap
	private AnimatedSprite currentAnim; 			// animación en ejecución
	private Bitmap bitmap;							// Imagen cargada
	
	private int frame; 								// frame actual en la animación en ejecución (de 0 a frameCount-1).
	private int lastFrame; 							/* sprite en el que termina la última animación 
													ejecutada (de 0 a nCol*nRow-1).*/
	private int step; 								/* pasos del juego ejecutados desde la última actualización de 
													la animación en curso.*/
	private int nCol; 								// número de columnas del spriteMap.
	private int nRow; 								// número de filas del spriteMap.
	private boolean finished; 						// indica si la animación actual ha terminado.
	
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
	 * Añade una animación a la tabla creándola desde cero.
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

	// Manda ejecutar una animación de la tabla.
	private void playAnim(String name){
		// Guardamos la animación actual
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
				// Si había animación anterior puede que sea la misma
				// y habrá que borrar la _oldAnim
				if (!oldAnim.equals(currentAnim))	{
					currentFrame = 0; // Reseteamos la animación porque es diferente
					finished = false;
				}
			}
			else{
				// Como no había anim anterior, reiniciamos
				frame = 0; // Reseteamos la animación porque es diferente
				finished = false;
			}
		}
	}

	/* Manda ejecutar una animación de la tabla, pero alterando su velocidad y su
	 * repetición durante esta ejecución (velocidad expresada en frames por paso).
	 */
	private void playAnim(String name, int framesPerStep, boolean loop) {	
		playAnim(name);
		
		currentAnim.setFrameDelay(framesPerStep);
//		currentAnim.setLoop(loop);
	}


	/* Si hay alguna animación ejecutándose, la para y fija el último frame por el
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