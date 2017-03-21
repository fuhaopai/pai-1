package com.pai.codegen.util;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils
{
  public static String upperFirst(String str)
  {
    String result = str.substring(0, 1).toUpperCase() + str.substring(1);
    return result;
  }

  public static String lowerFirst(String str) {
    String result = str.substring(0, 1).toLowerCase() + str.substring(1);
    return result;
  }

  public static String[] getBySplit(String content, String splitTag)
  {
    String[] aryRtn = new String[2];
    int pos = content.lastIndexOf(splitTag);
    aryRtn[0] = content.substring(0, pos);
    aryRtn[1] = content.substring(pos + splitTag.length());
    return aryRtn;
  }

  public static String replaceVariable(String template, Map<String, String> map)
  {
    Pattern regex = Pattern.compile("\\{(.*?)\\}");
    Matcher regexMatcher = regex.matcher(template);
    while (regexMatcher.find()) {
      String key = regexMatcher.group(1);
      String toReplace = regexMatcher.group(0);
      String value = (String)map.get(key);
      if (value != null) {
        template = template.replace(toReplace, value);
      }
    }

    return template;
  }

  public static String trimPrefix(String toTrim, String trimStr)
  {
    while (toTrim.startsWith(trimStr))
    {
      toTrim = toTrim.substring(trimStr.length());
    }
    return toTrim;
  }

  public static String trimSufffix(String toTrim, String trimStr)
  {
    while (toTrim.endsWith(trimStr))
    {
      toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
    }
    return toTrim;
  }

  public static String trim(String toTrim, String trimStr)
  {
    return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
  }

  public static String combilePath(String baseDir, String dir)
  {
    baseDir = trimSufffix(baseDir, File.separator);
    dir = trimPrefix(dir, File.separator);

    return baseDir + File.separator + dir;
  }

  public static String replace(String content, String startTag, String endTag, String repalceWith)
  {
    String tmp = content.toLowerCase();
    String tmpStartTag = startTag.toLowerCase();
    String tmpEndTag = endTag.toLowerCase();

    StringBuffer sb = new StringBuffer();
    int beginIndex = tmp.indexOf(tmpStartTag);
    int endIndex = tmp.indexOf(tmpEndTag);
    while ((beginIndex != -1) && (endIndex != -1) && (beginIndex < endIndex))
    {
      String pre = content.substring(0, tmp.indexOf(tmpStartTag) + tmpStartTag.length());
      String suffix = content.substring(tmp.indexOf(tmpEndTag));

      tmp = suffix.toLowerCase();
      content = suffix.substring(endTag.length());

      beginIndex = tmp.indexOf(tmpStartTag);
      endIndex = tmp.indexOf(tmpEndTag);
      String replaced = pre + "\r\n" + repalceWith + "\r\n" + endTag;
      sb.append(replaced);
    }
    sb.append(content);
    return sb.toString();
  }

  public static boolean isExist(String content, String begin, String end)
  {
    String tmp = content.toLowerCase();
    begin = begin.toLowerCase();
    end = end.toLowerCase();
    int beginIndex = tmp.indexOf(begin);
    int endIndex = tmp.indexOf(end);
    if ((beginIndex != -1) && (endIndex != -1) && (beginIndex < endIndex))
      return true;
    return false;
  }

  public static String subString(String content, String start, String end)
  {
    String str = content;
    if (content.indexOf(start) != -1) {
      if (content.indexOf(end) != -1)
        str = content.substring(content.indexOf(start) + start.length(), content.lastIndexOf(end));
      else {
        str = content.substring(content.indexOf(start) + start.length());
      }
    }

    return str;
  }

  public static String getFileExt(String fileName) {
    if (fileName.lastIndexOf(".") > -1) {
      return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    return "";
  }
}