package com.pai.codegen.util;

import com.pai.codegen.context.ContextHolder;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.main.CodeGenerator;
import java.io.File;

/**
 * 配置文件地址工具类，返回默认的配置文件地址
 * @author Administrator
 *
 */
public class PathUtil
{
	/**
	 * 返回codegen.xml文件地址，默认地址为: rootPath + "/conf/codegen.xml";
	 * @return
	 * @throws GeneralException
	 */
  public static String getConfigXmlPath()
    throws GeneralException
  {
    String rootPath = ContextHolder.getRootPath();
    String configXml = rootPath + "/conf/codegen.xml";
    File file = new File(configXml);
    if (!file.exists()) {
      throw new GeneralException(CodeGenerator.class, "请确认配置文件：" + configXml + "是否存在!");
    }
    return configXml;
  }

  /**
   * 返回codegen.xsd文件地址，默认地址为: rootPath + "/conf/codegen.xsd";
   * @return
   * @throws GeneralException
   */
  public static String getXsdPath() throws GeneralException
  {
    String rootPath = ContextHolder.getRootPath();
    String path = rootPath + "/conf/codegen.xsd";
    File file = new File(path);
    if (!file.exists()) {
      throw new GeneralException(CodeGenerator.class, "schema文件" + path + "不存在");
    }
    return path;
  }

  /**
   * 返回codegen.properties文件地址，默认地址为:rootPath + "/conf/codegen.properties";
   * @return
   * @throws GeneralException
   */
  public static String getPropertiesPath() throws GeneralException
  {
    String rootPath = ContextHolder.getRootPath();
    String path = rootPath + "/conf/codegen.properties";
    File file = new File(path);
    if (!file.exists()) {
      throw new GeneralException(CodeGenerator.class, "代码生成配置文件" + path + "不存在");
    }
    return path;
  }
}