package org.example.tinyEngineClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;


public class SpriteMap {
	
	private HashMap<String, Animation> map; 	// mapa de animaciones del spriteMap
	private Animation currentAnim; 			// animación en ejecución
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
		
		map = new HashMap<String, Animation>();
		
		this.frame = frame;
		finished = false;
		step = 0;
		lastFrame = frame;
	}
	
	public int getWidth(){
		int w = bitmap.getWidth() / nCol;
		return w;
	}
	public int getHeight(){
		int h = bitmap.getHeight() / nRow;
		return h;
	}
	
	
	/**
	 * Añade una animación a la tabla creándola desde cero.
	 * @param name
	 * @param frameList
	 * @param framesPerStep
	 * @param loop
	 */
	public void addAnim(String name, ArrayList<Integer> frameList, int framesPerStep, boolean loop){
		Animation aux = new Animation(name,frameList,framesPerStep,loop);
	
		map.put(name, aux);
	}

	// Manda ejecutar una animación de la tabla.
	public void playAnim(String name){
		// Guardamos la animación actual
		Animation oldAnim = currentAnim;

		// Buscamos la siguiente
		Iterator<Animation> it = map.values().iterator();
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
				if (!oldAnim.equals(currentAnim))	{
					currentFrame = 0;  // Reset animation because it has changed
					finished = false;
				}
			}
			else{
				frame = 0; // Reset animation
				finished = false;
			}
		}
	}

	/* Manda ejecutar una animación de la tabla, pero alterando su velocidad y su
	 * repetición durante esta ejecución (velocidad expresada en frames por paso).
	 */
	public void playAnim(String name, int framesPerStep, boolean loop) {	
		playAnim(name);
		if (currentAnim != null){
			currentAnim.setFrameDelay(framesPerStep);
			currentAnim.setLoop(loop);
		}
	}


	/* Si hay alguna animación ejecutándose, la para y fija el último frame por el
	 * que iba esta.
	 */
	public void stopAnim() {
		if (currentAnim != null) {
			//currentAnim.stop();
		}
	}
	
	private boolean nextFrame() {
		if (step >= currentAnim.getFramesPerStep() && currentAnim.getFramesPerStep() > 0) {
			step = 0;
			return true;
		}
		else {
			step++;
			return false;
		}
	}

	public void onUpdate() {
		if (currentAnim != null){
			if (!finished && nextFrame())
			{
				frame++;
				if (frame >= currentAnim.getFrameCount()) {
					if (!currentAnim.isLoop()) {
						finished = true;
						step = 0;
						lastFrame = currentAnim.getFrameList().get((currentAnim.getFrameCount())-1);
						frame = currentAnim.getFrameCount()-1;
					}
					else frame = 0;
				}
			}
		}
	}

	public void onDraw(int x, int y, Canvas canvas) {
		int w = bitmap.getWidth() / nCol;
		int h = bitmap.getHeight() / nRow;
		
		if (currentAnim != null){
			
			if(frame < currentAnim.getFrameList().size()){
				Integer posnum = currentAnim.getFrameList().get(frame);
			
				Point pos = numToXY(posnum);
				
				Rect actualFrame = new Rect(pos.x*w, pos.y*h, pos.x*w + w, pos.y*h + h);
				Rect dest = new Rect(x,y,x + w,y + h);
				canvas.drawBitmap(bitmap, actualFrame, dest, null);
			}
		}
	}

	private Point numToXY (int num) {
		return new Point(num % nCol, num / nCol);
	}

	@SuppressWarnings("unused")
	private int XYToNum(int x, int y) {
		return x*nCol+y;
	}
}