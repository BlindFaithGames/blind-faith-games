package es.eucm.blindfaithgames.tss.game;

import java.util.ArrayList;
import java.util.List;

public class NPC {
	
	private List<String> dialog;
	private String name;
	
	private int nextDialog;
	private boolean transition;
	
	public NPC(){
		dialog = new ArrayList<String>();
		nextDialog = 0;
		name = "";
		transition = false;
	}
	
	public NPC(String n){
		dialog = new ArrayList<String>();
		name = n;
		nextDialog = 0;
		transition = false;
	}
	
	public NPC(List<String> dialog){
		this.dialog = dialog;
		nextDialog = 0;
		name = "";
		transition = false;
	}
	
	public NPC(List<String> dialog, String name, boolean transition){
		this.dialog = dialog;
		nextDialog = 0;
		this.name = name;
		this.transition = transition;
	}
	
	public String getName(){
		return name;
	}

	public String nextDialog(){
		String speech = null;
		if(nextDialog < dialog.size()){
			speech = dialog.get(nextDialog);
			nextDialog++;
		}
		return speech;
	}

	public String getDialog() {
		String result = "";
		for(String s:dialog){
			result += " " + s; 
		}
		return result;
	}

	public boolean getTransition() {
		return transition;
	}

	public void reset() {
		nextDialog = 0;
	}
}
