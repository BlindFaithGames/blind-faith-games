package com.minesweeper.server;

import java.util.Date;

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
public class Entry {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Version
	@Column(name = "version")
	private Long version;
	
	@Persistent
	private String tag;
	@Persistent
	private Date timestamp;
	@Persistent
	private String configurationSettings;
	
	// Event information
	@Persistent
	private String type;
	@Persistent
	private String path;
	@Persistent
	private String comment;
	
	public Entry(Long version, String tag, Date timestamp,
			String configurationSettings, String type, String path,
			String comment) {
		this.id = new Long(1);
		this.version = version;
		this.tag = tag;
		this.timestamp = timestamp;
		this.configurationSettings = configurationSettings;
		this.type = type;
		this.path = path;
		this.comment = comment;
	}

	public Entry() {
		this.id = new Long(1);
		this.version = new Long(1);
		this.tag = "";
		this.timestamp = new Date();
 		this.configurationSettings = "";
		this.type = "";
		this.path = "";
		this.comment = "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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

	@Override
	public String toString() {
		return "Entry [id=" + id + ", version=" + version + ", tag=" + tag
				+ ", timestamp=" + timestamp + ", configurationSettings="
				+ configurationSettings + ", type=" + type + ", path=" + path
				+ ", comment=" + comment + "]";
	}
	
	@Override
	public boolean equals(Object o){
		Entry e = (Entry) o;
		return e.getId().equals(id);	
	}

}
