package org.xobo.coke.utility;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JSONUtil {
  public static final String IGNORE_PROPERTEIS_FILTER = "ignoreProperteisFilter";

  public static String toJSON(Object object) {
    ObjectMapper mapper = getObjectMapper();
    StringWriter writer = new StringWriter();
    try {
      mapper.writeValue(writer, object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  public static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }


  public static String toJSON(Object object, String... ignoreProperties) {
    ObjectMapper mapper = getObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.addMixIn(Object.class, PropertyFilterMixIn.class);
    FilterProvider filterProvider = new SimpleFilterProvider().addFilter(IGNORE_PROPERTEIS_FILTER,
        SimpleBeanPropertyFilter.serializeAllExcept(ignoreProperties));
    try {
      mapper.writer(filterProvider).writeValue(writer, object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }


  public static Object toObject(String json) {
    return toObject(json, Object.class);
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    ObjectMapper mapper = getObjectMapper();
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object toObject(TreeNode treeNode) {
    return toObject(treeNode, Object.class);
  }

  public static <T> T toObject(TreeNode treeNode, Class<T> clazz) {
    ObjectMapper mapper = getObjectMapper();
    try {
      return mapper.treeToValue(treeNode, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static JsonNode toJsonNode(Object obj) {
    if (obj == null) {
      return null;
    }
    ObjectMapper mapper = getMapper();
    return mapper.valueToTree(obj);
  }

  public static JsonNode toJsonNode(String json) {
    Map<String, Object> parameter = new HashMap<String, Object>();

    if (json == null) {
      return null;
    }
    ObjectMapper mapper = getMapper();
    try {
      return mapper.readTree(json);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }



  public static Map<String, Object> toMap(String json) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = getObjectMapper();
    try {
      JavaType javaType =
          mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class,
              String.class, Object.class);
      return mapper.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<String, Object> toMap(Object obj) {
    ObjectMapper mapper = getObjectMapper();
    JavaType javaType =
        mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class,
            String.class, Object.class);
    Map<String, Object> props = mapper.convertValue(obj, javaType);
    return props;
  }

  public static ObjectMapper getMapper() {
    ObjectMapper mapper = getObjectMapper();
    return mapper;
  }

  public static String prettyJSON(Object object) {
    String json = null;
    ObjectMapper mapper = getObjectMapper();
    try {
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return json;
  }

  public static void main(String[] args) {
    String json = "[{\"name\":\"aaa\"},{\"name\":\"bbb\"}]";
    Object object = toObject(json, Object.class);
    System.out.println(object);

    Map<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("name", "bing");
    parameter.put("age", 30);
    parameter.put("bod", new Date());

    json = toJSON(parameter, "name");
    System.out.println(json);
  }

}


@JsonFilter(JSONUtil.IGNORE_PROPERTEIS_FILTER)
class PropertyFilterMixIn {
}
