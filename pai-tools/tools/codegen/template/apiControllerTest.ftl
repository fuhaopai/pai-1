<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
package com.${sys}.app.api.${module}.test.controller;

import org.junit.Test;

import com.pai.apiservice.APIControllerBaseTest;

/**
 * 对象功能: ${model.tabComment} 对外API服务的单元测试类
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class}APIControllerTest extends APIControllerBaseTest{
	
	@Override
	public void initRemotePrefix() {
		remotePrefix = "http://10.105.13.111:8088";		
	}

	@Override
	public void initLocalPrefix() {
		localPrefix = "http://localhost:8080/app-apiservice";
	}	
	
	/**
	 * 更改这个值切换测试本地或远程
	 */
	@Override
	public void initTestLocal() {
		testLocal = true;		
	}

	/**
	 * 测试集合查询
	 * @throws Exception
	 */
	@Test
	public void testList() throws Exception{		

	}

	/**
	 * 测试新增
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception{		

	}


	/**
	 * 测试获取单个对象
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception{		

	} 
	
	/**
	 * 测试更新单个对象
	 * @throws Exception
	 */	
	@Test
	public void testUpdate() throws Exception{

	}
		
	/**
	 * 测试删除单个对象
	 * @throws Exception
	 */		
	@Test
	public void testDelete() throws Exception{

	}
}