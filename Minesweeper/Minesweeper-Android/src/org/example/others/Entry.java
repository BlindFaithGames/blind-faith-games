package org.example.others;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Entry {
	
	public static final String DATE_FORMAT_NOW = "HH:mm:ss dd-MM-yyyy";
	
	private String tag;
	private Date timestamp;
	private String configurationSettings;
	
	// Event information
	private String type;
	private String path;
	
	// Random info
	private String comment;
	
	public Entry(String tag, String configurationSettings, String type, String path,String comment){
		this.tag = tag;
		
	    Calendar cal = Calendar.getInstance();
	    timestamp = cal.getTime();
		
		this.configurationSettings = configurationSettings;
		this.type = type;
		this.path = path;
		this.comment =comment;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getConfigurationSettings() {
		return configurationSettings;
	}

	public void setConfigurationSettings(String configurationSettings) {
		this.configurationSettings = configurationSettings;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String toString(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return tag + " " + sdf.format(timestamp) +  " " 
				+ configurationSettings + " " + type + " " + path + " " + comment;
	}
}
