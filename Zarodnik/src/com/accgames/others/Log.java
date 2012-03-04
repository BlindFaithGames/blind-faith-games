package com.accgames.others;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Log {
	
	private static Log log = null;
	
	private String tag; // game name
	private Date date;
	private Map<Integer,Entry> logEntries; 
	private String comment;
	private int logCounter;
	
	private ArrayList<String> formAnswers;
	
	public Log getLog(){
		if(log == null)
			return log = new Log();
		else
			return log;
	}
	
	protected Log(){
		logEntries = new HashMap<Integer,Entry>();
		logCounter = 0;
		
		formAnswers = new ArrayList<String>();
		
		tag = "DEFAULT";
		
		/*Calendar c = Calendar.getInstance();
		date.setDate(c.get(Calendar.DAY_OF_MONTH));
		date.setMonth(c.get(Calendar.MONTH));
		date.setYear(c.get(Calendar.YEAR));
		date.setHours(c.get(Calendar.HOUR));
		date.setMinutes(c.get(Calendar.MINUTE));
		date.setSeconds(c.get(Calendar.SECOND));*/
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}

	public void setComment(String comment){
		this.comment = comment;
	}
	
	protected int addEntry(String tag, String activeConfigurationSettings, String type, String path, String comment){
		logCounter++;
		logEntries.put(logCounter, new Entry(logCounter,tag,activeConfigurationSettings,type,path,comment));
		return logCounter;
	}
	
	public String getEntry(int key){
		Entry e = logEntries.get(key);
		if(e != null)
			return e.toString();
		else
			return "ENTRY DELETED";
	}
	
	public Set<Integer> getEntryKeys(){
		return logEntries.keySet();
	}
	
	public Entry removeEntry(int key){
		return logEntries.remove(key);
	}
	
	// Form methods
	
	public void addAnswer(int question, String answer){
		formAnswers.add(question,answer);
	} 
	
	public String getAnswer(int question){
		return formAnswers.get(question);
	}
	
	public String removeAnswer(int question){
		return formAnswers.remove(question);
	}
	
	public void toXML(){
		// TODO 
	}
}
