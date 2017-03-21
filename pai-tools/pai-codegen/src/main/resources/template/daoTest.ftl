<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign po=class + "Po">
<#assign poVar=classVar + "Po">
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign baseClass=model.variables.baseClass>
package com.${sys}.biz.${module}.test.persistence.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.${sys}.biz.${module}.persistence.dao.${class}Dao;
import com.${sys}.biz.${module}.persistence.dao.${class}QueryDao;
import com.${sys}.biz.${module}.persistence.entity.${po};
import com.${sys}.biz.${module}.test.${baseClass?cap_first}BaseTest;

public class ${class}DaoTest extends ${baseClass?cap_first}BaseTest{

	@Resource
	private ${class}Dao ${classVar}Dao;
	
	@Resource
	private ${class}QueryDao ${classVar}QueryDao;
		
	@Test
	@Rollback(true)
	public void testCrud(){
		${po} ${poVar}=new ${po}();
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
		
		//创建一实体
		${classVar}Dao.create(${poVar});
        Assert.assertNotNull(${poVar}.getId());
        logger.debug("${poVar}1:"+ ${poVar}.getId());
		//获取一实体
		${po} ${poVar}2=${classVar}QueryDao.get(${poVar}.getId());
		Assert.assertNotNull(${poVar}2);
		logger.debug("${poVar}2:" + ${poVar}2.toString());
		Integer randId2=new Double(100000*Math.random()).intValue();
		<#list model.columnList as col>
			<#assign columnName=func.convertUnderLine(col.columnName)>
			<#if !col.isPK>
		<#if col.isNotNull>
		<#if col.colType="java.util.Date">
		${poVar}2.set${columnName?cap_first}(new Date());
		<#elseif col.colType="Float">
		${poVar}2.set${columnName?cap_first}(Float.parseFloat(randId2+""));
		<#elseif col.colType="Short">
		${poVar}2.set${columnName?cap_first}(new Short("1"));
		<#elseif col.colType="Integer">
		${poVar}2.set${columnName?cap_first}(randId2);
		<#elseif col.colType="Long">
		${poVar}2.set${columnName?cap_first}(Long.parseLong(randId2+""));
		<#elseif col.colType="String">
		${poVar}2.set${columnName?cap_first}("${poVar}" + randId2);
		</#if>
		</#if>
			</#if>
		</#list>
		//更新一实体
		${classVar}Dao.update(${poVar}2);
		
		${po} ${poVar}3=${classVar}QueryDao.get(${poVar}.getId());
		System.out.println("${poVar}3:"+${poVar}3.toString());
		//删除一实体
		//${classVar}Dao.remove(${poVar}.getId());
	}
	
}
