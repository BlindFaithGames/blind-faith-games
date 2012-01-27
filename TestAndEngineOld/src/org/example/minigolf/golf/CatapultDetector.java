package org.example.minigolf.golf;

import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.BaseDetector;

import android.view.MotionEvent;

public class CatapultDetector extends BaseDetector {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float ANGLE_CONSTANT = 90;
	private static final float DEFAULT_MAX_DISTANCE = 80;

	// ===========================================================
	// Fields
	// ===========================================================

	// Listener for the Detector
	private final ICatapultDetectorListener mCatapultDetectorListener;


	// First Touch
	private float mFirstX;
	private float mFirstY;

	private float mMaxDistance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CatapultDetector(final ICatapultDetectorListener pCatapultDetectorListener) {
		this(DEFAULT_MAX_DISTANCE, pCatapultDetectorListener);
	}

	public CatapultDetector(final float pMaxDistance,final ICatapultDetectorListener pCatapultDetectorListener) {
		this.setMaxDistance(pMaxDistance);
		this.mCatapultDetectorListener = pCatapultDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setMaxDistance(float mMaxDistance) {
		this.mMaxDistance = mMaxDistance;
	}

	public float getMaxDistance() {
		return mMaxDistance;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected float getX(final TouchEvent pTouchEvent) {
		return pTouchEvent.getX();
	}

	protected float getY(final TouchEvent pTouchEvent) {
		return pTouchEvent.getY();
	}

	// ===========================================================

	// Methods for/from SuperClass/Interfaces

	// ===========================================================

	@Override
	protected boolean onManagedTouchEvent(TouchEvent pSceneTouchEvent) {
		final float touchX = this.getX(pSceneTouchEvent);
		final float touchY = this.getY(pSceneTouchEvent);
		
		switch (pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.mFirstX = touchX;
				this.mFirstY = touchY;
				return true;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				final float distanceX = Math.abs(touchX - this.mFirstX);
				final float distanceY = Math.abs(touchY - this.mFirstY);
				final float distance = Math.min((float)Math.hypot((double) distanceX, (double) distanceY), mMaxDistance);
				
				final double angleX = touchX - this.mFirstX;
				final double angleY = touchY - this.mFirstY;
				final float angle = (float) Math.toDegrees(Math.atan2(angleY, angleX)) + ANGLE_CONSTANT;
				
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_MOVE) {
					this.mCatapultDetectorListener.onCharge(this,pSceneTouchEvent, distance, angle);
				}
				else{	
					this.mCatapultDetectorListener.onShoot(this, pSceneTouchEvent, distance, angle);
				}
				return true;
			default:
				return false;
		}
	}

	public interface ICatapultDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onCharge(final CatapultDetector pCatapultDetector,
				final TouchEvent pTouchEvent, final float pDistance,
				final float pAngle);

		public void onShoot(final CatapultDetector pCatapultDetector,
				final TouchEvent pTouchEvent, final float pDistance,
				final float pAngle);

	}

}
