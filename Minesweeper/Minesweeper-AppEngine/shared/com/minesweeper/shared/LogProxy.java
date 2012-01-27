package com.minesweeper.shared;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.minesweeper.server.Log", locator = "com.minesweeper.server.LogLocator")
public interface LogProxy extends ValueProxy {

	Long getVersion();
	void setVersion(Long version);

	Long getId();
	void setId(Long id);
	
	public Date getDueDate();
	public void setDueDate(Date dueDate);
	
	public String getEmailAddress();
	public void setEmailAddress(String emailAddress);
	
	public String getUserId();
	public void setUserId(String userId);
	
  	public String getTag();
	public void setTag(String tag);
	
	public List<Long> getLogEntries();
	public void setLogEntries(List<Long> logEntries);
	
	public String getComment(); 
	public void setComment(String comment);

	public List<String> getFormAnswers();
	public void setFormAnswers(List<String> formAnswers);
}
