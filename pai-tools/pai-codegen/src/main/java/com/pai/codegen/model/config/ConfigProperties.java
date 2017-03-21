package com.pai.codegen.model.config;

import com.pai.codegen.util.PathUtil;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
/**
 * 数据库连接属性文件工具类
 * @author Administrator
 *
 */
public class ConfigProperties
{
  private String charset;
  private String dbHelperClass;
  private String url;
  private String username;
  private String password;
  private String authCode;

  public ConfigProperties()
  {
    try
    {
      String proPath = PathUtil.getPropertiesPath();
      InputStream in = new BufferedInputStream(new FileInputStream(proPath));
      Properties prop = new Properties();
      prop.load(in);
      this.charset = prop.getProperty("charset");
      this.dbHelperClass = prop.getProperty("dbHelperClass");
      this.url = prop.getProperty("url");
      this.username = prop.getProperty("username");
      this.password = prop.getProperty("password");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getCharset() {
    return this.charset;
  }

  public String getDbHelperClass() {
    return this.dbHelperClass;
  }

  public String getUrl() {
    return this.url;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }
}