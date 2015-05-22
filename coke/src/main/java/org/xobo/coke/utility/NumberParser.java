package org.xobo.coke.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class NumberParser {
	public static Long parseLong(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return ((Number) obj).longValue();
		} else {
			Long val = null;
			try {
				val = Long.parseLong(obj.toString());
			} catch (Exception e) {

			}
			return val;
		}

	}

	public static Integer parseInteger(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return ((Number) obj).intValue();
		} else {
			Integer val = null;
			try {
				val = Integer.parseInt(obj.toString());
			} catch (Exception e) {

			}
			return val;
		}
	}

	public static List<Integer> parseIntegerList(String str) {
		return parseIntegerList(str, ",");
	}

	public static List<Integer> parseIntegerList(String str, String regex) {
		List<Integer> list = new ArrayList<Integer>();
		if (StringUtils.hasText(str)) {
			String[] tokens = str.split(regex);
			for (String token : tokens) {
				Integer val = parseInteger(token);
				if (val != null) {
					list.add(val);
				}
			}
		}
		return list;
	}
}
