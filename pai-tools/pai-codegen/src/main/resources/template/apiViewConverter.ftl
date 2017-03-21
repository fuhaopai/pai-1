<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
package com.${sys}.app.web.${module}.helper;

import java.util.ArrayList;
import java.util.List;

import com.pai.apiservice.common.helper.ViewConverter;
import com.${sys}.app.web.${module}.view.${class}View;
import com.${sys}.app.web.${module}.persistence.entity.${class}Po;

/**
 * 对象功能: ${model.tabComment} API 实体信息封装转换器
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class}ViewConverter implements ViewConverter<${class}View, ${class}Po>{

	public ${class}View converter(${class}Po po) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<${class}View> converter(List<${class}Po> pos) {
		List<${class}View> ${classVar}Views = new ArrayList<${class}View>();
		for(${class}Po po:pos){
			${classVar}Views.add(converter(po));
		}
		return ${classVar}Views;
	}

}
