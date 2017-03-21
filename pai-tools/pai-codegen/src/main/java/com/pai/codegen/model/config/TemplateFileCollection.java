package com.pai.codegen.model.config;

import com.pai.codegen.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class TemplateFileCollection
{
  private List<TemplateFile> files = new ArrayList();

  public void addTemplateFile(Element fileEl) {
    String refTemplate = fileEl.attributeValue("refTemplate");
    String filename = fileEl.attributeValue("filename");
    String dir = fileEl.attributeValue("dir");
    String sub = fileEl.attributeValue("sub");
    String override = fileEl.attributeValue("override");

    boolean isOverride = false;
    if ((StringUtils.isNotEmpty(override)) && (override.equals("true"))) {
      isOverride = true;
    }
    String append = fileEl.attributeValue("append");

    TemplateFile file = null;

    if ((append != null) && (append.toLowerCase().equals("true"))) {
      String insertTag = fileEl.attributeValue("insertTag");
      String startTag = fileEl.attributeValue("startTag");
      String endTag = fileEl.attributeValue("endTag");
      if (insertTag == null)
        insertTag = "<!--insertbefore-->";
      if (StringUtils.isEmpty(startTag)) startTag = "";
      if (StringUtils.isEmpty(endTag)) endTag = "";
      if ((sub != null) && (sub.toLowerCase().equals("true")))
        file = new TemplateFile(refTemplate, filename, dir, true, isOverride, true, insertTag, startTag, endTag);
      else {
        file = new TemplateFile(refTemplate, filename, dir, false, isOverride, true, insertTag, startTag, endTag);
      }
    }
    else if ((sub != null) && (sub.toLowerCase().equals("true"))) {
      file = new TemplateFile(refTemplate, filename, dir, true, isOverride, false);
    } else {
      file = new TemplateFile(refTemplate, filename, dir, false, isOverride, false);
    }

    this.files.add(file);
  }

  public List<TemplateFile> getTemplateFiles()
  {
    return this.files;
  }
}