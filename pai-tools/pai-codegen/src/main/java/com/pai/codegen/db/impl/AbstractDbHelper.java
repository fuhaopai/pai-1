package com.pai.codegen.db.impl;

import com.pai.codegen.db.IDbHelper;

public abstract class AbstractDbHelper
  implements IDbHelper
{
  protected String url = "";
  protected String username = "";
  protected String password = "";

  public void setUrl(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }
}