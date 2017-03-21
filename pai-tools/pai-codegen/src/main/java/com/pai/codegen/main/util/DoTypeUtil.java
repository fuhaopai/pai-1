package com.pai.codegen.main.util;

import com.pai.codegen.util.StringUtils;

public class DoTypeUtil
{
  private static final String ALL = "all";
  private static final String NO = "no";

  public static boolean isDo(String doType, String currentType)
  {
    if (StringUtils.isEmpty(doType)) {
      return true;
    }
    doType = doType.toLowerCase();

    boolean isDo = false;

    if (doType.indexOf(currentType) > -1) {
      if (doType.indexOf(NO + currentType) <= -1) {
        isDo = true;
      }
    }
    else if (doType.indexOf(ALL) > -1) {
      isDo = true;
    }

    return isDo;
  }
}