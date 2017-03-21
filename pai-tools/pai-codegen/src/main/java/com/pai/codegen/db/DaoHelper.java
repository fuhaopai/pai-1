package com.pai.codegen.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pai.codegen.exception.GeneralException;
/**
 * 数据库操作工具类
 * @author Administrator
 *
 * @param <T>
 */
public class DaoHelper<T>
{
  private String url = "";
  private String userName = "";
  private String pwd = "";

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public DaoHelper(String url, String username, String password)
  {
    this.url = url;
    this.userName = username;
    this.pwd = password;
  }

  public T queryForObject(String sql, ResultSetMapper<T> cmd)
    throws GeneralException
  {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try
    {
      conn = DriverManager.getConnection(this.url, this.userName, this.pwd);
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return cmd.getObjecFromRs(rs);
      }
      System.out.println("没有找到数据:" + sql);
      return null;
    } catch (SQLException e) {
      throw new GeneralException(getClass(), e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null)
          conn.close();
      }
      catch (SQLException e) {
        throw new GeneralException(getClass(), e);
      }
    }
  }

  public List<T> queryForList(String sql, ResultSetMapper<T> cmd)
    throws GeneralException
  {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    List list = new ArrayList();
    try {
      conn = DriverManager.getConnection(this.url, this.userName, this.pwd);
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next())
        list.add(cmd.getObjecFromRs(rs));
    }
    catch (SQLException e) {
      throw new GeneralException(getClass(), e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null)
          conn.close();
      }
      catch (SQLException e) {
        throw new GeneralException(getClass(), e);
      }
    }
    return list;
  }
}