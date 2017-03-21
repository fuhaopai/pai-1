 package com.pai.codegen.constants;
 
 public enum ImageType
 {
   JPG, JPEG, PNG, GIF, BMP, WMF, JPE, TIF;
 
  public static String[] toArray() { String[] array = new String[values().length];
     for (int i = 0; i < values().length; i++) {
      array[i] = values()[i].name().toLowerCase();
     }
     return array;
   }
 }