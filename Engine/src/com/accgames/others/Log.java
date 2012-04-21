package com.accgames.others;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Used to store every meaningful event in the games that we want to send to our remote server.
 * 
 * @author Javier �lvarez & Gloria Pozuelo.
 * 
 * */
@SuppressWarnings("unused")
public class Log {
	
	private static Log log = null; // Used to implement the singleton pattern
	
	private String tag; // Game name
	private Date date; // Log creation date
	private Map<Integer,Entry> logEntries; // Log Entries

	private String comment; // Any random stuff about a game or whatever
	private int logCounter; // Number of entries
	
	private ArrayList<String> formAnswers; // Answer about a possible game questionnaire.
	
	/**
	 * Constructor of the class. Singleton
	 * 
	 * */
	protected Log(){
		logEntries = new HashMap<Integer,Entry>();
		logCounter = 0;
		
		formAnswers = new ArrayList<String>();
		
		tag = "DEFAULT";
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------	
	
	/**
	 * Gets a unique instance of Input. Singleton pattern.
	 * 
	 * @return An instance of Input.
	 * 
	 * */
	public static Log getLog(){
		if(log == null)
			return log = new Log();
		else
			return log;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getAnswer(int question){
		return formAnswers.get(question);
	}
	
	public Set<Integer> getEntryKeys(){
		return logEntries.keySet();
	}

	public String getEntry(int key){
		Entry e = logEntries.get(key);
		if(e != null)
			return e.toString();
		else
			return "ENTRY DELETED";
	}

// ----------------------------------------------------------- Setters -----------------------------------------------------------	
	
	public void setTag(String tag){
		this.tag = tag;
	}

	public void setComment(String comment){
		this.comment = comment;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Adds a new log entry.
	 * 
	 * @param tag An id common to an entry set.
	 * @param configurationSettings String that contains information related with the game configuration.
	 * @param type Event type.
	 * @param path ClassPath where the event that we want to save is created.
	 * @param comment  Any random stuff about a game or whatever
	 * 
	 * @return Entry id in the log.
	 * */
	public int addEntry(String tag, String activeConfigurationSettings, String type, String path, String comment){
		logCounter++;
		logEntries.put(logCounter, new Entry(logCounter,tag,activeConfigurationSettings,type,path,comment));
		return logCounter;
	}

	/**
	 * Removes an log entry.
	 * 
	 * @param key Entry id.
	 * 
	 * @return The entry removed.
	 * */
	public Entry removeEntry(int key){
		return logEntries.remove(key);
	}
	
	/**
	 * Adds a form answer 
	 * 
	 * @param question The number of the question whose answer will be added.
	 * @param answer Answer in string format.
	 * */
	public void addAnswer(int question, String answer){
		formAnswers.add(question,answer);
	} 
	/**
	 * Removes a form answer.
	 *  
	 * @param question The number of the question whose answer will be removed.
	 * 
	 * @return The removed answer.
	 * */
	public String removeAnswer(int question){
		return formAnswers.remove(question);
	}

	public void clearAnswers() {
		if(formAnswers != null)
			formAnswers.clear();
	}
}
