<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign subtables=model.subTableList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
package com.${sys}.biz.${module}.persistence.entity;
<#if subtables?exists && subtables?size!=0>
import java.util.ArrayList;
import java.util.List;
</#if>
/**
 * 对象功能:${model.tabComment} entity对象
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class}Po extends ${class}Tbl{

	<#if subtables?exists && subtables?size!=0>
	<#list subtables as table>
	<#assign vars=table.variables>
	protected List<${vars.class}Po> ${vars.classVar}PoList=new ArrayList<${vars.class}Po>(); 		/*${table.tabComment}列表*/
	</#list>
	</#if>
	
<#if subtables?exists && subtables?size!=0>
<#list subtables as table>
<#assign vars=table.variables>
	public void set${vars.classVar?cap_first}PoList(List<${vars.class}Po> ${vars.classVar}PoList) 
	{
		this.${vars.classVar}PoList = ${vars.classVar}PoList;
	}
	/**
	 * 返回 ${table.tabComment}列表
	 * @return
	 */
	public List<${vars.class}Po> get${vars.classVar?cap_first}PoList() 
	{
		return this.${vars.classVar}PoList;
	}
</#list>
</#if>
}