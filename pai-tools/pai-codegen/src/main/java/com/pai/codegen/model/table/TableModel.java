package com.pai.codegen.model.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableModel
{
  private String tableName;
  private String tabComment;
  private String foreignKey = "";

  private Map<String, String> variables = new HashMap();

  private List<ColumnModel> columnList = new ArrayList();

  private List<TableModel> subTableList = new ArrayList();
  private boolean sub;

  public String getTableName()
  {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTabComment()
  {
    return this.tabComment.replaceAll("\r\n", "");
  }

  public void setTabComment(String tabComment) {
    this.tabComment = tabComment;
  }

  public boolean isSub()
  {
    return this.sub;
  }

  public void setSub(boolean sub) {
    this.sub = sub;
  }

  public boolean getSub() {
    return this.sub;
  }

  public List<ColumnModel> getColumnList()
  {
    return this.columnList;
  }

  public List<ColumnModel> getPkList()
  {
    List list = new ArrayList();
    for (ColumnModel col : this.columnList) {
      if (col.getIsPK())
        list.add(col);
    }
    return list;
  }

  public ColumnModel getPkModel()
  {
    for (ColumnModel col : this.columnList) {
      if (col.getIsPK()) {
        return col;
      }
    }
    return null;
  }

  public List<ColumnModel> getCommonList()
  {
    List list = new ArrayList();
    for (ColumnModel col : this.columnList) {
      if (!col.getIsPK())
        list.add(col);
    }
    return list;
  }

  public void setColumnList(List<ColumnModel> columnList)
  {
    this.columnList = columnList;
  }

  public List<TableModel> getSubTableList()
  {
    return this.subTableList;
  }

  public void setSubTableList(List<TableModel> subTableList) {
    this.subTableList = subTableList;
  }

  public String getForeignKey()
  {
    return this.foreignKey;
  }

  public void setForeignKey(String foreignKey) {
    this.foreignKey = foreignKey;
  }

  public Map<String, String> getVariables()
  {
    return this.variables;
  }

  public void setVariables(Map<String, String> variables)
  {
    this.variables = variables;
  }

  public String toString()
  {
    return "TableModel [tableName=" + this.tableName + ", tabComment=" + this.tabComment + ", foreignKey=" + this.foreignKey + ", variables=" + this.variables + ", columnList=" + this.columnList + ", subTableList=" + this.subTableList + ", sub=" + this.sub + "]";
  }
}