package com.pai.codegen.model.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

/**
 * <table></table>标签信息解析工具类
 * @author Administrator
 *
 */
public class GenTable
{
  private String tableName;
  private Map<String, String> variable = new HashMap();

  private List<GenSubTable> subtable = new ArrayList();

  public GenTable(Element tableEl) {
    this.tableName = tableEl.attributeValue("tableName");
    getVariable().put("tabname", this.tableName);
    for (Iterator j = tableEl.elementIterator("variable"); j.hasNext(); ) {
      Element fileEl = (Element)j.next();
      String name = fileEl.attributeValue("name");
      String value = fileEl.attributeValue("value");
      getVariable().put(name, value);
    }

    if (getVariable().containsKey("class")) {
      String val = (String)getVariable().get("class");
      val = val.substring(0, 1).toLowerCase() + val.substring(1);
      getVariable().put("classVar", val);
    }

    for (Iterator m = tableEl.elementIterator("subtable"); m.hasNext(); ) {
      Element subEl = (Element)m.next();
      String tablename = subEl.attributeValue("tablename");
      String foreignKey = subEl.attributeValue("foreignKey");
      Map vars = new HashMap();

      for (Iterator var = subEl.elementIterator("variable"); var.hasNext(); ) {
        Element varEl = (Element)var.next();
        String name = varEl.attributeValue("name");
        String value = varEl.attributeValue("value");
        vars.put(name, value);
      }
      //增加子表的功能 2015-11-04 15:13 HYY
      if (vars.containsKey("class")) {
          String val = (String)vars.get("class");
          val = val.substring(0, 1).toLowerCase() + val.substring(1);
          vars.put("classVar", val);
        }
      
      addSubTable(tablename, foreignKey, vars);
    }
  }

  public String getTableName()
  {
    return this.tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public void addSubTable(String tableName, String foreignKey, Map<String, String> vars) {
    GenSubTable sb = new GenSubTable(tableName, foreignKey, vars);
    this.subtable.add(sb);
  }

  public Map<String, String> getVariable()
  {
    return this.variable;
  }
  public void setVariable(Map<String, String> variable) {
    this.variable = variable;
  }

  public List<GenSubTable> getSubtable()
  {
    return this.subtable;
  }

  public void setSubtable(List<GenSubTable> subtable) {
    this.subtable = subtable;
  }
}