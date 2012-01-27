package com.minesweeper.shared;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "com.minesweeper.server.Entry", locator = "com.minesweeper.server.EntryLocator")
public interface EntryProxy extends ValueProxy{
	
	public Long getId();
	public void setId(Long id);
	
	public Long getVersion();
	public void setVersion(Long version);

	public String getTag();
	public void setTag(String tag);
	
	public Date getTimestamp();
	public void setTimestamp(Date timestamp);

	public String getConfigurationSettings();
	public void setConfigurationSettings(String configurationSettings);
	
	public String getType();
	public void setType(String type);

	public String getPath();
	public void setPath(String path);

	public String getComment();
	public void setComment(String comment);
}
