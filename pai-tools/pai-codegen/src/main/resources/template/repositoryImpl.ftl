<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
package com.${sys}.biz.${module}.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.pai.base.core.helper.SpringHelper;
import com.pai.base.db.persistence.dao.IQueryDao;
import com.pai.biz.frame.repository.AbstractRepository;
import com.${sys}.biz.${module}.domain.${class};
import com.${sys}.biz.${module}.repository.${class}Repository;
import com.${sys}.biz.${module}.persistence.dao.${class}QueryDao;
import com.${sys}.biz.${module}.persistence.entity.${class}Po;

/**
 * 对象功能:${model.tabComment} Repository接口的实现类
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@Repository
public class ${class}RepositoryImpl extends AbstractRepository<String, ${class}Po,${class}> implements ${class}Repository{
	  
	@Resource
	private  ${class}QueryDao ${classVar}QueryDao;

	public ${class} newInstance() {
		${class}Po po = new ${class}Po();
		${class} ${classVar} = SpringHelper.getBean(${class}.class);
		${classVar}.setData(po);
		return ${classVar};
	}

	public ${class} newInstance(${class}Po po) {
		${class} ${classVar} = SpringHelper.getBean(${class}.class);
		${classVar}.setData(po);
		return ${classVar};
	} 
	
	@Override
	protected IQueryDao<String, ${class}Po> getQueryDao() {
		return ${classVar}QueryDao;
	}
	
}
