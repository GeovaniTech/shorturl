package br.com.devpree.shorturl.to;

import java.util.Date;

public class TOUrlDetails {
	private String shortUrl;
	private String completeUrl;
	private Date creationDate;
	private long views;
	
	public String getShortUrl() {
		return shortUrl;
	}
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}
	public String getCompleteUrl() {
		return completeUrl;
	}
	public void setCompleteUrl(String completeUrl) {
		this.completeUrl = completeUrl;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public long getViews() {
		return views;
	}
	public void setViews(long views) {
		this.views = views;
	}
}
