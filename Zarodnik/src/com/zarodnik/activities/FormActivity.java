package com.zarodnik.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.accgames.others.Log;
import com.accgames.sound.TTS;
import com.zarodnik.R;

public class FormActivity extends Activity implements OnClickListener,
		OnFocusChangeListener, OnLongClickListener{
	
	private static int N_QUESTIONS = 12;
	
	private TTS textToSpeech;

	private int nQuestion; // Indicates the question currently enabled.
	private TextView t; // Current label question
	private RadioGroup ans; // Current radio group answer
	private RadioButton op0, op1, op2, op3, op4, op5;
	private int[] ansFocus;
	
	private Button buttonNext, buttonPrevious;
	private MultiAutoCompleteTextView textField;
	
	private Log l;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// If the user wants to send another form, firstly we remove the old
		// information
		Log.getLog().clearAnswers();
		l = Log.getLog();
		
		setContentView(R.layout.form);

		t = (TextView) findViewById(R.id.q);
		t.setOnClickListener(this);
		t.setOnFocusChangeListener(this);
		t.setOnLongClickListener(this);
		
		buttonNext = (Button) findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(this);
		buttonNext.setOnFocusChangeListener(this);
		buttonNext.setOnLongClickListener(this);
		buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
		buttonPrevious.setOnClickListener(this);
		buttonPrevious.setOnFocusChangeListener(this);
		buttonPrevious.setOnLongClickListener(this);
		
		ans = (RadioGroup) findViewById(R.id.ans);
		ans.setOnFocusChangeListener(this);
		
		op0 = (RadioButton) findViewById(R.id.radio0);
		op0.setOnFocusChangeListener(this);
		op0.setOnClickListener(this);
		op1 = (RadioButton) findViewById(R.id.radio1);
		op1.setOnFocusChangeListener(this);
		op1.setOnClickListener(this);
		op2 = (RadioButton) findViewById(R.id.radio2);
		op2.setOnFocusChangeListener(this);
		op2.setOnClickListener(this);
		op3 = (RadioButton) findViewById(R.id.radio3);
		op3.setOnFocusChangeListener(this);
		op3.setOnClickListener(this);
		op4 = (RadioButton) findViewById(R.id.radio4);
		op4.setOnFocusChangeListener(this);
		op4.setOnClickListener(this);
		op5 = (RadioButton) findViewById(R.id.radio5);
		op5.setOnFocusChangeListener(this);
		op5.setOnClickListener(this);
		
		textField = (MultiAutoCompleteTextView) findViewById(R.id.textfield);
		textField.setOnFocusChangeListener(this);
		textField.setOnEditorActionListener(new OnEditorActionListener() {

	        @Override
	        public boolean onEditorAction(TextView v, int actionId,
	                KeyEvent event) {
	            if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
	                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                in.hideSoftInputFromWindow(textField
	                        .getApplicationWindowToken(),
	                        InputMethodManager.HIDE_NOT_ALWAYS);
	            }
	            return false;
	        }
	    });
		
		nQuestion = 0;
		ansFocus = new int[N_QUESTIONS];
		for(int i = 0; i < 11; i++){
			ansFocus[i] = 5;
		}
		ansFocus[1] = 3;

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(
				MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.form_label) + ", " 
									+ getString(R.string.form_intro) 
									+ getString(R.string.form_instructions1));
	}

	@Override
	public void onClick(View v) {
		if(v != null){
			if(v instanceof RadioButton){
				RadioButton r = (RadioButton) v;
				textToSpeech.speak(getString(R.string.action_form) + " " + r.getText().toString());
			}else{
				if(SettingsActivity.getBlindInteraction(this)){
					textToSpeech.speak(v);
				}
				else{
					menuAction(v);
				}
			}
		}
	}

	private boolean menuAction(View v) {
		boolean res = false;
		switch (v.getId()) {
		case (R.id.buttonNext):
			saveQuestion();
			
			nQuestion++;
			
			if(nQuestion == N_QUESTIONS){
				System.out.println(l);
				this.finish();
				return true;
			}
			if(nQuestion == N_QUESTIONS-1)
				buttonNext.setText("Finish");
			
			nextQuestion();
			
			res = true;
			
			break;
		case (R.id.buttonPrevious):
			saveQuestion();
			
			nQuestion--;
			
			nextQuestion();
			
			res = true;
			
			break;
		}
		return res;
	}

	private void saveQuestion() {
		RadioButton r; 
		boolean found  = false;
		int i = 0;
		while(!found && i < ans.getChildCount()){
			if(ans.getChildAt(i) instanceof RadioButton){
				r = (RadioButton) ans.getChildAt(i);
				found = r.isChecked();
			}
			i++;
		}
		if(nQuestion == 11){
			l.addAnswer(nQuestion, textField.getText().toString());
		}else{
			if(found){
				if(ans.getChildAt(i-1) instanceof RadioButton){
					r = (RadioButton) ans.getChildAt(i-1);
					ansFocus[nQuestion] = i-1;
					l.addAnswer(nQuestion, r.getText().toString());
				}
			}
		}
	}

	private void nextQuestion() {
		ans.requestFocus();
		nextFocus();
		switch(nQuestion){
			case 0:
				t.setText(R.string.form_intro);
				t.setContentDescription(getString(R.string.form_intro));
				textToSpeech.speak(getString(R.string.form_label) + ". " 
									+ getString(R.string.form_intro) 
									+ getString(R.string.form_instructions1));
				op0.setVisibility(View.GONE);
				op1.setVisibility(View.GONE);
				op2.setVisibility(View.GONE);
				op3.setVisibility(View.GONE);
				op4.setVisibility(View.GONE);
				op5.setVisibility(View.GONE);
				buttonPrevious.setVisibility(View.GONE);
				nextAnswer();
			break;
			case 1:
				buttonPrevious.setVisibility(View.VISIBLE);
				op0.setVisibility(View.VISIBLE);
				op1.setVisibility(View.VISIBLE);
				op2.setVisibility(View.VISIBLE);
				op3.setVisibility(View.VISIBLE);
				op4.setVisibility(View.GONE);
				op5.setVisibility(View.GONE);
				t.setText(R.string.q1_context);
				t.setContentDescription(getString(R.string.q1_context));
				textToSpeech.speak(getString(R.string.form_instructions3) + " " + t.getContentDescription().toString());
				op0.setText(R.string.radio11);
				op0.setContentDescription(getString(R.string.radio11));
				op1.setText(R.string.radio12);
				op1.setContentDescription(getString(R.string.radio12));
				op2.setText(R.string.radio13);
				op2.setContentDescription(getString(R.string.radio13));
				op3.setText(R.string.na);
				op3.setContentDescription(getString(R.string.na));
				op3.setSelected(true);
				break;
			case 2:
				op4.setVisibility(View.VISIBLE);
				op5.setVisibility(View.VISIBLE);
				t.setText(R.string.q2_context);
				t.setContentDescription(getString(R.string.q2_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 3:
				t.setText(R.string.q3_context);
				t.setContentDescription(getString(R.string.q3_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 4:
				t.setText(R.string.q4_context);
				t.setContentDescription(getString(R.string.q4_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 5:
				t.setText(R.string.q5_context);
				t.setContentDescription(getString(R.string.q5_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 6:
				t.setText(R.string.q6_context);
				t.setContentDescription(getString(R.string.q6_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 7:
				t.setText(R.string.q7_context);
				t.setContentDescription(getString(R.string.q7_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 8:
				t.setText(R.string.q8_context);
				t.setContentDescription(getString(R.string.q8_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 9:
				t.setText(R.string.q9_context);
				t.setContentDescription(getString(R.string.q9_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 10:
				t.setText(R.string.q10_context);
				t.setContentDescription(getString(R.string.q10_context));
				textToSpeech.speak(t);
				nextAnswer();
				break;
			case 11:
				op0.setVisibility(View.GONE);
				op1.setVisibility(View.GONE);
				op2.setVisibility(View.GONE);
				op3.setVisibility(View.GONE);
				op4.setVisibility(View.GONE);
				op5.setVisibility(View.GONE);
				textField.setVisibility(View.VISIBLE);
				t.setText(R.string.q11_context);
				t.setContentDescription(getString(R.string.q11_context));
				textField.setContentDescription(getString(R.string.form_instructions4));
				textToSpeech.speak(getString(R.string.form_instructions2) + ". " + t.getContentDescription().toString());
				break;
		}
		
	}

	private void nextFocus() {
		View v;
		if(ans.getChildCount() < ansFocus[nQuestion]){
			v = ans.getChildAt(ans.getChildCount()-1);
		}else{
			v = ans.getChildAt(ansFocus[nQuestion]);
		}
		
		if(v instanceof RadioButton){
			RadioButton r = (RadioButton) v;
			r.setChecked(true);
		}
	}

	private void nextAnswer() {
		op4.setEnabled(true);
		op5.setEnabled(true);
		op0.setText(R.string.one_context);
		op0.setContentDescription(getString(R.string.one_context));
		op1.setText(R.string.two_context);
		op1.setContentDescription(getString(R.string.two_context));
		op2.setText(R.string.three_context);
		op2.setContentDescription(getString(R.string.three_context));
		op3.setText(R.string.four_context);
		op3.setContentDescription(getString(R.string.four_context));
		op4.setText(R.string.five_context);
		op4.setContentDescription(getString(R.string.five_context));
		op5.setText(R.string.na);
		op5.setContentDescription(getString(R.string.na));
		op5.setSelected(true);
	}

	/**
	 * OnFocusChangeListener Interface
	 * */
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if(v != null && v.getContentDescription() != null){
				textToSpeech.speak(v);
			}
		}
	}


	@Override
	public boolean onLongClick(View v) {
		return menuAction(v);
		 
	}

}