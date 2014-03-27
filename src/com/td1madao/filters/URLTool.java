package com.td1madao.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLTool {
public static boolean isURL(String urlString) {
	String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
	Pattern patt = Pattern. compile(regex );
	Matcher matcher = patt.matcher(urlString);
	boolean isMatch = matcher.matches();
	if (!isMatch) {
		return false;
	} 
	return true;
}

}
