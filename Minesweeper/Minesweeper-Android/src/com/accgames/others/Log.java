package com.accgames.others;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Log {

	private static Log log = null;
	
	public static final String NONE = "None";
	public static final String ONCREATE = "OnCreate";
	public static final String DEVICE = "Device";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String EXIT = "Exit";
	public static final String KEY_EVENT = "KeyEvent";
	public static final String TAP_EVENT = "TapEvent";
	public static final String DOUBLE_TAP_EVENT = "DoubleTapEvent";
	public static final String TRIPLE_TAP_EVENT = "TripleTapEvent";
	
	private String tag; // game name
	private Date date;
	private Map<Integer,Entry> logEntries; 
	private String comment;
	private int logCounter;
	
	private ArrayList<String> formAnswers;
	
	public static  Log  getLog(){
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
	
	public String getTag() {
		return tag;
	}

	public Date getDate() {
		return date;
	}

	public Map<Integer, Entry> getLogEntries() {
		return logEntries;
	}

	public String getComment() {
		return comment;
	}

	public ArrayList<String> getFormAnswers() {
		return formAnswers;
	}

	public void setTag(String tag){
		this.tag = tag;
	}

	public void setComment(String comment){
		this.comment = comment;
	}
	
	public int addEntry(String tag, String activeConfigurationSettings, String type, String path, String comment){
		logCounter++;
		logEntries.put(logCounter, new Entry(tag,activeConfigurationSettings,type,path,comment));
		return logCounter;
	}
	
	public Entry getEntry(int key){
		Entry e = logEntries.get(key);
		if(e != null)
			return e;
		else
			return null;
	}
	
	public Set<Integer> getEntryKeys(){
		return logEntries.keySet();
	}
	
	public Entry removeEntry(int key){
		return logEntries.remove(key);
	}
	
	// Form methods
	
	public void addAnswer(String answer){
		formAnswers.add(answer);
	} 
	
	public String getAnswer(int question){
		return formAnswers.get(question);
	}
	
	public String removeAnswer(int question){
		return formAnswers.remove(question);
	}
	
	public void clearAnswers(){
		formAnswers.clear();
	}
	
	public void toXML(){
		// TODO 
	}

	public void clear() {
		logEntries.clear();
	}

}
