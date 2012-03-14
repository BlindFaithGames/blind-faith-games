package com.accgames.others;

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
		this.configurationSettings = configurationSettings;
		this.type = type;
		this.path = path;
		this.setComment(comment);
	}
	
	public String toString(){
		return tag + " " + timestamp.toGMTString() +  " " 
				+ configurationSettings + " " + type + " " + path;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
