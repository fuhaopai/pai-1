package com.pai.codegen.helper;

import com.pai.codegen.db.IDbHelper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.main.CodeGenerator;
import com.pai.codegen.model.config.ConfigModel;
/**
 * DB工厂类
 * @author Administrator
 *
 */
public class DbHelperFactory
{
  public static IDbHelper getDbHelper(ConfigModel configModel)
    throws GeneralException
  {
    IDbHelper dbHelper = null;
    String dbHelperClass = configModel.getConfigPropertiesModel().getDbHelperClass();
    try
    {
      dbHelper = (IDbHelper)Class.forName(dbHelperClass).newInstance();
    } catch (InstantiationException e) {
      throw new GeneralException(CodeGenerator.class, "指定的dbHelperClass：" + dbHelperClass + "无法实例化，可能该类是一个抽象类、接口、数组类、基本类型或 void, 或者该类没有默认构造方法!", e);
    }
    catch (IllegalAccessException e) {
      throw new GeneralException(CodeGenerator.class, "指定的dbHelperClass： " + dbHelperClass + "没有默认构造方法或不可访问!", e);
    } catch (ClassNotFoundException e) {
      throw new GeneralException(CodeGenerator.class, "找不到指定的dbHelperClass:" + dbHelperClass + "!", e);
    }
    dbHelper.setUrl(configModel.getConfigPropertiesModel().getUrl(), configModel.getConfigPropertiesModel().getUsername(), configModel.getConfigPropertiesModel().getPassword());

    return dbHelper;
  }
}