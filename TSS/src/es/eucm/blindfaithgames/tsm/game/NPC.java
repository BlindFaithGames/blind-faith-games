package es.eucm.blindfaithgames.tsm.game;

import java.util.ArrayList;
import java.util.List;

public class NPC {
	
	private List<String> dialog;
	private String name;
	
	private int nextDialog;
	
	public NPC(){
		dialog = new ArrayList<String>();
		nextDialog = 0;
		name = "";
	}
	
	public NPC(String n){
		dialog = new ArrayList<String>();
		name = n;
		nextDialog = 0;
	}
	
	public NPC(List<String> dialog){
		this.dialog = dialog;
		nextDialog = 0;
		name = "";
	}
	
	public NPC(List<String> dialog, String name){
		this.dialog = dialog;
		nextDialog = 0;
		this.name = name;
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
}
