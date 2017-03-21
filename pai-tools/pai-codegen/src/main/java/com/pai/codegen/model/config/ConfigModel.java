package com.pai.codegen.model.config;

import com.pai.codegen.model.gen.GenTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 模型配置处理类:
 * 1. configPropertiesModel: 数据库连接属性配置
 * 2.variables：系统变量
 * 3.templateCollection：模板文件集
 * 4.filesMap和dirsMap：迭代系统量keys获得的模板文件生成路径及模板文件配置
 * 5.tables：代码生成的文件信息（表名，类名，模块，生成哪些文件...）
 * @author Administrator
 *
 */
public class ConfigModel
{
  private ConfigProperties configPropertiesModel;
  private Map<String, String> variables = new HashMap();
  private TemplatePathCollection templateCollection;
  private List<GenTable> tables = new ArrayList();
  private Map<String, TemplateFileCollection> filesMap = new HashMap();
  private Map<String, DirPrefixCollection> dirsMap = new HashMap();

  public String getCharset()
  {
    return this.configPropertiesModel.getCharset();
  }

  public ConfigProperties getConfigPropertiesModel() {
    return this.configPropertiesModel;
  }

  public void setConfigPropertiesModel(ConfigProperties configPropertiesModel) {
    this.configPropertiesModel = configPropertiesModel;
  }

  public Map<String, String> getVariables()
  {
    return this.variables;
  }

  public void setVariables(Map<String, String> variables) {
    this.variables = variables;
  }

  public TemplatePathCollection getTemplateCollection()
  {
    return this.templateCollection;
  }

  public void setTemplateCollection(TemplatePathCollection templateCollection) {
    this.templateCollection = templateCollection;
  }

  public List<GenTable> getTables() {
    return this.tables;
  }

  public void addTable(GenTable genTable) {
    this.tables.add(genTable);
  }

  public boolean hasFiles() {
    if (this.filesMap.size() > 0) {
      return true;
    }
    return false;
  }

  public void addFiles(String key, TemplateFileCollection dbFiles) {
    this.filesMap.put(key, dbFiles);
  }

  public TemplateFileCollection getFiles(String key) {
    return (TemplateFileCollection)this.filesMap.get(key);
  }

  public void addDirs(String key, DirPrefixCollection dirPrefixCollection) {
    this.dirsMap.put(key, dirPrefixCollection);
  }

  public DirPrefixCollection getDirs(String key) {
    return (DirPrefixCollection)this.dirsMap.get(key);
  }

  public String[] getKeys() {
    String keys = (String)getVariables().get("keys");
    String[] keyArray = keys.split(",");
    return keyArray;
  }
}