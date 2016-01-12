package org.xobo.coke.utility;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
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


  public static Map<String, Object> toMap(String json) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      JavaType javaType =
          mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class,
              String.class, Object.class);
      return mapper.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String prettyJSON(Object object) {
    String json = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return json;
  }

}
