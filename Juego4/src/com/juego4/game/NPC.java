package com.juego4.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPC {
	
	private List<String> questions;
	private Map<Integer,List<String>> answer;
	private String name;
	
	public NPC(){
		questions = new ArrayList<String>();
		answer = new HashMap<Integer, List<String>>();
	}
	
	public NPC(String n){
		questions = new ArrayList<String>();
		answer = new HashMap<Integer, List<String>>();
		name = n;
	}
	
	public NPC(List<String> questions, Map<Integer,List<String>> answer){
		this.answer = answer;
		this.questions = questions;
	}

}
