package br.com.devpree.shorturl.request;

public class TOCreateUrlRequestRestModel {
	private String completeUrl;
	private String customId;
	private Integer length;
	
	public TOCreateUrlRequestRestModel() {
	}
	
	public String getCompleteUrl() {
		return completeUrl;
	}
	public void setCompleteUrl(String completeUrl) {
		this.completeUrl = completeUrl;
	}
	public String getCustomId() {
		return customId;
	}
	public void setCustomId(String customId) {
		this.customId = customId;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
}
