package org.example.minigolf.activities;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseLinear;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;
import org.example.minigolf.golf.Ball;
import org.example.minigolf.golf.CatapultDetector;
import org.example.minigolf.golf.CatapultDetector.ICatapultDetectorListener;

import android.util.Log;

public class GolfGameActivity extends BaseGameActivity implements IOnSceneTouchListener,ICatapultDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;
	private static final String TAG = "Minigolf";

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera camera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private Ball ball;
	private TiledTextureRegion mBallTextureRegion;
	private RepeatingSpriteBackground mBackground;
	private TextureRegion mHoleTextureRegion;
	private CatapultDetector mCatapultDetector;

	@Override
	public Engine onLoadEngine() {
		this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.camera));
	}

	@Override
	public void onLoadResources() {
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(256, 256, TextureOptions.DEFAULT);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.mBallTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "ballA.png", 0, 0, 2, 4);
        mHoleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "hole.png", 128, 0);
//        this.mBackground = new RepeatingSpriteBackground(CAMERA_WIDTH, CAMERA_HEIGHT, this.mEngine.getTextureManager(), new AssetBitmapTextureAtlasSource(this, "background_grass.png"));

        this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);


	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
//		scene.setBackground(this.mBackground);
		scene.setBackground(new ColorBackground(0, 0.8784f, 0));
		scene.setOnAreaTouchTraversalFrontToBack();

		// Creamos el detector de la catapulta
		this.mCatapultDetector = new CatapultDetector(this);
		this.mCatapultDetector.setEnabled(true);
		
		final int centerX = (CAMERA_WIDTH - this.mBallTextureRegion.getTileWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mBallTextureRegion.getTileHeight()) / 2;
		
		final int posY = CAMERA_HEIGHT - mBallTextureRegion.getTileHeight();
		/* Dibujamos la bola. */
		ball = new Ball(centerX, posY, this.mBallTextureRegion);
		scene.attachChild(ball);

		/* Create the hole and add it to the scene. */
        final Sprite hole = new Sprite(centerX, 0, mHoleTextureRegion);
        scene.attachChild(hole);

		scene.setOnSceneTouchListener(this);
		scene.setTouchAreaBindingEnabled(true);
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.ball != null){
			this.mCatapultDetector.onTouchEvent(pSceneTouchEvent);
		}
		return true;
	}

	@Override
	public void onCharge(CatapultDetector pCatapultDetector,
			TouchEvent pTouchEvent, float pDistance, float pAngle) {
		Log.d(TAG, "Cargando... {Distancia:" + pDistance + ", angulo: " + pAngle + "}");
		this.ball.setRotation(pAngle);
		
	}

	@Override
	public void onShoot(CatapultDetector pCatapultDetector,
			TouchEvent pTouchEvent, float pDistance, float pAngle) {
		Log.d(TAG, "Disparo... {Distancia:" + pDistance + ", angulo: " + pAngle + "}");
		this.reanimate();
	}
	
    private void reanimate() {
        this.runOnUpdateThread(new Runnable() {
                @Override
                public void run() {
	                ball.clearEntityModifiers();
	
	                final float x = ball.getX();
	                ball.setPosition(x, 0);
	                ball.registerEntityModifier(new MoveModifier(3,x,x,CAMERA_HEIGHT-ball.getHeight(),0,
	                		EaseLinear.getInstance()));
                    ball.animate(7, true);
                }
        });
}

}