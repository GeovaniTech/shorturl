package br.com.devpree.shorturl.exception;

public class UrlNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public UrlNotFoundException(String message) {
		super(message);
	}
}
