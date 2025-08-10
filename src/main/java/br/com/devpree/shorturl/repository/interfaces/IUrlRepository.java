package br.com.devpree.shorturl.repository.interfaces;

import br.com.devpree.shorturl.to.TOUrlDetails;

public interface IUrlRepository {
	/**
	 * Gets the URL Details by ShortUrl from Firebase FireStore
	 * 
	 * @param shortUrl
	 * @return
	 * @throws Exception
	 */
	public TOUrlDetails getTOUrlByShortUrl(String shortUrl) throws Exception;
	
	/**
	 * Creates the URL on Firebase FireStore
	 * 
	 * @param completeUrl
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public TOUrlDetails createShortUrl(String completeUrl, Integer length) throws Exception;
}
