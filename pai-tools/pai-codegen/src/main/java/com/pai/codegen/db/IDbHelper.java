package com.pai.codegen.db;

import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.TableModel;

public abstract interface IDbHelper
{
  public abstract void setUrl(String paramString1, String paramString2, String paramString3);

  public abstract TableModel build(String paramString)
    throws GeneralException;
}