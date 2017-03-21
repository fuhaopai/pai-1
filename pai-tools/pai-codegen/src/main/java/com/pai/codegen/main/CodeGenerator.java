package com.pai.codegen.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.pai.codegen.context.ContextHolder;
import com.pai.codegen.db.IDbHelper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.helper.ConfigModelHelper;
import com.pai.codegen.helper.DbHelperFactory;
import com.pai.codegen.helper.TableModelHelper;
import com.pai.codegen.main.util.DoTypeUtil;
import com.pai.codegen.model.config.ConfigModel;
import com.pai.codegen.model.config.TemplateFile;
import com.pai.codegen.model.config.TemplateFileCollection;
import com.pai.codegen.model.table.TableModel;
import com.pai.codegen.util.CodeGenUtil;
import com.pai.codegen.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class CodeGenerator
{
  private static String rootPath;

  public static void setRootPath(String path)
  {
    rootPath = path;
  }

  public void execute() throws Exception {
    try
    {
    	//设置配置文件所在的路径
      ContextHolder.setRootPath(rootPath);
      //代码生成配置文件的所有信息（数据库连接信息，代码模板文件，variables，table(subtable)，key+Files, key+Dirs)
      ConfigModel configModel = ConfigModelHelper.loadByConfigXml();

      IDbHelper dbHelper = DbHelperFactory.getDbHelper(configModel);
      //获得所有表信息，包括子表(表的注释，表的字段）
      List<TableModel> tableModels = TableModelHelper.getTableModelList(dbHelper, configModel);

      Configuration freemarkerCfg = new Configuration();
      File templateLocation = new File(ContextHolder.getRootPath() + "template");
      freemarkerCfg.setDirectoryForTemplateLoading(templateLocation);
      //生成文件
      genTableByConfig(configModel, tableModels, freemarkerCfg);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void genTableByConfig(ConfigModel configModel, List<TableModel> tableModels, Configuration freemarkerCfg)
    throws Exception
  {
    try
    {
      if ((tableModels == null) || (tableModels.size() == 0)) {
        System.out.println("没有指定生成的表!");
        return;
      }

      if (!configModel.hasFiles()) {
        System.out.println("没有指定生成的文件!");
        return;
      }

      System.out.println("*********开始生成**********");
      String workspace = (String)configModel.getVariables().get("workspace");
      for (TableModel tableModel : tableModels) {
        String doType = (String)tableModel.getVariables().get("doType");
        String module = (String)tableModel.getVariables().get("module");
        String[] keys = configModel.getKeys();
        boolean hasApi = false;
        String dirPrefix;
        /**
         * 迭代variables标签的keys值，与table标签的doType比较
         * 如果doType值包含keys中的值，则按照keys中对应值的模板文件生成相应的文件。
         */
        for (String key : keys)
        {
          if (DoTypeUtil.isDo(doType, key)) {
            TemplateFileCollection fileCollection = configModel.getFiles(key);
            //生成的文件存放目录
            dirPrefix = workspace + configModel.getDirs(key).get(module);
            /**
             * 迭代key+Files标签的模板配置并生成文件，存储路径为:dirPrefix+该模板的dir，文件名为：该模板的filename值，调用的模板为该模板的refTemplate对应的模板
             * 
             * 即使用refTemplate指定的模板生成文件名为filename值的文件，生成的文件路径为dirPrefix+该模板的dir
             */
            for (TemplateFile templateFile : fileCollection.getTemplateFiles()) {
              String filename = CodeGenUtil.generateFile(templateFile, configModel, tableModel, dirPrefix, freemarkerCfg);
              if (StringUtils.isNotEmpty(filename)) {
                System.out.println(filename + " 生成成功!");
              }
            }
          }
          if ((StringUtils.isNotEmpty(doType)) && ((doType.equals("all")) || (doType.indexOf("api") > -1))) {
            hasApi = true;
          }
        }
        if (hasApi) {
          String API_BASE = "apiBase";
          TemplateFileCollection fileCollection = configModel.getFiles(API_BASE);
          dirPrefix = workspace + configModel.getDirs(API_BASE).get(module);
          if (StringUtils.isEmpty(configModel.getDirs(API_BASE).get(module))) {
            dirPrefix = workspace + configModel.getDirs(API_BASE).get("all");
          }
          for (TemplateFile templateFile : fileCollection.getTemplateFiles()) {
            String filename = CodeGenUtil.generateFile(templateFile, configModel, tableModel, dirPrefix, freemarkerCfg);
            if (StringUtils.isNotEmpty(filename))
              System.out.println(filename + " 生成成功!1111");
          }
        }
      }
      System.out.println("*********所有文件生成成功!**********");
    } catch (IOException e) {
      throw new GeneralException(CodeGenerator.class, e);
    } catch (TemplateException e) {
      throw new GeneralException(CodeGenerator.class, "freemarker执行出错!", e);
    }
  }

  public static void main(String[] args)
    throws Exception
  {
    CodeGenerator codeGenerator = new CodeGenerator();
    setRootPath("D:\\workspace-pai\\pai-tools\\pai-codegen\\src\\main\\resources");
    codeGenerator.execute();
  }
}