<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
<#assign baseClass=model.variables.baseClass>
<#assign poVar=classVar + "Po">
package com.${sys}.biz.${module}.test.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.pai.base.db.mybatis.impl.domain.MyBatisPage;
import com.${sys}.biz.${module}.domain.${class};
import com.${sys}.biz.${module}.repository.${class}Repository;
import com.${sys}.biz.${module}.persistence.entity.${class}Po;
import com.${sys}.biz.${module}.test.${baseClass}BaseTest;

/**
 * 对象功能:${model.tabComment} 领域对象实体单元测试类
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class}Test extends ${baseClass?cap_first}BaseTest{
	 
	@Resource
	private ${class}Repository ${classVar}Repository;
	
	@Test
	@Rollback(true)
	public void create(){				
		${class} ${classVar} = ${classVar}Repository.newInstance();
		
		${class}Po ${classVar}Po=new ${class}Po();
		Integer randId=new Double(100000*Math.random()).intValue();
		<#list model.columnList as col>
			<#assign columnName=func.convertUnderLine(col.columnName)>
		<#if col.isPK>
		${poVar}.setId(idGenerator.genSid());
		<#else>
		<#if col.isNotNull>
		<#if col.colType="java.util.Date">
		${poVar}.set${columnName?cap_first}(new Date());
		<#elseif col.colType="Float">
		${poVar}.set${columnName?cap_first}(Float.parseFloat(randId+""));
		<#elseif col.colType="Short">
		${poVar}.set${columnName?cap_first}(new Short("1"));
		<#elseif col.colType="Integer">
		${poVar}.set${columnName?cap_first}(randId);
		<#elseif col.colType="Long">
		${poVar}.set${columnName?cap_first}(Long.parseLong(randId+""));
		<#elseif col.colType="String">
		${poVar}.set${columnName?cap_first}("${poVar}" + randId);
		</#if>
		</#if>
		</#if>
		</#list>
		
		${classVar}.setData(${classVar}Po);
		
		List<String> ids = new ArrayList<String>();
		
		${classVar}.create();	
		ids.add(${classVar}.getId());
						
		${class} ${classVar}2 = ${classVar}Repository.newInstance();
		${classVar}Po.setId(idGenerator.genSid());
		${classVar}2.setData(${classVar}Po);
		
		${classVar}2.create();
		ids.add(${classVar}2.getId());
		
		List<${class}Po> ${classVar}Pos = ${classVar}Repository.findByIds(ids);
		Assert.assertTrue(${classVar}Pos.size()>=2);
		
		${classVar}Pos = ${classVar}Repository.findAll();
		Assert.assertTrue(${classVar}Pos.size()>=2);
		
		MyBatisPage defaultPage = new MyBatisPage(1,10);
		
		${classVar}Pos = ${classVar}Repository.findPaged(defaultPage);
		Assert.assertTrue(${classVar}Pos.size()>=2);
	}
}
