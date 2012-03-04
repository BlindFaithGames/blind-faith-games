package com.accgames.others;

import java.util.Calendar;
import java.util.Date;

public class Entry {
	
	private int id;
	private String tag;
	private Date timestamp;
	private String configurationSettings;
	
	// Event information
	private String type;
	private String path;
	
	// Random info
	private String comment;
	
	public Entry(int id, String tag, String configurationSettings, String type, String path,String comment){
		this.id = id;
		this.tag = tag;
		
		/*Calendar c = Calendar.getInstance();
		timestamp.setDate(c.get(Calendar.DAY_OF_MONTH));
		timestamp.setMonth(c.get(Calendar.MONTH));
		timestamp.setYear(c.get(Calendar.YEAR));
		timestamp.setHours(c.get(Calendar.HOUR));
		timestamp.setMinutes(c.get(Calendar.MINUTE));
		timestamp.setSeconds(c.get(Calendar.SECOND));*/
		
		this.configurationSettings = configurationSettings;
		this.type = type;
		this.path = path;
		this.comment =comment;
	}
	
	public String toString(){
		return tag + " " + timestamp.toGMTString() +  " " 
				+ configurationSettings + " " + type + " " + path;
	}
}
