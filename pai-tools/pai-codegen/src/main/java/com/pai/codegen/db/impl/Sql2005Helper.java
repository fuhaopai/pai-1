package com.pai.codegen.db.impl;

import com.pai.codegen.db.DaoHelper;
import com.pai.codegen.db.IDbHelper;
import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.model.table.TableModel;
import com.pai.codegen.util.StringUtils;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Sql2005Helper
  implements IDbHelper
{
  private String url = "";
  private String username = "";
  private String password = "";

  private String sqlPk = "sp_pkeys N'%s'";

  private String sqlTableComment = "select cast(b.value as varchar) comment from sys.tables a, sys.extended_properties b where a.type='U' and a.object_id=b.major_id and b.minor_id=0 and a.name='%s'";

  private String sqlColumn = "select a.name, c.name typename, a.max_length length, a.is_nullable,a.precision,a.scale,(select count(*) from sys.identity_columns where sys.identity_columns.object_id = a.object_id and a.column_id = sys.identity_columns.column_id) as autoGen,(select cast(value as varchar) from sys.extended_properties where sys.extended_properties.major_id = a.object_id and sys.extended_properties.minor_id = a.column_id) as description from sys.columns a, sys.tables b, sys.types c where a.object_id = b.object_id and a.system_type_id=c.system_type_id and b.name='%s' and c.name<>'sysname' order by a.column_id";

  private String sqlAllTables = "select name from sys.tables where type='U' and name<>'sysdiagrams'";

  public Sql2005Helper() throws GeneralException
  {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    } catch (ClassNotFoundException e) {
      throw new GeneralException(getClass(), "找不到sqlserver驱动!", e);
    }
  }

  public void setUrl(String url, String username, String password)
  {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public TableModel build(String tableName)
    throws GeneralException
  {
    String comment = getComment(tableName);
    String pk = getPk(tableName);
    TableModel tableModel = new TableModel();
    tableModel.setTableName(tableName);
    tableModel.setTabComment(comment);

    List colList = getColumnsByTable(tableName);

    if (StringUtils.isNotEmpty(pk)) {
      setPk(colList, pk);
    }
    tableModel.setColumnList(colList);
    return tableModel;
  }

  private void setPk(List<ColumnModel> list, String pk)
  {
    for (ColumnModel model : list)
      if (pk.toLowerCase().equals(model.getColumnName().toLowerCase()))
        model.setIsPK(true);
  }

  private List<ColumnModel> getColumnsByTable(String tableName)
    throws GeneralException
  {
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlColumn, new Object[] { tableName });
    List list = dao.queryForList(sql, new Sql2005MapCmd());
    return list;
  }

  public List<String> getAllTable()
    throws GeneralException
  {
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    List list = dao.queryForList(this.sqlAllTables, new ResultSetMapper()
    {
      public String getObjecFromRs(ResultSet rs) throws SQLException {
        return rs.getString("name");
      }
    });
    return list;
  }

  private String getComment(String tableName)
    throws GeneralException
  {
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlTableComment, new Object[] { tableName });
    String comment = (String)dao.queryForObject(sql, new ResultSetMapper() {
      public String getObjecFromRs(ResultSet rs) throws SQLException {
        return rs.getString("comment");
      }
    });
    comment = comment == null ? tableName : comment;

    String[] aryComment = comment.split("\n");
    return aryComment[0];
  }

  private String getPk(String tableName)
    throws GeneralException
  {
    DaoHelper dao = new DaoHelper(this.url, this.username, this.password);
    String sql = String.format(this.sqlPk, new Object[] { tableName });
    System.out.println(sql);
    String columnName = (String)dao.queryForObject(sql, new ResultSetMapper()
    {
      public String getObjecFromRs(ResultSet rs)
        throws SQLException
      {
        return rs.getString("column_name");
      }
    });
    return columnName;
  }

  public static void main(String[] args)
    throws GeneralException
  {
    Sql2005Helper helper = new Sql2005Helper();
    helper.setUrl("jdbc:sqlserver://192.168.1.111:1433; DatabaseName=gzrs", "sa", "sasa");
    List list = helper.getColumnsByTable("Doc_ArchivesResource");
  }
}