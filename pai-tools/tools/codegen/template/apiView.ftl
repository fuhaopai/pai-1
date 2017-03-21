<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
package com.${sys}.app.web.${module}.view;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * 对象功能: ${model.tabComment} API 实体信息封装
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@ApiModel( value = "${class}View", description = "${model.tabComment} API 实体信息封装" )
public class ${class}View {

}
