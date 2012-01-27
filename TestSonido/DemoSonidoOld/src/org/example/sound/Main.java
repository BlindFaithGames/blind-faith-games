package org.example.sound;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Main extends Activity{
	Screen sc;
	SoundManager SM;
	public enum Distance {V_CLOSE,CLOSE, FAR, V_FAR, TARGET}
	public Distance dist;
	public enum Side {RIGHT, LEFT, TARGET}
	public Side side;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SM = new SoundManager(Main.this);
        SM.addSound(0, R.raw.sound, Main.this);
        SM.addSound(1, R.raw.target, Main.this);
        sc = new Screen(this);
        sc.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
					int x = (int)event.getX();
					int y = (int)event.getY();
					int center =sc.getWidth()/2;
					int distan = x-center;//negative left - positive right
					String print = "X = " +Integer.toString(x)+" Y = "+Integer.toString(y);
					String print1 = "Dist. to target = "+Integer.toString(distan);
					Toast.makeText(Main.this, print+"\n"+print1, Toast.LENGTH_SHORT).show();
					dist = getDist(distan);
					side = getSide(distan);
					
					switch (side){
						case RIGHT: playResult(0);break;
						case LEFT:  playResult(1);break;
						case TARGET: SM.play(1);break;
					}
					return false;
			}
		});
        setContentView(sc);
    }
    
    public Distance getDist(int location){
    	int loc = Math.abs(location);
    	Distance d=null;
    	if (0<loc&&loc<sc.getWidth()/8) d=Distance.V_CLOSE;
    	else if (sc.getWidth()/8<loc&&loc<sc.getWidth()/4) d=Distance.CLOSE;
    	else if (sc.getWidth()/4<loc&&loc<sc.getWidth()*3/8) d=Distance.FAR;
    	else d=Distance.V_FAR;
    	return d;		
    }
    
    public Side getSide(int location){
    	Side s = null;
    	if (location>2) s=Side.RIGHT;
    	else if (location<-2) s=Side.LEFT;
    	else s=Side.TARGET;
    		return s;
    }
    
    public void playResult(int vol){
    	switch (dist){
			case CLOSE: 	SM.playLooped(0, 6, 2.0f, vol);break;
			case V_CLOSE: 	SM.playLooped(0, 5, 1.5f, vol);break;
			case FAR: 		SM.playLooped(0, 4, 1.0f, vol);break;
			case V_FAR: 	SM.playLooped(0, 3, 0.5f, vol);break;
		}
    }
}
