package com.pai.codegen.db.impl;

import com.pai.codegen.db.DaoHelper;
import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.model.table.TableModel;
import com.pai.codegen.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OracleHelper extends AbstractDbHelper
{
  private String sqlPk = "select column_name from user_constraints c,user_cons_columns col where c.constraint_name=col.constraint_name and c.constraint_type='P' and c.table_name='%s'";

  private String sqlTableComment = "select * from user_tab_comments  where table_type='TABLE' AND table_name ='%s'";

  private String sqlColumn = "select    A.column_name NAME,A.data_type TYPENAME,A.data_length LENGTH,A.data_precision PRECISION,    A.Data_Scale SCALE,A.Data_default, A.NULLABLE, B.comments DESCRIPTION  from  user_tab_columns A,user_col_comments B where a.COLUMN_NAME=b.column_name and    A.Table_Name = B.Table_Name and A.Table_Name='%s' order by A.column_id";

  private String sqlAllTables = "select table_name from user_tables where status='VALID'";

  public OracleHelper() throws GeneralException {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
    } catch (ClassNotFoundException e) {
      throw new GeneralException(getClass(), "找不到oracle驱动!", e);
    }
  }

  private List<ColumnModel> getColumnsByTable(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlColumn, new Object[] { tableName });
    List list = dao.queryForList(sql, new OracleMapCmd());
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
        return rs.getString("COMMENTS");
      }
    });
    if (comment == null) comment = tableName;
    String[] aryComment = comment.split("\n");
    return aryComment[0];
  }

  private String getPk(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();
    String sql = String.format(this.sqlPk, new Object[] { tableName });
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String pk = "";
    try {
      pk = (String)dao.queryForObject(sql, new ResultSetMapper() {
        public String getObjecFromRs(ResultSet rs) throws SQLException {
          return rs.getString("COLUMN_NAME");
        }
      });
    }
    catch (Exception e) {
      throw new GeneralException(getClass(), "从表中取得主键出错,请检查表是否设置主键");
    }
    return pk;
  }

  private void setPk(List<ColumnModel> list, String pk)
  {
    for (ColumnModel model : list)
      if (pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
        model.setIsPK(true);
  }

  public TableModel build(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();

    String tabComment = getTableComment(tableName);
    String pk = getPk(tableName);

    TableModel tableModel = new TableModel();
    tableModel.setTableName(tableName);
    tableModel.setTabComment(tabComment);

    List list = getColumnsByTable(tableName);

    if (StringUtils.isNotEmpty(pk)) {
      setPk(list, pk);
    }
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

  public static void main(String[] args)
    throws GeneralException
  {
    OracleHelper helper = new OracleHelper();
    helper.setUrl("jdbc:oracle:thin:@localhost:1521:zyp", "zyp", "zyp");
    String pk = helper.getPk("TEST");
  }
}