package com.pai.codegen.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pai.codegen.model.config.ConfigModel;
import com.pai.codegen.model.config.TemplateFile;
import com.pai.codegen.model.table.TableModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 代码生成工具类
 * @author Administrator
 *
 */
public class CodeGenUtil
{
	/**
	 * 代码生成
	 * @param file 模板文件（模板，文件名，路径等参数）
	 * @param configModel （系统变量）
	 * @param tableModel 数据库表参数（表名，sys, module, class, baseClass, doType)等参数
	 * @param baseDir  文件base目录。 完整目录文件名为：baseDir+file.getDir()+file.getFilename();
	 * @param cfg
	 * @return
	 * @throws Exception
	 */
  public static String generateFile(TemplateFile file, ConfigModel configModel, TableModel tableModel, String baseDir, Configuration cfg)
    throws Exception
  {
    String tbName = tableModel.getTableName();
    Map<String,String> variables = tableModel.getVariables();
    boolean isSub = tableModel.getSub();

    String filename = file.getFilename();
    String startTag = file.getStartTag();
    String endTag = file.getEndTag();
    boolean sub = file.isSub();
    boolean override = file.isOverride();
    if ((isSub == true) && (!sub)) {
      return "";
    }
    Map<String,String> stringVariables = new HashMap<String,String>();
    stringVariables.put("tabname", tbName);
    startTag = StringUtils.replaceVariable(startTag, stringVariables);
    endTag = StringUtils.replaceVariable(endTag, stringVariables);

    String dir = file.getDir();
    filename = StringUtils.replaceVariable(filename, variables);
    dir = StringUtils.replaceVariable(dir, variables);
    dir = StringUtils.combilePath(baseDir, dir);
    dir = StringUtils.replaceVariable(dir, variables);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("model", tableModel);
    map.put("vars", configModel.getVariables());
    map.put("date", new Date());

    String tplPath = configModel.getTemplateCollection().getTemplatePath(file.getRefTemplate());
    if (file.isAppend()) {
      appendFile(dir, filename, tplPath, configModel.getCharset(), cfg, map, file.getInsertTag(), startTag, endTag);
    }
    else if (override) {
      generateFile(dir, filename, tplPath, configModel.getCharset(), cfg, map);
    } else {
      File f = new File(dir + "\\" + filename);
      if (!f.exists()) {
        generateFile(dir, filename, tplPath, configModel.getCharset(), cfg, map);
      }
    }

    return filename;
  }

  public static void deleteAppendFile(String fileDir, String filename, String charset, String startTag, String endTag)
    throws Exception
  {
    String filepath = StringUtils.combilePath(fileDir, filename);
    File file = new File(filepath);
    if (file.exists()) {
      String content = FileUtils.readFile(filepath, charset);
      if (content.indexOf(startTag) != -1) {
        content = content.substring(0, content.indexOf(startTag)).trim() + "\r\n" + content.substring(content.indexOf(endTag) + endTag.length()).trim();

        file.delete();
        file = new File(filepath);
        FileUtils.writeFile(content.trim(), file, charset);
        System.out.println("删除了内容" + startTag + "-----" + endTag);
      }
    }
  }

  public static void deleteFile(String fileDir, String filename)
  {
    String filepath = StringUtils.combilePath(fileDir, filename);
    File file = new File(filepath);
    if (file.exists()) {
      file.delete();
      System.out.println("删除了文件" + filename);
    } else {
      System.out.println(filename + "该文件不存在");
    }
    if (!FileUtils.isExistFile(fileDir)) {
      file = new File(fileDir);
      file.delete();
    }
  }

  private static void generateFile(String fileDir, String fileName, String freemarkerTemplatePath, String charset, Configuration cfg, Map data)
    throws IOException, TemplateException
  {
    String path = StringUtils.combilePath(fileDir, fileName);

    File newFile = new File(fileDir, fileName);
    if (!newFile.exists()) {
      if (!newFile.getParentFile().exists()) {
        newFile.getParentFile().mkdirs();
      }
      newFile.createNewFile();
    }
    Writer writer = new OutputStreamWriter(new FileOutputStream(newFile), charset);

    Template template = cfg.getTemplate(freemarkerTemplatePath, charset);

    template.process(data, writer);
  }

  private static void appendFile(String fileDir, String fileName, String templatePath, String charset, Configuration cfg, Map data, String insertTag, String startTag, String endTag)
    throws IOException, TemplateException
  {
    String path = StringUtils.combilePath(fileDir, fileName);
    File newFile = new File(fileDir, fileName);

    StringWriter out = new StringWriter();
    Template template = cfg.getTemplate(templatePath, charset);
    template.process(data, out);
    String str = out.toString();

    boolean exists = false;
    String content = "";
    if (newFile.exists()) {
      content = FileUtils.readFile(path, charset);
      if ((StringUtils.isNotEmpty(startTag)) && (StringUtils.isNotEmpty(endTag)) && 
        (StringUtils.isExist(content, startTag, endTag))) {
        content = StringUtils.replace(content, startTag, endTag, str);
        exists = true;
      }

    }

    if (!exists) {
      if ((StringUtils.isNotEmpty(startTag)) && (StringUtils.isNotEmpty(endTag))) {
        str = startTag.trim() + "\r\n" + str + "\r\n" + endTag.trim();
      }
      if (content.indexOf(insertTag) != -1) {
        String[] aryContent = StringUtils.getBySplit(content, insertTag);
        content = aryContent[0] + str + "\r\n" + insertTag + aryContent[1];
      }
      else {
        content = content + "\r\n" + str;
      }
    }
    FileUtils.writeFile(content, newFile, charset);
  }
}