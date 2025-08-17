package br.com.devpree.shorturl.request;

public class TOCreateUrlRequestRestModel {
	private String longUrl;
	private String customAlias;
	private Integer length;
	
	public TOCreateUrlRequestRestModel() {
	}
	
	public Integer getLength() {
		return length;
	}
	
	public void setLength(Integer length) {
		this.length = length;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getCustomAlias() {
		return customAlias;
	}

	public void setCustomAlias(String customAlias) {
		this.customAlias = customAlias;
	}
}
