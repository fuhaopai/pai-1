package com.pai.codegen.model.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Element;

 /**
  * 模板文件集，生成不同的文件时使用不同的模板进行解析
  * 此模板文件配置见：codegen.xml
  * 形如：
  * <templates>
  *   <template key="dao" path="dao.ftl" />
  * </templates>
  * @author Administrator
  *
  */
public class TemplatePathCollection
{
  private Map<String, String> templatePathMap = new HashMap();

  public TemplatePathCollection(Element templatesEl) {
    buildTemplates(templatesEl);
  }

  private void buildTemplates(Element templatesEl) {
    for (Iterator j = templatesEl.elementIterator("template"); j.hasNext(); ) {
      Element templateEl = (Element)j.next();
      String id = templateEl.attributeValue("key");
      String path = templateEl.attributeValue("path");
      this.templatePathMap.put(id, path);
    }
  }

  public String getTemplatePath(String key)
  {
    return (String)this.templatePathMap.get(key);
  }
}