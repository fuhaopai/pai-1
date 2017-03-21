<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
package com.${sys}.app.api.${module}.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pai.biz.common.entity.BaseAPIResult;
import com.${sys}.app.web.${module}.view.${class}ListAPIResult;
import com.${sys}.app.web.${module}.view.${class}APIResult;
import com.pai.app.core.framework.web.controller.GenericController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * 对象功能: ${model.tabComment} 对外API服务
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@Controller
@RequestMapping("/api/${sys}/${module}/")
@Api(value = "/api/${sys}/${module}/",basePath="/api/${sys}/${module}/",description = "${model.tabComment} 对外API服务")
public class ${class}APIController extends	GenericController {		
	
	private final static String V1="v1";
	
	@Override
	protected void initController() {
		//TODO 初始化操作
	}

	/**
	 * 查询 ${model.tabComment} 集合
	 * @param request
	 * @param response
	 * @return ${class}ListAPIResult
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
			value = V1 + "/${classVar}s",
			notes = "查询 ${model.tabComment} 集合",
			response = ${class}ListAPIResult.class,
			httpMethod = "GET"
			)	
	public ${class}ListAPIResult list(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//TODO			
		return new ${class}ListAPIResult();
	}
	/**
	 * 往集合添加单个 ${model.tabComment}  实体
	 * @param request
	 * @param response
	 * @return ${class}APIResult
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s",method=RequestMethod.POST)
	@ResponseBody	
	@ApiOperation(
			value = V1 + "/${classVar}s",
			notes = "往集合添加单个 ${model.tabComment}  实体",
			response = ${class}APIResult.class,
			httpMethod = "POST"
			)	
	public ${class}APIResult add(HttpServletRequest request,HttpServletResponse response) throws Exception{		
		return new ${class}APIResult();
	}
	/**
	 * 删除${model.tabComment} 集合，可以传入条件。（慎重，某些模块如果不需要对外提供此功能，请删除此方法）
	 * @param request
	 * @param response
	 * @return ${class}APIResult
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s",method=RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(
			value = V1 + "/${classVar}s",
			notes = "删除${model.tabComment} 集合",
			response = ${class}APIResult.class,
			httpMethod = "DELETE"
			)		
	public ${class}APIResult deleteAll(HttpServletRequest request,HttpServletResponse response) throws Exception{		
		return new ${class}APIResult();
	}
	
	/**
	 * 更新${model.tabComment} 集合
	 * @param request
	 * @param response
	 * @return ${class}ListAPIResult
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(
			value = V1 + "/${classVar}s",
			notes = "更新${model.tabComment} 集合",
			response = ${class}ListAPIResult.class,
			httpMethod = "PUT"
			)			
	public ${class}ListAPIResult updateAll(HttpServletRequest request,HttpServletResponse response) throws Exception{		
		return new ${class}ListAPIResult();
	}

	/**
	 * 查询单个 ${model.tabComment} 实体
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s/{key}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
			value = V1 + "/${classVar}s/{key}",
			notes = "查询单个 ${model.tabComment} 实体",
			response = ${class}APIResult.class,
			httpMethod = "GET"
			)	
	public ${class}APIResult get(@PathVariable String key,HttpServletRequest request,HttpServletResponse response) throws Exception{		
		return new ${class}APIResult();
	}

	/**
	 * 更新单个 ${model.tabComment} 实体
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s/{key}",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(
			value = V1 + "/${classVar}s/{key}",
			notes = "更新单个 ${model.tabComment} 实体",
			response = ${class}APIResult.class,
			httpMethod = "PUT"
			)		
	public ${class}APIResult update(@PathVariable String key,HttpServletRequest request,HttpServletResponse response) throws Exception{		
		return new ${class}APIResult();
	}	
	/**
	 * 删除单个 ${model.tabComment} 实体
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value=V1 + "/${classVar}s/{key}",method=RequestMethod.DELETE)
	@ResponseBody	
	@ApiOperation(
			value = V1 + "/${classVar}s/{key}",
			notes = "删除单个 ${model.tabComment} 实体",
			response = ${class}APIResult.class,
			httpMethod = "DELETE"
			)		
	public BaseAPIResult delete(@PathVariable String key,HttpServletRequest request,HttpServletResponse response) throws Exception{		
		return new BaseAPIResult();
	}
	
}
