package org.xobo.coke.utility;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;

public class CokeURLUtils {
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

	public static String getWebAppPath(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int portNumber = request.getServerPort();
		String contextPath = request.getContextPath();
		return scheme + "://" + serverName + ":" + portNumber + contextPath;
	}
}
