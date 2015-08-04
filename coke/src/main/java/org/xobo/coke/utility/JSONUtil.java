package org.xobo.coke.utility;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

	public static String toJSON(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
