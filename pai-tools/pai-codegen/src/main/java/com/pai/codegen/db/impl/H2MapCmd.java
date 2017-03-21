package com.pai.codegen.db.impl;

import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2MapCmd
  implements ResultSetMapper<ColumnModel>
{
  public ColumnModel getObjecFromRs(ResultSet rs)
    throws SQLException
  {
    ColumnModel model = new ColumnModel();

    String name = rs.getString("COLUMN_NAME");
    String is_nullable = rs.getString("IS_NULLABLE");
    String data_type = rs.getString("TYPE_NAME");
    String length = rs.getString("LENGTH");
    String precisions = rs.getString("PRECISIONS");
    String scale = rs.getString("SCALE");
    String column_list = rs.getString("COLUMN_LIST");
    String column_comment = rs.getString("REMARKS");

    int iLength = string2Int(length, 0);
    int iPrecisions = string2Int(precisions, 0);
    int iScale = string2Int(scale, 0);

    String displayDbType = getDisplayDbType(data_type, iLength, iPrecisions, iScale);
    String javaType = getJavaType(data_type, iPrecisions, iScale);

    boolean isNotNull = "NO".equalsIgnoreCase(is_nullable);

    model.setColumnName(name);
    model.setColDbType(data_type);
    model.setComment(column_comment);
    model.setIsNotNull(isNotNull);
    model.setLength(iLength);
    model.setPrecision(iPrecisions);
    model.setScale(iScale);
    model.setDisplayDbType(displayDbType);
    model.setColType(javaType);
    boolean isPkColumn = false;
    if (StringUtils.isNotEmpty(column_list)) {
      String[] pkColumns = column_list.split(",");
      for (String pkColumn : pkColumns) {
        if (name.trim().equalsIgnoreCase(pkColumn.trim())) {
          isPkColumn = true;
          break;
        }
      }
    }
    model.setIsPK(isPkColumn);
    return model;
  }

  private String getDisplayDbType(String dbtype, long length, int precision, int scale)
  {
    String type = dbtype.toUpperCase();
    if (dbtype.indexOf("CHAR") > 0)
      return type + "(" + length + ")";
    if (("DECIMAL".equalsIgnoreCase(type)) || ("NUMBER".equalsIgnoreCase(type)) || ("DEC".equalsIgnoreCase(type)) || ("NUMERIC".equalsIgnoreCase(type)))
    {
      return type + "(" + (precision - scale) + "," + scale + ")";
    }
    return type;
  }

  private String getJavaType(String dbtype, int precision, int scale)
  {
    dbtype = dbtype.toUpperCase();
    if (("BLOB".equals(dbtype)) || ("TINYBLOB".equals(dbtype)) || ("MEDIUMBLOB".equals(dbtype)) || ("LONGBLOB".equals(dbtype)) || ("IMAGE".equals(dbtype)) || ("OID".equals(dbtype)) || ("BINARY".equals(dbtype)) || ("VARBINARY".equals(dbtype)) || ("LONGVARBINARY".equals(dbtype)) || ("RAW".equals(dbtype)) || ("BYTEA".equals(dbtype)))
    {
      return "byte[]";
    }if (("TIMESTAMP".equals(dbtype)) || ("TIME".equals(dbtype)) || ("DATE".equals(dbtype)) || ("DATETIME".equals(dbtype)) || ("SMALLDATETIME".equals(dbtype)))
    {
      return "java.util.Date";
    }if (("BIGINT".equalsIgnoreCase(dbtype)) || ("INT8".equalsIgnoreCase(dbtype)) || ("IDENTITY".equalsIgnoreCase(dbtype)))
    {
      return "Long";
    }if (("INTEGER".equalsIgnoreCase(dbtype)) || ("INT".equalsIgnoreCase(dbtype)) || ("MEDIUMINT".equalsIgnoreCase(dbtype)) || ("INT4".equalsIgnoreCase(dbtype)) || ("SIGNED".equalsIgnoreCase(dbtype)) || ("SMALLINT".equalsIgnoreCase(dbtype)) || ("YEAR".equalsIgnoreCase(dbtype)) || ("INT2".equalsIgnoreCase(dbtype)) || ("TINYINT".equalsIgnoreCase(dbtype)))
    {
      return "Integer";
    }if (("DOUBLE".equalsIgnoreCase(dbtype)) || ("FLOAT".equalsIgnoreCase(dbtype)) || ("FLOAT4".equalsIgnoreCase(dbtype)) || ("FLOAT8".equalsIgnoreCase(dbtype)) || ("DECIMAL".equalsIgnoreCase(dbtype)) || ("NUMBER".equalsIgnoreCase(dbtype)) || ("DEC".equalsIgnoreCase(dbtype)) || ("NUMERIC".equalsIgnoreCase(dbtype)) || ("REAL".equalsIgnoreCase(dbtype)))
    {
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