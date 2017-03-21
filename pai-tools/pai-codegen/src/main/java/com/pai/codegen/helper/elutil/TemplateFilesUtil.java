package com.pai.codegen.helper.elutil;

import com.pai.codegen.model.config.TemplateFileCollection;
import java.util.Iterator;
import org.dom4j.Element;

public class TemplateFilesUtil
{
  public static TemplateFileCollection buildTemplateFiles(Element templateFiles)
  {
    TemplateFileCollection templateFileCollection = new TemplateFileCollection();
    Iterator j;
    if (templateFiles != null) {
      for (j = templateFiles.elementIterator("file"); j.hasNext(); ) {
        Element fileEl = (Element)j.next();
        templateFileCollection.addTemplateFile(fileEl);
      }
    }
    return templateFileCollection;
  }
}