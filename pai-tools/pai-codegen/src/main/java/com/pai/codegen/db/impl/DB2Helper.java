package com.pai.codegen.db.impl;

import com.pai.codegen.db.DaoHelper;
import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.model.table.TableModel;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DB2Helper extends AbstractDbHelper
{
  private String sqlPk = "SELECT TABNAME TAB_NAME, COLNAME COL_NAME, KEYSEQ  FROM  SYSCAT.COLUMNS WHERE  TABSCHEMA='BPMX380' AND KEYSEQ>0 AND UPPER(TABNAME) = UPPER('%s')";

  private String sqlTableComment = "SELECT  TABNAME, REMARKS FROM  SYSCAT.TABLES WHERE TABSCHEMA IN (SELECT CURRENT SCHEMA FROM SYSIBM.DUAL) AND UPPER(TABNAME) = UPPER('%s') ";

  private String sqlColumn = "SELECT TABNAME, COLNAME, TYPENAME, REMARKS, NULLS, LENGTH, SCALE, KEYSEQ  FROM  SYSCAT.COLUMNS WHERE  TABSCHEMA IN (SELECT CURRENT SQLID FROM SYSIBM.DUAL) AND UPPER(TABNAME) = UPPER('%s') ";

  public DB2Helper()
    throws GeneralException
  {
    try
    {
      Class.forName("com.ibm.db2.jcc.DB2Driver");
    } catch (ClassNotFoundException e) {
      throw new GeneralException(getClass(), "找不到DB2驱动!", e);
    }
  }

  private List<ColumnModel> getColumnsByTable(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlColumn, new Object[] { tableName });
    List list = dao.queryForList(sql, new DB2MapCmd());
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

  public static void main(String[] args)
    throws GeneralException
  {
    DB2Helper helper = new DB2Helper();
    helper.setUrl("jdbc:db2://192.168.1.17:50000/bpmx:currentSchema=BPMX380;", "db2admin", "123456");
    System.out.println(helper.build("TEST1"));
  }
}