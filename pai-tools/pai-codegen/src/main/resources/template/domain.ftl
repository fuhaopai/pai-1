<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
package com.${sys}.biz.${module}.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.pai.biz.frame.domain.AbstractDomain;
import com.pai.base.core.helper.SpringHelper;
import com.${sys}.biz.${module}.persistence.dao.${class}Dao;
import com.${sys}.biz.${module}.persistence.entity.${class}Po;

/**
 * 对象功能:${model.tabComment} 领域对象实体
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@SuppressWarnings("serial")
@Service
@Scope("prototype")
public class ${class} extends AbstractDomain<String, ${class}Po>{
	 
	private ${class}Dao ${classVar}Dao = null;

	protected void init(){
		${classVar}Dao = SpringHelper.getBean(${class}Dao.class);
		setDao(${classVar}Dao);
	}	 
	 
}
