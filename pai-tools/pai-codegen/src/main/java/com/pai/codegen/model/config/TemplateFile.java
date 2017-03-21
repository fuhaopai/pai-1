package com.pai.codegen.model.config;

public class TemplateFile
{
  private String refTemplate;
  private String filename;
  private String dir;
  private boolean sub = false;
  private boolean override = false;
  private boolean append = false;
  private String insertTag = "";
  private String startTag = "start{tabname}";
  private String endTag = "end{tabname}";

  public TemplateFile(String refTemplate, String filename, String dir, boolean sub, boolean override, boolean append, String insertTag, String startTag, String endTag) {
    this.refTemplate = refTemplate;
    this.filename = filename;
    this.dir = dir;
    this.sub = sub;
    this.override = override;
    this.append = append;
    this.insertTag = insertTag;
    this.startTag = startTag;
    this.endTag = endTag;
  }

  public TemplateFile(String refTemplate, String filename, String dir, boolean sub, boolean override, boolean append) {
    this.refTemplate = refTemplate;
    this.filename = filename;
    this.dir = dir;
    this.sub = sub;
    this.override = override;
    this.append = append;
    this.insertTag = "";
    this.startTag = "";
    this.endTag = "";
  }

  public String getRefTemplate()
  {
    return this.refTemplate;
  }

  public void setRefTemplate(String refTemplate) {
    this.refTemplate = refTemplate;
  }

  public boolean isSub() {
    return this.sub;
  }

  public void setSub(boolean sub) {
    this.sub = sub;
  }

  public boolean isOverride() {
    return this.override;
  }

  public void setOverride(boolean sub) {
    this.override = this.override;
  }

  public String getFilename()
  {
    return this.filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getDir()
  {
    return this.dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }

  public boolean isAppend() {
    return this.append;
  }

  public void setAppend(boolean append) {
    this.append = append;
  }

  public String getInsertTag() {
    return this.insertTag;
  }

  public void setInsertTag(String insertTag) {
    this.insertTag = insertTag;
  }

  public String getStartTag() {
    return this.startTag;
  }

  public void setStartTag(String startTag) {
    this.startTag = startTag;
  }

  public String getEndTag() {
    return this.endTag;
  }

  public void setEndTag(String endTag) {
    this.endTag = endTag;
  }
}