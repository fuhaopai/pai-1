package com.pai.codegen.db.impl;

import com.pai.codegen.db.DaoHelper;
import com.pai.codegen.db.IDbHelper;
import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.model.table.TableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class H2Helper
  implements IDbHelper
{
  private String sqlTableComment = "SELECT TABLE_NAME, REMARKS FROM INFORMATION_SCHEMA.TABLES T WHERE T.TABLE_SCHEMA=SCHEMA() AND UPPER(TABLE_NAME) = UPPER('%s') ";

  private String sqlColumn = "SELECT A.TABLE_NAME, A.COLUMN_NAME, A.IS_NULLABLE, A.TYPE_NAME, A.CHARACTER_OCTET_LENGTH LENGTH, A.NUMERIC_PRECISION PRECISIONS, A.NUMERIC_SCALE SCALE, B.COLUMN_LIST, A.REMARKS FROM INFORMATION_SCHEMA.COLUMNS A  JOIN INFORMATION_SCHEMA.CONSTRAINTS B ON A.TABLE_NAME=B.TABLE_NAME WHERE  A.TABLE_SCHEMA=SCHEMA() AND B.CONSTRAINT_TYPE='PRIMARY KEY' AND UPPER(A.TABLE_NAME) = UPPER('%s') ";

  private String sqlAllTables = "SELECT TABLE_NAME, REMARKS FROM INFORMATION_SCHEMA.TABLES T WHERE T.TABLE_SCHEMA=SCHEMA() ";
  private String url;
  private String username;
  private String password;

  public H2Helper()
    throws GeneralException
  {
    try
    {
      Class.forName("org.h2.Driver");
    } catch (ClassNotFoundException e) {
      throw new GeneralException(getClass(), "找不到H2驱动!", e);
    }
  }

  public void setUrl(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  private List<ColumnModel> getColumnsByTable(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlColumn, new Object[] { tableName });
    List list = dao.queryForList(sql, new H2MapCmd());
    return list;
  }

  private String getTableComment(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();

    String sql = String.format(this.sqlTableComment, new Object[] { tableName });
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String comment = (String)dao.queryForObject(sql, new ResultSetMapper() {
      public String getObjecFromRs(ResultSet rs) throws SQLException {
        return rs.getString("REMARKS");
      }
    });
    return comment == null ? tableName : comment;
  }

  public TableModel build(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();

    String tabComment = getTableComment(tableName);

    TableModel tableModel = new TableModel();
    tableModel.setTableName(tableName);
    tableModel.setTabComment(tabComment);

    List list = getColumnsByTable(tableName);
    tableModel.setColumnList(list);

    return tableModel;
  }

  public List<String> getAllTable()
    throws GeneralException
  {
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    return dao.queryForList(this.sqlAllTables, new ResultSetMapper() {
      public String getObjecFromRs(ResultSet rs) throws SQLException {
        return rs.getString("TABLE_NAME");
      }
    });
  }
}