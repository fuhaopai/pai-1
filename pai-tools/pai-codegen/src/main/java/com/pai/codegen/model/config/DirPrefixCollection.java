package com.pai.codegen.model.config;

import java.util.HashMap;
import java.util.Map;

public class DirPrefixCollection
{
  private Map<String, String> dirPrefixs = new HashMap();

  public void put(String key, String value) {
    this.dirPrefixs.put(key, value);
  }

  public String get(String key) {
    return (String)this.dirPrefixs.get(key);
  }
}