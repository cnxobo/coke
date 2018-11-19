package org.xobo.coke.utility;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNodeUtil {
  public static String getString(JsonNode jsonNode, String path) {
    JsonNode node = jsonNode.get(path);
    return node != null ? node.textValue() : null;
  }
}
