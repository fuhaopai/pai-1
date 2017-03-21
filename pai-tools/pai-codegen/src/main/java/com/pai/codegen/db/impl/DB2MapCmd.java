package com.pai.codegen.db.impl;

import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2MapCmd
  implements ResultSetMapper<ColumnModel>
{
  public ColumnModel getObjecFromRs(ResultSet rs)
    throws SQLException
  {
    ColumnModel model = new ColumnModel();
    String name = rs.getString("COLNAME");
    String typename = rs.getString("TYPENAME");
    int length = Integer.parseInt(rs.getString("LENGTH"));
    int precision = length;
    int scale = rs.getInt("SCALE");
    String description = rs.getString("REMARKS");
    int primaryKey = string2Int(rs.getString("KEYSEQ"), 0);
    description = description == null ? name : description;
    String NULLABLE = rs.getString("NULLS");

    String displayDbType = getDisplayDbType(typename, length, precision, scale);
    String javaType = getJavaType(typename, precision, scale);

    boolean isNotNull = "N".equalsIgnoreCase(NULLABLE);

    model.setColumnName(name);
    model.setColDbType(typename);
    model.setComment(description);
    model.setIsNotNull(isNotNull);
    model.setLength(length);
    model.setPrecision(length);
    model.setScale(scale);
    model.setDisplayDbType(displayDbType);
    model.setColType(javaType);
    model.setIsPK(primaryKey > 0);
    return model;
  }

  private String getDisplayDbType(String dbtype, long length, int precision, int scale)
  {
    if (("CHAR".equalsIgnoreCase(dbtype)) || ("VARCHAR".equalsIgnoreCase(dbtype)) || ("LONG VARCHAR".equalsIgnoreCase(dbtype)))
    {
      return dbtype + "(" + length + ")";
    }if ("DECIMAL".equalsIgnoreCase(dbtype))
    {
      return "DECIMAL(" + (precision - scale) + "," + scale + ")";
    }if (("BIGINT".equalsIgnoreCase(dbtype)) || ("DOUBLE".equalsIgnoreCase(dbtype)) || ("INTEGER".equalsIgnoreCase(dbtype)) || ("REAL".equalsIgnoreCase(dbtype)) || ("SMALLINT".equalsIgnoreCase(dbtype)))
    {
      return dbtype;
    }
    return dbtype;
  }

  private String getJavaType(String dbtype, int precision, int scale)
  {
    dbtype = dbtype.toUpperCase();
    if (("BLOB".equals(dbtype)) || ("GRAPHIC".equals(dbtype)) || ("LONG VARGRAPHIC".equals(dbtype)) || ("VARGRAPHIC".equals(dbtype)))
    {
      return "byte[]";
    }if (("CLOB".equals(dbtype)) || ("XML".equals(dbtype)) || ("DBCLOB".equals(dbtype)))
    {
      return "String";
    }if (("CHARACTER".equals(dbtype)) || ("LONG VARCHAR".equals(dbtype)) || ("VARCHAR".equals(dbtype)))
    {
      return "String";
    }if (("TIMESTAMP".equals(dbtype)) || ("TIME".equals(dbtype)) || ("DATE".equals(dbtype)))
    {
      return "java.util.Date";
    }if ("BIGINT".equalsIgnoreCase(dbtype))
      return "Long";
    if (("INTEGER".equalsIgnoreCase(dbtype)) || ("SMALLINT".equalsIgnoreCase(dbtype)))
    {
      return "Integer";
    }if (("DOUBLE".equalsIgnoreCase(dbtype)) || ("REAL".equalsIgnoreCase(dbtype)))
    {
      return "Double";
    }if (dbtype.indexOf("DECIMAL") > 0) {
      return "Double";
    }
    return "String";
  }

  private int string2Int(String str, int def)
  {
    int retval = def;
    if (StringUtils.isNotEmpty(str)) {
      try {
        retval = Integer.parseInt(str);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return retval;
  }
}