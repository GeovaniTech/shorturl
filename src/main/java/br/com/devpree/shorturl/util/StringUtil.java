package br.com.devpree.shorturl.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {
	private static Integer DEFAULT_MAX_LENGTH = 6;
	
	/**
	 * Creates a random Strings combining letters and numbers. Is is possible
	 * to limit the size of the String.
	 * 
	 * @param maxLength
	 * @return
	 */
	public static String generateRandomString(Integer maxLength) {
		Integer length = maxLength == null ? DEFAULT_MAX_LENGTH : maxLength;	
		
		return RandomStringUtils.secure().next(length, true, true);
	}
	
	public static boolean isNotNull(String value) {
		if(value != null && !value.trim().equals("")) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNull(String value) {
		if(value == null || value.trim().equals("")) {
			return true;
		}
		
		return false;
	}
}
