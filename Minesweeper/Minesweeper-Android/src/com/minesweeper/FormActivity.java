package com.minesweeper;

import org.example.minesweeper.TTS;
import org.example.others.Log;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FormActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private TTS textToSpeech;

	private Button buttonNext1, buttonNext2, buttonFinish;
	private MultiAutoCompleteTextView ans14;

	private RadioGroup ans1, ans2, ans3, ans4, ans5, ans6, ans7, ans8, ans9,
			ans10, ans11, ans12, ans13;
	private RadioButton r1Selected, r2Selected, r3Selected, r4Selected,
			r5Selected, r6Selected, r7Selected, r8Selected, r9Selected,
			r10Selected, r11Selected, r12Selected, r13Selected;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// If the user wants to send another form, firstly we remove the old information
		Log.getLog().clearAnswers();
		
		setScreenContent(R.layout.form1);

		buttonNext1 = (Button) findViewById(R.id.buttonNext1);
		buttonNext1.setOnClickListener(this);
		buttonNext1.setOnFocusChangeListener(this);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(
				MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.form_label));
	}

	@Override
	public void onClick(View v) {
		boolean done = false;
		switch (v.getId()) {
		case (R.id.buttonNext1):
			saveResults(R.layout.form1);
			setScreenContent(R.layout.form2);
			buttonNext2 = (Button) findViewById(R.id.buttonNext2);
			buttonNext2.setOnClickListener(this);
			buttonNext2.setOnFocusChangeListener(this);
			done = true;
			break;
		case (R.id.buttonNext2):
			saveResults(R.layout.form2);
			setScreenContent(R.layout.form3);
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// only will trigger it if no physical keyboard is open
			mgr.showSoftInput(ans14, InputMethodManager.SHOW_IMPLICIT);
			buttonFinish = (Button) findViewById(R.id.button_finish);
			buttonFinish.setOnClickListener(this);
			buttonFinish.setOnFocusChangeListener(this);
			done = true;
			break;
		case (R.id.button_finish):
			saveResults(R.layout.form3);
			done = true;
			break;
		}
		if (!done) {
			textToSpeech.speak(v);
			textToSpeech.setQueueMode(TTS.QUEUE_ADD);
			if (groupContainId(ans1, v.getId()))
				textToSpeech.speak(getString(R.string.q1_context));
			else if (groupContainId(ans2, v.getId()))
				textToSpeech.speak(getString(R.string.q2_context));
			else if (groupContainId(ans3, v.getId()))
				textToSpeech.speak(getString(R.string.q3_context));
			else if (ans4 != null && groupContainId(ans4, v.getId()))
				textToSpeech.speak(getString(R.string.q4_context));
			else if (ans5 != null && groupContainId(ans5, v.getId()))
				textToSpeech.speak(getString(R.string.q5_context));
			else if (ans6 != null && groupContainId(ans6, v.getId()))
				textToSpeech.speak(getString(R.string.q6_context));
			else if (ans7 != null && groupContainId(ans7, v.getId()))
				textToSpeech.speak(getString(R.string.q7_context));
			else if (ans8 != null && groupContainId(ans8, v.getId()))
				textToSpeech.speak(getString(R.string.q8_context));
			else if (ans9 != null && groupContainId(ans9, v.getId()))
				textToSpeech.speak(getString(R.string.instructions_label));
			else if (ans10 != null && groupContainId(ans10, v.getId()))
				textToSpeech.speak(getString(R.string.q10_context));
			else if (ans11 != null && groupContainId(ans11, v.getId()))
				textToSpeech.speak(getString(R.string.q11_context));
			else if (ans12 != null && groupContainId(ans12, v.getId()))
				textToSpeech.speak(getString(R.string.q12_context));
			else if (ans13 != null && groupContainId(ans13, v.getId()))
				textToSpeech.speak(getString(R.string.q13_context));

			textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
		}
	}

	/**
	 * Sets the screen content based on the screen id.
	 */
	private void setScreenContent(int screenId) {
		setContentView(screenId);
		switch (screenId) {
		case R.layout.form1:
			setForm1ScreenContent();
			break;
		case R.layout.form2:
			setForm2ScreenContent();
			break;
		case R.layout.form3:
			setForm3ScreenContent();
			break;
		}
	}

	private void setForm3ScreenContent() {
		ans9 = (RadioGroup) findViewById(R.id.ans9);
		ans9.setOnFocusChangeListener(this);
		ans9.requestFocus();

		ans10 = (RadioGroup) findViewById(R.id.ans10);
		ans10.setOnFocusChangeListener(this);
		ans10.requestFocus();

		ans11 = (RadioGroup) findViewById(R.id.ans11);
		ans11.setOnFocusChangeListener(this);
		ans11.requestFocus();

		ans12 = (RadioGroup) findViewById(R.id.ans12);
		ans12.setOnFocusChangeListener(this);
		ans12.requestFocus();

		ans13 = (RadioGroup) findViewById(R.id.ans13);
		ans13.setOnFocusChangeListener(this);
		ans13.requestFocus();

		ans14 = (MultiAutoCompleteTextView) findViewById(R.id.ans14);
//		ans14.setOnClickListener(this);
		
		r9Selected = (RadioButton) findViewById(ans9.getCheckedRadioButtonId());
		setRadioContent(ans9);
		r10Selected = (RadioButton) findViewById(ans10
				.getCheckedRadioButtonId());
		setRadioContent(ans10);
		r11Selected = (RadioButton) findViewById(ans11
				.getCheckedRadioButtonId());
		setRadioContent(ans11);
		r12Selected = (RadioButton) findViewById(ans12
				.getCheckedRadioButtonId());
		setRadioContent(ans12);
		r13Selected = (RadioButton) findViewById(ans13
				.getCheckedRadioButtonId());
		setRadioContent(ans13);

	}

	private void setForm2ScreenContent() {
		ans4 = (RadioGroup) findViewById(R.id.ans4);
		ans4.setOnFocusChangeListener(this);
		ans4.requestFocus();

		ans5 = (RadioGroup) findViewById(R.id.ans5);
		ans5.setOnFocusChangeListener(this);
		ans5.requestFocus();

		ans6 = (RadioGroup) findViewById(R.id.ans6);
		ans6.setOnFocusChangeListener(this);
		ans6.requestFocus();

		ans7 = (RadioGroup) findViewById(R.id.ans7);
		ans7.setOnFocusChangeListener(this);
		ans7.requestFocus();

		ans8 = (RadioGroup) findViewById(R.id.ans8);
		ans8.setOnFocusChangeListener(this);
		ans8.requestFocus();

		r4Selected = (RadioButton) findViewById(ans4.getCheckedRadioButtonId());
		setRadioContent(ans4);
		r5Selected = (RadioButton) findViewById(ans5.getCheckedRadioButtonId());
		setRadioContent(ans5);
		r6Selected = (RadioButton) findViewById(ans6.getCheckedRadioButtonId());
		setRadioContent(ans6);
		r7Selected = (RadioButton) findViewById(ans7.getCheckedRadioButtonId());
		setRadioContent(ans7);
		r8Selected = (RadioButton) findViewById(ans8.getCheckedRadioButtonId());

	}

	private void setForm1ScreenContent() {
		ans1 = (RadioGroup) findViewById(R.id.ans1);
		ans1.setOnFocusChangeListener(this);
		ans1.requestFocus();

		ans2 = (RadioGroup) findViewById(R.id.ans2);
		ans2.setOnFocusChangeListener(this);
		ans2.requestFocus();
		
		ans3 = (RadioGroup) findViewById(R.id.ans3);
		ans3.setOnFocusChangeListener(this);
		ans3.requestFocus();

		r1Selected = (RadioButton) findViewById(ans1.getCheckedRadioButtonId());
		setRadioContent(ans1);
		r2Selected = (RadioButton) findViewById(ans2.getCheckedRadioButtonId());
		setRadioContent(ans2);
		r3Selected = (RadioButton) findViewById(ans3.getCheckedRadioButtonId());
		setRadioContent(ans3);
	}

	private void setRadioContent(RadioGroup group) {
		int i = 0;
		while (i < group.getChildCount()) {
			group.getChildAt(i).setOnFocusChangeListener(this);
			group.getChildAt(i).setOnClickListener(this);
			i++;                                       
		}
	}

	private void saveResults(int form) {
		switch (form) {
		case (R.layout.form1):
			manageForm1();
			break;
		case (R.layout.form2):
			manageForm2();
			break;
		case (R.layout.form3):
			manageForm3();
			break;
		}
	}

	private void manageForm1() {
		Log l = Log.getLog();
		l.addAnswer(r1Selected.getText().toString());
		l.addAnswer(r2Selected.getText().toString());
		l.addAnswer(r3Selected.getText().toString());
	}

	private void manageForm2() {
		Log l = Log.getLog();
		l.addAnswer(r4Selected.getText().toString());
		l.addAnswer(r5Selected.getText().toString());
		l.addAnswer(r6Selected.getText().toString());
		l.addAnswer(r7Selected.getText().toString());
		l.addAnswer(r8Selected.getText().toString());
	}

	private void manageForm3() {
		Log l = Log.getLog();
		l.addAnswer(r9Selected.getText().toString());
		l.addAnswer(r10Selected.getText().toString());
		l.addAnswer(r11Selected.getText().toString());
		l.addAnswer(r12Selected.getText().toString());
		l.addAnswer(r13Selected.getText().toString());
		l.addAnswer(ans14.getText().toString());
		
		finish();
	}

	/**
	 * OnFocusChangeListener Interface
	 * */
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			textToSpeech.speak(v);
			textToSpeech.setQueueMode(TTS.QUEUE_ADD);
			if (groupContainId(ans1, v.getId()))
				textToSpeech.speak(getString(R.string.q1_context));
			else if (groupContainId(ans2, v.getId()))
				textToSpeech.speak(getString(R.string.q2_context));
			else if (groupContainId(ans3, v.getId()))
				textToSpeech.speak(getString(R.string.q3_context));
			else if (ans4 != null && groupContainId(ans4, v.getId()))
				textToSpeech.speak(getString(R.string.q4_context));
			else if (ans5 != null && groupContainId(ans5, v.getId()))
				textToSpeech.speak(getString(R.string.q5_context));
			else if (ans6 != null && groupContainId(ans6, v.getId()))
				textToSpeech.speak(getString(R.string.q6_context));
			else if (ans7 != null && groupContainId(ans7, v.getId()))
				textToSpeech.speak(getString(R.string.q7_context));
			else if (ans8 != null && groupContainId(ans8, v.getId()))
				textToSpeech.speak(getString(R.string.q8_context));
			else if (ans9 != null && groupContainId(ans9, v.getId()))
				textToSpeech.speak(getString(R.string.instructions_label));
			else if (ans10 != null && groupContainId(ans10, v.getId()))
				textToSpeech.speak(getString(R.string.q10_context));
			else if (ans11 != null && groupContainId(ans11, v.getId()))
				textToSpeech.speak(getString(R.string.q11_context));
			else if (ans12 != null && groupContainId(ans12, v.getId()))
				textToSpeech.speak(getString(R.string.q12_context));
			else if (ans13 != null && groupContainId(ans13, v.getId()))
				textToSpeech.speak(getString(R.string.q13_context));

			textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);

		}
	}

	private boolean groupContainId(RadioGroup group, int id) {
		int i = 0;
		boolean found = false;
		while (!found && i < group.getChildCount()) {
			found = group.getChildAt(i).getId() == id;
			i++;
		}
		return found;
	}

}