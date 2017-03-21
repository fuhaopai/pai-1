<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
package com.${sys}.app.web.${module}.view;

import com.pai.biz.common.entity.BaseAPIResult;
/**
 * 对象功能: ${model.tabComment} API 列表返回信息
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class}ListAPIResult extends BaseAPIResult{

}
