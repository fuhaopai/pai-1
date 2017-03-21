package com.pai.codegen.model.table;

public class ColumnModel
{
  private String columnName = "";

  private String comment = "";

  private String colType = "";

  private String colDbType = "";

  private boolean isPK = false;

  private long length = 0L;

  private int precision = 0;

  private int scale = 0;

  private int autoGen = 0;

  private boolean isNotNull = false;

  private String displayDbType = "";

  public String getColumnName()
  {
    return this.columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  public String getComment()
  {
    return this.comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getColType()
  {
    return this.colType;
  }
  public void setColType(String colType) {
    this.colType = colType;
  }

  public String getColDbType()
  {
    return this.colDbType;
  }
  public void setColDbType(String colDbType) {
    this.colDbType = colDbType;
  }

  public boolean getIsPK()
  {
    return this.isPK;
  }
  public void setIsPK(boolean isPK) {
    this.isPK = isPK;
  }

  public long getLength()
  {
    return this.length;
  }
  public void setLength(long length) {
    this.length = length;
  }

  public int getPrecision()
  {
    return this.precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public int getScale()
  {
    return this.scale;
  }
  public void setScale(int scale) {
    this.scale = scale;
  }

  public int getAutoGen()
  {
    return this.autoGen;
  }
  public void setAutoGen(int autoGen) {
    this.autoGen = autoGen;
  }

  public boolean getIsNotNull()
  {
    return this.isNotNull;
  }
  public void setIsNotNull(boolean isNotNull) {
    this.isNotNull = isNotNull;
  }

  public String getDisplayDbType()
  {
    return this.displayDbType;
  }
  public void setDisplayDbType(String displayDbType) {
    this.displayDbType = displayDbType;
  }

  public String getDisplay()
  {
    return "";
  }

  public String toString()
  {
    return "ColumnModel [columnName=" + this.columnName + ", comment=" + this.comment + ", colType=" + this.colType + ", colDbType=" + this.colDbType + ", isPK=" + this.isPK + ", length=" + this.length + ", precision=" + this.precision + ", scale=" + this.scale + ", autoGen=" + this.autoGen + ", isNotNull=" + this.isNotNull + ", displayDbType=" + this.displayDbType + "]";
  }
}