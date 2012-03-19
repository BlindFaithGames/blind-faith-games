package com.accgames.others;

import java.util.Date;

/**
 * Contains information about the games that will be sent to the AppEngine server.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */
public class Entry {
	
	private int id; // Entry number given by its log.
	private String tag; // Entry number given by its log.
	private Date timestamp; // The date where the entry is created.
	private String configurationSettings; // String that contains information related with the game configuration.
	
	// Event information
	private String type; // Event type.
	private String path; // ClassPath where the event that we want to save is created.
	
	// Random info
	private String comment; // Any random stuff about a game or whatever
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param id Entry number given by its log.
	 * @param tag An id common to an entry set.
	 * @param configurationSettings String that contains information related with the game configuration.
	 * @param type Event type.
	 * @param path ClassPath where the event that we want to save is created.
	 * @param comment  Any random stuff about a game or whatever
	 * 
	 * */
	public Entry(int id, String tag, String configurationSettings, String type, String path,String comment){
		this.id = id;
		this.tag = tag;
		this.configurationSettings = configurationSettings;
		this.type = type;
		this.path = path;
		this.setComment(comment);
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 
	
	public String getComment() {
		return comment;
	}
	
	public int getId() {
		return id;
	}
	
// ----------------------------------------------------------- Setters ----------------------------------------------------------- 
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
// ----------------------------------------------------------- Others ----------------------------------------------------------- 	
	
	public String toString(){
		return tag + " " + timestamp.toGMTString() +  " " 
				+ configurationSettings + " " + type + " " + path;
	}
}
