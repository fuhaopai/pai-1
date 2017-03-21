package com.pai.codegen.helper;

import com.pai.codegen.context.ContextHolder;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.helper.elutil.DirPrefixUtil;
import com.pai.codegen.helper.elutil.TemplateFilesUtil;
import com.pai.codegen.helper.elutil.VariablesUtil;
import com.pai.codegen.main.CodeGenerator;
import com.pai.codegen.model.CommonResult;
import com.pai.codegen.model.config.ConfigModel;
import com.pai.codegen.model.config.ConfigProperties;
import com.pai.codegen.model.config.DirPrefixCollection;
import com.pai.codegen.model.config.TemplateFileCollection;
import com.pai.codegen.model.config.TemplatePathCollection;
import com.pai.codegen.model.gen.GenTable;
import com.pai.codegen.util.DocumentUtil;
import com.pai.codegen.util.PathUtil;
import com.pai.codegen.util.XmlUtils;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;

public class ConfigModelHelper
{
  public static ConfigModel loadByConfigXml()
    throws Exception
  {
    String rootPath = ContextHolder.getRootPath();

    String xsdPath = PathUtil.getXsdPath();

    String xmlPath = PathUtil.getConfigXmlPath();
    //codegen.xml与codegen.xsd文件校验
    CommonResult result = XmlUtils.validXmlBySchema(xsdPath, xmlPath);
    //校验不通过抛出异常
    if (!result.isSuccess()) {
      throw new GeneralException(CodeGenerator.class, "XML文件通过XSD文件校验失败:" + result);
    }

    Document document = DocumentUtil.readDocument(rootPath);
    Element root = document.getRootElement();

    ConfigModel cm = new ConfigModel();
    //数据库连接属性文件工具类
    ConfigProperties cpm = new ConfigProperties();
    cm.setConfigPropertiesModel(cpm);

    //模板文件集
    Element templatesEl = root.element("templates");
    TemplatePathCollection templateCollection = new TemplatePathCollection(templatesEl);
    cm.setTemplateCollection(templateCollection);
    
    //系统用变量，如：developer， company， keys， workspace
    Element variablesEl = root.element("variables");
    Map variableMap = VariablesUtil.getVariables(variablesEl);
    cm.setVariables(variableMap);

    /**
     * 迭代系统变量获得的keys，并将每个key + "Files"的模板存储到ConfigModel的Files HashMap中备用
     * 同时把每个key + "Dirs"要生成的模板路径存储到ConfigModel的Dirs HashMap中备用
     */
    String[] keyArray = cm.getKeys();
    for (String key : keyArray) {
      Element dbEl = root.element(key + "Files");
      TemplateFileCollection files = TemplateFilesUtil.buildTemplateFiles(dbEl);
      cm.addFiles(key, files);

      Element moduleDirsEl = root.element(key + "Dirs");
      DirPrefixCollection dirPrefixs = DirPrefixUtil.buildDirPrefixCollection(moduleDirsEl);
      cm.addDirs(key, dirPrefixs);
    }

    /**
     * 获取<table></table>标签的信息,包括子表信息
     * 一个codegen.xml文件可以有多个<table>标签
     */
    for (Iterator i = root.elementIterator("table"); i.hasNext(); ) {
      Element tableEl = (Element)i.next();
      GenTable table = new GenTable(tableEl);
      cm.addTable(table);
    }
    return cm;
  }
}