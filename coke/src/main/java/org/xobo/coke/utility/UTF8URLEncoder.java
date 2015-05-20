package org.xobo.coke.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UTF8URLEncoder {
	public static String encode(String value) {
		String encodedValue;
		try {
			encodedValue = URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			encodedValue = value;
		}
		return encodedValue;
	}

	public static String getContentDisposition(String fileName) {
		String encodedFilename = encode(fileName);
		return "attachment;filename=\"" + encodedFilename + "\";filename*=utf-8''" + encodedFilename;
	}
}
