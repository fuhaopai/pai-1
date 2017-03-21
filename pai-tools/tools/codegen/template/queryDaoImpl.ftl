<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
package com.${sys}.biz.${module}.persistence.dao.impl;

<#if sub?exists && sub>
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
</#if>
import org.springframework.stereotype.Repository;

import com.pai.base.db.mybatis.dao.MyBatisQueryDaoImpl;
import com.${sys}.biz.${module}.persistence.dao.${class}QueryDao;
import com.${sys}.biz.${module}.persistence.entity.${class}Po;

/**
 * 对象功能:${model.tabComment} QueryDao接口的实现
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@SuppressWarnings("serial")
@Repository
public class ${class}QueryDaoImpl extends MyBatisQueryDaoImpl<String, ${class}Po> implements ${class}QueryDao{

    @Override
    public String getNamespace() {
        return ${class}Po.class.getName();
    }
	
}
