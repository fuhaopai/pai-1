package com.pai.codegen.helper.elutil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Element;

public class VariablesUtil
{
  private static final String ElName = "variable";

  public static Map<String, String> getVariables(Element variablesEl)
  {
    Map variables = new HashMap();
    for (Iterator j = variablesEl.elementIterator("variable"); j.hasNext(); ) {
      Element variableEl = (Element)j.next();
      String name = variableEl.attributeValue("name");
      String value = variableEl.attributeValue("value");
      variables.put(name, value);
    }
    return variables;
  }
}