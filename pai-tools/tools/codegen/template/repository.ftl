<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
package com.${sys}.biz.${module}.repository;

import com.pai.biz.frame.repository.IRepository;
import com.${sys}.biz.${module}.domain.${class};
import com.${sys}.biz.${module}.persistence.entity.${class}Po;
/**
 * 对象功能:${model.tabComment} Repository接口
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 命名规范：查list集合以findXx做前缀,单个po实体用getXx做前缀,数量countXx,条件查询加ByXx后缀,如getXxByName
 */
public interface ${class}Repository extends IRepository<String, ${class}Po,${class}>{
	  
	 
}
