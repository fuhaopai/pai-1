<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.getPkVar(model) >
package com.${sys}.app.admin.${module}.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.ModelAndView;

import com.pai.base.core.constants.ActionMsgCode;
import com.pai.base.core.entity.CommonResult;
import com.pai.app.web.core.framework.web.controller.JUnitControllerBase;
import com.pai.base.core.util.date.DateConverter;
import com.pai.${sys}.${module}.domain.${class};
import com.pai.${sys}.${module}.repository.${class}Repository;
import com.pai.${sys}.${module}.persistence.entity.${class}Po;

public class ${class}ControllerTest extends JUnitControllerBase{
	@Resource
	${class}Repository ${classVar}Repository;

	@Test
	@Rollback(true)
	public void testListData() throws Exception{          
        // 执行URI对应的action
        String uri = "/${sys}/admin/${module}/${classVar}/listData.do";
        ResultActions resultActions = mockMvc.perform((get(uri))).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        //返回body并打印
        String body = mvcResult.getResponse().getContentAsString();
        p(body);
        
        //将整个请求和返回全部打印
        resultActions.andDo(print());
	}
	
	@Test
	@Rollback(true)
	public void testEdit() throws Exception{
        String uri = "/${sys}/admin/${module}/${classVar}/edit.do";

        //新增
        ResultActions resultActions = mockMvc.perform((get(uri))).andExpect(status().isOk());            
        ModelAndView mav = resultActions.andReturn().getModelAndView();	       	                  
        ${class}Po ${classVar}Po = (${class}Po) mav.getModel().get("${classVar}Po");
        Assert.assertNotNull(${classVar}Po);
        Assert.assertNull(${classVar}Po.getId());        
        
        //编辑
        ${class} last${class} = ${classVar}Repository.getLast();        
        if(last${class}!=null){
            resultActions = mockMvc.perform((get(uri).param("id",last${class}.getId()))).andExpect(status().isOk());            
            mav = resultActions.andReturn().getModelAndView();	       	                  
	        ${classVar}Po = (${class}Po) mav.getModel().get("${classVar}Po");
	        System.out.println(${classVar}Po);             
	        Assert.assertNotNull(${classVar}Po);
        }        
	}
	
	@Test
	@Rollback(true)
	public void testSave() throws Exception{
        ${class} ${classVar} = ${classVar}Repository.getLast();
        if(${classVar}==null){
        	return;
        }
        String uri = "/${sys}/admin/${module}/${classVar}/save.do";

        MockHttpServletRequestBuilder builder = post(uri)
		<#list model.columnList as col><#assign columnName=func.convertUnderLine(col.columnName)>
			<#if col.isPK>.param("${columnName}",${classVar}.getData().get${columnName?cap_first}())<#else><#if col.colType="java.util.Date">.param("${columnName}", DateConverter.toString(${classVar}.getData().get${columnName?cap_first}()))<#if !col_has_next>;</#if><#elseif col.colType="char">.param("${columnName}",String.valueOf(${classVar}.getData().get${columnName?cap_first}()))<#if !col_has_next>;</#if><#elseif col.colType!="String">.param("${columnName}",${classVar}.getData().get${columnName?cap_first}().toString())<#if !col_has_next>;</#if><#else>.param("${columnName}",${classVar}.getData().get${columnName?cap_first}())<#if !col_has_next>;</#if></#if></#if>
		</#list>
		        
        //执行和返回Body
        CommonResult commonResult = executeCommonResult(builder);
        
        Assert.assertTrue(commonResult.isSuccess());
        Assert.assertEquals(ActionMsgCode.CREATE.name(), commonResult.getMsgCode());
        
        builder.param("id",${classVar}.getId())
                	.param("createBy", ${classVar}.getData().getCreateBy())
                	.param("createTime", DateConverter.toString(${classVar}.getData().getCreateTime()));
        
        //执行和返回Body
        commonResult = executeCommonResult(builder);
        
        Assert.assertTrue(commonResult.isSuccess());
        Assert.assertEquals(ActionMsgCode.UPDATE.name(), commonResult.getMsgCode());        
        
	}	
	
	@Test
	@Rollback(true)	
	public void testDelete() throws Exception{
		${class} ${classVar} = ${classVar}Repository.getLast();
		if(${classVar}!=null){
			String uri = "/${sys}/admin/${module}/${classVar}/delete.do";
	
	        MockHttpServletRequestBuilder builder = get(uri)
	        		.param("id",${classVar}.getId());
	        
	        CommonResult commonResult = executeCommonResult(builder);
	
	        Assert.assertTrue(commonResult.isSuccess());
	        Assert.assertEquals(ActionMsgCode.DELETE.name(), commonResult.getMsgCode());
        }
	}
}
