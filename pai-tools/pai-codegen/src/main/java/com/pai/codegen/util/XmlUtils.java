package com.pai.codegen.util;

import com.pai.codegen.model.CommonResult;
import java.io.File;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
/**
 * codegen.xml与codegen.xsd文件校验工具类
 * @author Administrator
 *
 */
public class XmlUtils
{
  public static CommonResult validXmlBySchema(String xsdPath, String xmlPath)
  {
    CommonResult result = new CommonResult();

    SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

    File schemaFile = new File(xsdPath);

    Schema schema = null;
    try {
      schema = schemaFactory.newSchema(schemaFile);
    } catch (SAXException e) {
      e.printStackTrace();
    }

    Validator validator = schema.newValidator();

    Source source = new StreamSource(FileUtils.getInputStream(xmlPath));
    try
    {
      validator.validate(source);
    } catch (Exception ex) {
      result.setSuccess(false);
      result.setMsg(ex.getMessage());
      return result;
    }
    result.setSuccess(true);
    result.setMsg("");
    return result;
  }
}