package org.xobo.coke.utility;

import java.io.UnsupportedEncodingException;

import org.springframework.web.util.UriUtils;

public class UTF8URLEncoder {
	public static String encode(String value) {
		String encodedValue;
		try {
			encodedValue = UriUtils.encodePath(value, "UTF-8");
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
