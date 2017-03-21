<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign module=model.variables.module>
<#assign sys=model.variables.sys>
<#assign baseClass=model.variables.baseClass>
package com.${sys}.biz.${module}.test;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;

import com.pai.base.api.service.IdGenerator;
import com.pai.base.core.test.BaseTestCase;

/**
 * 测试基类，。
 * 其下的测试类均继承该子类
 * @author ${vars.developer}
 *
 */
@ContextConfiguration({"classpath:conf/${sys}-${module}-test.xml"})
public class ${baseClass}BaseTest extends BaseTestCase{
	
	@Resource
    protected IdGenerator idGenerator;
}
