package com.pai.codegen.db.impl;

import com.pai.codegen.db.DaoHelper;
import com.pai.codegen.db.IDbHelper;
import com.pai.codegen.db.ResultSetMapper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.table.ColumnModel;
import com.pai.codegen.model.table.TableModel;
import com.pai.codegen.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DMHelper
  implements IDbHelper
{
  private String SQL_GET_PK = "SELECT  CONS_C.COLUMN_NAME FROM \"SYS\".\"USER_CONSTRAINTS\" CONS, \"SYS\".\"USER_CONS_COLUMNS\" CONS_C    WHERE  CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME  AND CONS.CONSTRAINT_TYPE='P'  AND CONS_C.POSITION=1  AND   CONS.TABLE_NAME='%s'";

  private String SQL_GET_TABLE_COMMENT = "SELECT TABLE_NAME,COMMENTS FROM (SELECT A.TABLE_NAME AS TABLE_NAME,DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS COMMENTS FROM \"SYS\".\"USER_TABLES\" A LEFT JOIN \"SYS\".\"SYSTABLECOMMENTS\" B ON  A.TABLE_NAME=B.TVNAME) WHERE  TABLE_NAME ='%s'";

  private final String SQL_GET_COLUMNS = "SELECT T.TABLE_NAME TABLE_NAME, T.NAME NAME,T.TYPENAME TYPENAME, T.LENGTH LENGTH,  T.PRECISION PRECISION,T.SCALE SCALE,T.DATA_DEFAULT DATA_DEFAULT,T.NULLABLE NULLABLE,T.DESCRIPTION DESCRIPTION,  (SELECT  COUNT(*)   FROM    \"SYS\".\"USER_CONSTRAINTS\" CONS, \"SYS\".\"USER_CONS_COLUMNS\" CONS_C    WHERE  CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME  AND CONS.CONSTRAINT_TYPE='P'  AND CONS_C.POSITION=1  AND   CONS.TABLE_NAME=T.TABLE_NAME  AND CONS_C.COLUMN_NAME= T.NAME) AS  IS_PK FROM (SELECT A.COLUMN_ID COLUMN_ID, A.TABLE_NAME TABLE_NAME, A.COLUMN_NAME NAME, A.DATA_TYPE TYPENAME, A.DATA_LENGTH LENGTH, A.DATA_PRECISION PRECISION, A.DATA_SCALE SCALE, A.DATA_DEFAULT, A.NULLABLE, DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS DESCRIPTION  FROM \"SYS\".\"USER_TAB_COLUMNS\" A LEFT JOIN \"SYS\".\"SYSCOLUMNCOMMENTS\" B ON  A.COLUMN_NAME=B.COLNAME AND  A.TABLE_NAME=B.TVNAME  AND B.SCHNAME=user() ) T  WHERE TABLE_NAME='%S'  ORDER BY COLUMN_ID ";

  private String SQL_GET_ALL_TABLES = "SELECT TABLE_NAME,COMMENTS FROM (SELECT A.TABLE_NAME AS TABLE_NAME,DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS COMMENTS FROM \"SYS\".\"USER_TABLES\" A LEFT JOIN \"SYS\".\"SYSTABLECOMMENTS\" B ON  A.TABLE_NAME=B.TVNAME) WHERE 1=1";
  private String url;
  private String username;
  private String password;

  public DMHelper()
    throws GeneralException
  {
    try
    {
      Class.forName("dm.jdbc.driver.DmDriver");
    } catch (ClassNotFoundException e) {
      throw new GeneralException(getClass(), "找不到 达梦 驱动!", e);
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
    String sql = String.format("SELECT T.TABLE_NAME TABLE_NAME, T.NAME NAME,T.TYPENAME TYPENAME, T.LENGTH LENGTH,  T.PRECISION PRECISION,T.SCALE SCALE,T.DATA_DEFAULT DATA_DEFAULT,T.NULLABLE NULLABLE,T.DESCRIPTION DESCRIPTION,  (SELECT  COUNT(*)   FROM    \"SYS\".\"USER_CONSTRAINTS\" CONS, \"SYS\".\"USER_CONS_COLUMNS\" CONS_C    WHERE  CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME  AND CONS.CONSTRAINT_TYPE='P'  AND CONS_C.POSITION=1  AND   CONS.TABLE_NAME=T.TABLE_NAME  AND CONS_C.COLUMN_NAME= T.NAME) AS  IS_PK FROM (SELECT A.COLUMN_ID COLUMN_ID, A.TABLE_NAME TABLE_NAME, A.COLUMN_NAME NAME, A.DATA_TYPE TYPENAME, A.DATA_LENGTH LENGTH, A.DATA_PRECISION PRECISION, A.DATA_SCALE SCALE, A.DATA_DEFAULT, A.NULLABLE, DECODE(B.COMMENT$,NULL, A.TABLE_NAME,B.COMMENT$) AS DESCRIPTION  FROM \"SYS\".\"USER_TAB_COLUMNS\" A LEFT JOIN \"SYS\".\"SYSCOLUMNCOMMENTS\" B ON  A.COLUMN_NAME=B.COLNAME AND  A.TABLE_NAME=B.TVNAME  AND B.SCHNAME=user() ) T  WHERE TABLE_NAME='%S'  ORDER BY COLUMN_ID ", new Object[] { tableName });
    List list = dao.queryForList(sql, new DMMapCmd());
    return list;
  }

  private String getTableComment(String tableName)
    throws GeneralException
  {
    tableName = tableName.toUpperCase();

    String sql = String.format(this.SQL_GET_TABLE_COMMENT, new Object[] { tableName });
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
    String sql = String.format(this.SQL_GET_PK, new Object[] { tableName });
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
    return dao.queryForList(this.SQL_GET_ALL_TABLES, new ResultSetMapper() {
      public String getObjecFromRs(ResultSet rs) throws SQLException {
        return rs.getString("TABLE_NAME");
      }
    });
  }

  public static void main(String[] args)
    throws GeneralException
  {
    DMHelper helper = new DMHelper();
    helper.setUrl("jdbc:oracle:thin:@localhost:1521:zyp", "zyp", "zyp");
    String pk = helper.getPk("TEST");
  }
}