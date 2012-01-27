package com.minesweeper.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Version;


/**
 * Database entity
 *
 */

@PersistenceCapable
public class Log {

  @PrimaryKey 
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY) 
  private Long id;
  
  @Version
  @Column(name = "version")
  private Long version;
  @Persistent 
  private Date dueDate;
  @Persistent
  private String emailAddress;
  @Persistent
  private String userId;
  @Persistent
  private String tag; 
  @Persistent
  private List<Long> entries;
  @Persistent
  private String comment;
  @Persistent
  private List<String> formAnswers;

  public Log() {
		this.id = new Long(1);
		this.version = new Long(1);
		this.dueDate = new Date();
		this.emailAddress = "";
		this.userId = "3";
		this.tag = "";
		this.entries = new ArrayList<Long>();
		this.comment = "";
		this.formAnswers = new ArrayList<String>();
  }

	public Log(Long id, Long version, Date dueDate, String emailAddress,
			Boolean done, String name, String userId, String tag,
			List<Long> logEntries, String comment, List<String> formAnswers) {
		super();
		this.id = id;
		this.version = version;
		this.dueDate = dueDate;
		this.emailAddress = emailAddress;
		this.userId = userId;
		this.tag = tag;
		this.entries = logEntries;
		this.comment = comment;
		this.formAnswers = formAnswers;
	}

	public Date getDueDate() {
	    return dueDate;
	}

	public String getEmailAddress() {
	    return this.emailAddress;
	}
	
	public Long getId() {
	    return id;
	}

	public String getUserId() {
	    return userId;
	}
	
	public void setDueDate(Date dueDate) {
	    this.dueDate = dueDate;
	}

  	public String getTag() {
  		return tag;
  	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public List<Long> getLogEntries() {
		return entries;
	}
	
	public void setLogEntries(List<Long> logEntries) {
		this.entries = logEntries;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getFormAnswers() {
		return formAnswers;
	}
	
	public void setFormAnswers(List<String> formAnswers) {
		this.formAnswers = formAnswers;
	}
	
	public void setEmailAddress(String emailAddress) {
	    this.emailAddress = emailAddress;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
	  StringBuilder builder = new StringBuilder();
	  builder.append("Log [dueDate=");
	  builder.append(dueDate);
	  builder.append(", tag=");
	  builder.append(tag);
	  Iterator<Long> it = entries.iterator();
	  while(it.hasNext()){
		  Long entry = it.next();
		  builder.append(", Entry=");
		  builder.append(entry);
	  }
	  builder.append(", comment=");
	  builder.append(comment);
	  java.util.Iterator<String> i = formAnswers.iterator();
	  int counter = 1;
	  while(it.hasNext()){
		  String ans = i.next();
		  builder.append(", Answer" + counter + "=");
		  builder.append(ans);
		  counter++;
	  }
	  builder.append("]");
	  return builder.toString();
	}
	
	@Override
	public boolean equals(Object o){
		Log l = (Log) o;
		return l.id.equals(id);
	}
}
