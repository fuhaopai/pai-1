package com.pai.codegen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter
{
  private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
  private static final SimpleDateFormat dateTimeLineFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
  private static final String dateTimeLinePattern = "yyyy-MM-dd-HH-mm-ss";
  private static final String shortDatePattern = "yyMMdd";
  private static String timePattern = "HH:mm";

  public static final String now()
  {
    return dateTimeFormatter.format(new Date());
  }

  public static final String nowLine() {
    return dateTimeLineFormatter.format(new Date());
  }

  public static final String toString(long time) {
    return dateTimeFormatter.format(new Date(time));
  }

  public static final String toString(Date aDate)
  {
    return dateTimeFormatter.format(aDate);
  }
  public static final String toString(Date aDate, String pattern) {
    if ((pattern == null) || (aDate == null)) {
      return "";
    }
    SimpleDateFormat df = null;
    String returnValue = "";
    df = new SimpleDateFormat(pattern);
    returnValue = df.format(aDate);

    return returnValue;
  }

  public static Date toDate(String strDate) throws ParseException {
    Date date = null;
    try
    {
      date = dateTimeFormatter.parse(strDate);
    } catch (ParseException pe) {
      pe.printStackTrace();
      throw new ParseException(pe.getMessage(), pe.getErrorOffset());
    }

    return date;
  }

  public static final Date toDate(String strDate, String pattern) throws ParseException {
    SimpleDateFormat df = null;
    Date date = null;
    df = new SimpleDateFormat(pattern);
    try
    {
      date = df.parse(strDate);
    } catch (ParseException pe) {
      pe.printStackTrace();
      throw new ParseException(pe.getMessage(), pe.getErrorOffset());
    }

    return date;
  }
}