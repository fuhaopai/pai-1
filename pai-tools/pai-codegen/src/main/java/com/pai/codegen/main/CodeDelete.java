package com.pai.codegen.main;

import com.pai.codegen.context.ContextHolder;
import com.pai.codegen.helper.ConfigModelHelper;
import com.pai.codegen.main.util.DoTypeUtil;
import com.pai.codegen.model.CommonResult;
import com.pai.codegen.model.config.ConfigModel;
import com.pai.codegen.model.config.DirPrefixCollection;
import com.pai.codegen.model.config.TemplateFile;
import com.pai.codegen.model.config.TemplateFileCollection;
import com.pai.codegen.model.gen.GenSubTable;
import com.pai.codegen.model.gen.GenTable;
import com.pai.codegen.util.CodeGenUtil;
import com.pai.codegen.util.PathUtil;
import com.pai.codegen.util.StringUtils;
import com.pai.codegen.util.XmlUtils;
import java.util.List;
import java.util.Map;

public class CodeDelete
{
  private static String rootPath;

  public static void setRootPath(String path)
  {
    rootPath = path;
  }

  public void deleteFileByConfig(ConfigModel configModel)
    throws Exception
  {
    List<GenTable> tableList = configModel.getTables();

    String charset = configModel.getCharset();
    String workspace = (String)configModel.getVariables().get("workspace");

    for (GenTable table : tableList) {
    	Map variables = table.getVariable();
    	String doType = (String)variables.get("doType");
    	String module = (String)variables.get("module");
       String sys = (String)variables.get("sys");
       String[] keys = configModel.getKeys();
       String dirPrefix;
      for (String key : keys) {
        if (DoTypeUtil.isDo(doType, key)) {
          TemplateFileCollection fileCollection = configModel.getFiles(key);
          dirPrefix = workspace + configModel.getDirs(key).get(module);
          for (TemplateFile templateFile : fileCollection.getTemplateFiles()) {
            String fileDir = getDir(templateFile, dirPrefix, variables);
            deleteFile(templateFile, fileDir, charset, variables);
          }

        }

      }

      List<GenSubTable> genSubTables = table.getSubtable();
      if ((genSubTables != null) && (genSubTables.size() != 0))
        for (GenSubTable subtable : genSubTables) {
          Map vars = subtable.getVars();
          dirPrefix = "";
          for (String key : keys)
            if (DoTypeUtil.isDo(doType, key)) {
              TemplateFileCollection fileCollection = configModel.getFiles(key);
              dirPrefix = configModel.getDirs(key).get(module);
              for (TemplateFile templateFile : fileCollection.getTemplateFiles()) {
                String fileDir = getDir(templateFile, dirPrefix, variables);
                deleteFile(templateFile, fileDir, charset, vars); }  }   }  
    }
  }

  private void deleteFile(TemplateFile templateFile, String fileDir, String charset, Map<String, String> variables) throws Exception { 
	  String filename = StringUtils.replaceVariable(templateFile.getFilename(), variables);

    if (templateFile.isAppend()) {
      String startTag = StringUtils.replaceVariable(templateFile.getStartTag(), variables);
      String endTag = StringUtils.replaceVariable(templateFile.getEndTag(), variables);

      CodeGenUtil.deleteAppendFile(fileDir, filename, charset, startTag, endTag);
    } else {
      CodeGenUtil.deleteFile(fileDir, filename);
    } }

  private String getDir(TemplateFile templateFile, String baseDir, Map<String, String> variables) throws Exception
  {
    String dir = templateFile.getDir();
    dir = StringUtils.replaceVariable(dir, variables);
    String fileDir = baseDir + "\\" + dir;
    return fileDir;
  }

  private String getSubTableFileDir(String dir, String baseDir, Map<String, String> vars) {
    String fileDir = dir;
    if (fileDir.indexOf("{") != -1) {
      String var = fileDir.substring(fileDir.indexOf(123) + 1, fileDir.indexOf(125));
      fileDir = fileDir.substring(0, fileDir.indexOf(123)) + (String)vars.get(var);
    }
    return baseDir + "\\" + fileDir;
  }

  private String getSubTableFileName(String fileName, Map<String, String> vars) {
    String filename = fileName;
    if (filename.indexOf("{") != -1) {
      String var = filename.substring(filename.indexOf(123) + 1, filename.indexOf(125));
      filename = (String)vars.get(var) + filename.substring(filename.indexOf(125) + 1);
    }
    return filename;
  }

  public void execute() throws Exception
  {
    ContextHolder.setRootPath(rootPath);

    String xsdPath = PathUtil.getXsdPath();

    String xmlPath = PathUtil.getConfigXmlPath();

    CommonResult result = XmlUtils.validXmlBySchema(xsdPath, xmlPath);
    if (result.isSuccess())
    {
      ConfigModel configModel = ConfigModelHelper.loadByConfigXml();
      deleteFileByConfig(configModel);
    }
  }

  public static void main(String[] args) throws Exception {
    CodeDelete codeDelete = new CodeDelete();
    setRootPath("D:\\workspace1\\V1.2\\pai-codegen\\src\\main\\resources");
    codeDelete.execute();
  }
}