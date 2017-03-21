package com.pai.codegen.db.impl;

import com.pai.codegen.db.DaoHelper;
import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.model.table.TableModel;
import com.pai.codegen.util.StringUtils;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlHelper extends AbstractDbHelper
{
  String sqlColumns = "select * from information_schema.columns where table_schema=DATABASE() and table_name='%s' ";

  String sqlComment = "select table_name,table_comment  from information_schema.tables t where t.table_schema=DATABASE() and table_name='%s' ";

  String sqlAllTable = "select table_name,table_comment from information_schema.tables t where t.table_schema=DATABASE()";

  public MySqlHelper() throws GeneralException
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new GeneralException(getClass(), "找不到mysql驱动!", e);
    }
  }

  public TableModel build(String tableName)
    throws GeneralException
  {
    TableModel tableModel = getTableModel(tableName);
    List colList = getColumnsByTable(tableName);
    tableModel.setColumnList(colList);
    return tableModel;
  }

  private List<ColumnModel> getColumnsByTable(String tableName)
    throws GeneralException
  {
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlColumns, new Object[] { tableName });
    List list = dao.queryForList(sql, new MySqlMapCmd());
    return list;
  }

  private TableModel getTableModel(String tableName)
    throws GeneralException
  {
    TableModel tablemodel = new TableModel();
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlComment, new Object[] { tableName });
    String comment = (String)dao.queryForObject(sql, new ResultSetMapper()
    {
      public String getObjecFromRs(ResultSet rs) throws SQLException {
        String comment = rs.getString("table_comment");
        return comment;
      }
    });
    tablemodel.setTableName(tableName);

    if (StringUtils.isEmpty(comment))
      comment = tableName;
    if (comment.startsWith("InnoDB free")) {
      comment = tableName;
      tablemodel.setTabComment(comment);
    }
    else {
      if (comment.indexOf(";") != -1)
      {
        int pos = comment.indexOf(";");
        comment = comment.substring(0, pos);
      }
      String[] aryComment = comment.split("\n");
      tablemodel.setTabComment(aryComment[0]);
    }

    return tablemodel;
  }

  public static void main(String[] args)
    throws GeneralException
  {
    MySqlHelper helper = new MySqlHelper();
    helper.setUrl("jdbc:mysql://192.168.1.8:3306/bpm?useUnicode=true&amp;characterEncoding=utf-8", "root", "root");
    TableModel table = helper.build("act_ru_task");
    System.out.println("ok");
  }
}