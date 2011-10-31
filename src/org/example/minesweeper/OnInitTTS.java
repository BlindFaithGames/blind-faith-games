package org.example.minesweeper;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.tts.TextToSpeech;
import android.util.Log;


/**
 * 
 * 
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez This class
 *         implements OnInitListener Interface
 *
 */
public class OnInitTTS  implements TextToSpeech.OnInitListener {
	private static final String TAG = "Synthesizer";
	private TextToSpeech mTts;
	private String initialSpeech;
	
	
	public OnInitTTS(TextToSpeech mTts){	
		this.mTts = mTts;
		initialSpeech = null;
	}
	
	public OnInitTTS(TextToSpeech mTts, String initialSpeech) {
		this.mTts = mTts;
		this.initialSpeech = initialSpeech;
	}

	@Override
	public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
            }
            else{
            	// The TTS engine has been successfully initialized.
            	if (initialSpeech != null)
            		mTts.speak(initialSpeech,TextToSpeech.QUEUE_FLUSH ,null);
            }
            
        } else {
            // Initialization failed.
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
	}
	
	public static boolean isInstalled(Context ctx) {
	    Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(intent,   
                PackageManager.MATCH_DEFAULT_ONLY);  
        return list.size() > 0;  
	}

	public void setmTts(TextToSpeech mTts) {
		this.mTts = mTts;
	}
	
}
