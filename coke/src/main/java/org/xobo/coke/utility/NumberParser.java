package org.xobo.coke.utility;

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
}
