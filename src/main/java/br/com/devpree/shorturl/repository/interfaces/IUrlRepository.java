package br.com.devpree.shorturl.repository.interfaces;

import br.com.devpree.shorturl.to.TOUrlDetails;

public interface IUrlRepository {
	public TOUrlDetails getTOUrlByShortUrl(String shortUrl) throws Exception;
	public TOUrlDetails createShortUrl(String completeUrl, Integer length) throws Exception;
}
