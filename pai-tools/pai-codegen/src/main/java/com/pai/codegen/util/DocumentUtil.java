package com.pai.codegen.util;

import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.main.CodeGenerator;
import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * XML文件读取工具类
 * @author Administrator
 *
 */
public class DocumentUtil
{
  public static Document readDocument(String rootPath)
  {
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      String configXmlPath = PathUtil.getConfigXmlPath();
      document = saxReader.read(new File(configXmlPath));
    } catch (DocumentException e) {
      throw new GeneralException(CodeGenerator.class, "读取XML出错!", e);
    }
    return document;
  }
}