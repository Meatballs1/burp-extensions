package com.gdssecurity.utils;

public class HttpHelper {



	public static String removeHttpHeader(String strHttpHeaders,String strPattern)
	{

		strHttpHeaders = strHttpHeaders.replaceAll(strPattern, "");

		return strHttpHeaders;
	}

	public static String updateContentLength (String strHttpHeaders,int length)
	{

		strHttpHeaders = strHttpHeaders.replaceAll("(Content-Length: )(\\d+)","$1" + length);

		return strHttpHeaders;
	}

}
