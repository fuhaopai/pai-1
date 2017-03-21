package com.pai.codegen.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract interface ResultSetMapper<T>
{
  public abstract T getObjecFromRs(ResultSet paramResultSet)
    throws SQLException;
}