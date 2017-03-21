package com.pai.codegen.db.impl;

import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlMapCmd
  implements ResultSetMapper<ColumnModel>
{
  public ColumnModel getObjecFromRs(ResultSet rs)
    throws SQLException
  {
    ColumnModel model = new ColumnModel();

    String colName = rs.getString("column_name");
    model.setColumnName(colName);

    String column_key = rs.getString("column_key");

    long character_length = rs.getLong("character_octet_length");
    model.setLength(character_length);

    String nullable = rs.getString("is_nullable");
    boolean is_nullable = nullable.equals("YES");
    model.setIsNotNull(!is_nullable);

    int precision = rs.getInt("numeric_precision");
    model.setPrecision(precision);

    int scale = rs.getInt("NUMERIC_scale");
    model.setScale(scale);

    boolean isPk = column_key.equals("PRI");
    model.setIsPK(isPk);

    String data_type = rs.getString("data_type");
    model.setColDbType(data_type);

    String javaType = getJavaType(data_type, precision, scale);
    model.setColType(javaType);

    String displayDbType = getDisplayDbType(data_type, character_length, precision, scale);
    model.setDisplayDbType(displayDbType);

    String comment = rs.getString("column_comment");
    comment = StringUtils.isEmpty(comment) ? colName : comment;
    String[] aryComment = comment.split("\n");
    model.setComment(aryComment[0]);

    return model;
  }

  private String getDisplayDbType(String dbtype, long character_length, int precision, int scale)
  {
    if (dbtype.equals("varchar"))
      return "varchar(" + character_length + ")";
    if (dbtype.equals("decimal")) {
      return "decimal(" + precision + "," + scale + ")";
    }
    return dbtype;
  }

  private String getJavaType(String dbtype, int precision, int scale)
  {
    if (dbtype.equals("bigint"))
      return "Long";
    if (dbtype.equals("int"))
      return "Integer";
    if ((dbtype.equals("tinyint")) || (dbtype.equals("smallint")))
      return "Short";
    if ((dbtype.equals("varchar")) || (dbtype.endsWith("text"))) {
      return "String";
    }
    if ((dbtype.equals("varchar")) || (dbtype.endsWith("text"))) {
      return "String";
    }
    if (dbtype.equals("double")) {
      return "java.math.BigDecimal";
    }
    if (dbtype.equals("float")) {
      return "java.math.BigDecimal";
    }
    if (dbtype.endsWith("blob")) {
      return "byte[]";
    }
    if (dbtype.equals("decimal"))
    {
      if (scale == 0)
      {
        if (precision <= 10) {
          return "Integer";
        }
        return "Long";
      }

      return "java.math.BigDecimal";
    }

    if (dbtype.startsWith("date") || dbtype.equals("timestamp"))
    {
      return "java.util.Date";
    }

    return dbtype;
  }
}