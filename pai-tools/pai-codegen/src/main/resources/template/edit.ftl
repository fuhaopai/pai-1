<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign classVar=model.variables.classVar>
<#assign colList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title></title>
	<#noparse><#</#noparse>include "/common/jquery.ftl">
	<#noparse><#</#noparse>include "/common/ligerUI.ftl">		 			
	<link href="<#noparse>${</#noparse>CtxPath}/scripts/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	<link href="<#noparse>${</#noparse>CtxPath}/styles/platform/form.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<#noparse>${</#noparse>CtxPath}/scripts/pai/common.js" ></script>		    	            	    	
    <script type="text/javascript">  
	$(function(){		
		bindFormValidation("${classVar}EditForm","<#noparse>${</#noparse>CtxPath}/storeadmin/${sys}/${module}/${classVar}/save.do");								
		$("#${classVar}EditForm").ligerForm();           
	});
	var __ctxPath = "<#noparse>${CtxPath}</#noparse>";	   
    </script>
	<script type="text/javascript" src="<#noparse>${CtxPath}</#noparse>/scripts/pai/${sys}/${module}/${classVar}.js" ></script>
</head>

<body style="padding:10px">	
    <form id="${classVar}EditForm" name="${classVar}EditForm" method="post">
    <<#noparse>#</#noparse>if ${classVar}Po.id?exists>
    	<input type="hidden" name="pk" value="<#noparse>${</#noparse>${classVar}Po.id}" ltype="text"/>
    </<#noparse>#</#noparse>if>    	
    <<#noparse>#</#noparse>if ${classVar}Po.createTime?exists>
    	<input type="hidden" name="createTime" value="<#noparse>${</#noparse>${classVar}Po.createTime?string('yyyy-MM-dd HH:mm:ss')}" ltype="text"/>
    </<#noparse>#</#noparse>if>
    <<#noparse>#</#noparse>if ${classVar}Po.createBy?exists>
    	<input type="hidden" name="createBy" value="<#noparse>${</#noparse>${classVar}Po.createBy}" ltype="text"/>
    </<#noparse>#</#noparse>if>
    	<div>&nbsp;</div>
        <table cellpadding="0" cellspacing="0" class="l-table-edit">
		<#list colList as col>
		<#assign colName=func.convertUnderLine(col.columnName)>		       
<#assign needValidate="N"/><#if col.isNotNull || col.colType="Integer" || col.colType="Float" || col.colType="java.util.Date"><#assign needValidate="Y"/></#if>		
            <tr>
                <td align="right" class="l-table-edit-td">${col.getComment()}:</td>
                <td align="left" class="l-table-edit-td">
                	<input name="${colName}" type="text" id="${colName}" <#if col.colType="java.util.Date">class="wdateTime" onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"</#if> value="<#noparse>${</#noparse>${classVar}Po.${colName}}" ltype="text" <#if needValidate=="Y">validate='{<#if col.isNotNull>required:true</#if><#if col.colType="Integer"><#if col.isNotNull>,</#if>digits:true</#if><#if col.colType="Float"><#if col.isNotNull>,</#if>number:true</#if><#if col.colType="java.util.Date"><#if col.isNotNull>,</#if>date:true</#if>}'</#if>/>
                </td>
                <td align="left"></td>
            </tr>    
		</#list>           
        </table>
 		<br />
		<input type="submit" value="提交" id="Button1" class="l-button l-button-submit" /> 
    </form>   
</body>
</html>
