package org.xobo.coke.querysupporter.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryResolver {
  private Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
  private StringBuffer assemblySql = new StringBuffer();
  private StringBuilder select = new StringBuilder();
  private StringBuilder from = new StringBuilder();
  private StringBuilder where = new StringBuilder();
  private StringBuilder order = new StringBuilder();

  public Map<String, Object> getValueMap() {
    return valueMap;
  }

  public void setValueMap(Map<String, Object> valueMap) {
    this.valueMap = valueMap;
  }

  public StringBuffer getAssemblySql() {
    return assemblySql;
  }

  public void setAssemblySql(StringBuffer assemblySql) {
    this.assemblySql = assemblySql;
  }

  public StringBuilder getSelect() {
    return select;
  }

  public void setSelect(StringBuilder select) {
    this.select = select;
  }

  public StringBuilder getFrom() {
    return from;
  }

  public void setFrom(StringBuilder from) {
    this.from = from;
  }

  public StringBuilder getWhere() {
    return where;
  }

  public void setWhere(StringBuilder where) {
    this.where = where;
  }

  public StringBuilder getOrder() {
    return order;
  }

  public void setOrder(StringBuilder order) {
    this.order = order;
  }

  public String getOrderPhrase() {
    if (order.length() > 0) {
      return " order by " + order;
    } else {
      return "";
    }
  }
}
