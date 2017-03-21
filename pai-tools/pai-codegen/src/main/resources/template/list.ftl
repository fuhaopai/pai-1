<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign classVar=class?uncap_first>
<#assign sys=model.variables.sys>
<#assign module=model.variables.module>
<#assign commonList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${comment }管理</title>
	<<#noparse>#</#noparse>include "/WEB-INF/view/common/jquery.ftl">
	<<#noparse>#</#noparse>include "/WEB-INF/view/common/ligerUI.ftl">		 
	<script type="text/javascript" src="<#noparse>${</#noparse>CtxPath}/scripts/pai/common.js" ></script>					    					    
    <script type="text/javascript">
    	var grid = null;
        var searchForm = null;
        $(function ()
        {
        	searchForm = $("#form1").ligerForm({
				inputWidth : 190,labelWidth : 0,space : 1,rightToken :'',
				fields : [
		<#list model.commonList as col>
			<#assign colName=func.convertUnderLine(col.columnName)>                          
			<#if (col.isPK) >				
			<#else>
				{ display: '&nbsp;${col.getComment()}', name: 'Q__S__EQ__${colName}', align: 'left', width: 80, minWidth: 60 },
			</#if>
		</#list>			
			{ display: 'aliasSortName', name: 'aliasSortName',type:'hidden'},	
				          	{display: "<input type='button' value='查询' class='l-button' onClick='javascript:fnListSearch();' style='width:50px;'>", 
							name: "searchButton", newline: false, width:0.01}
						  ]
			});        
        	
           grid = $("#maingrid").ligerGrid({
                height:'100%',
                
                onChangeSort: function (sortname, sortorder) {
	    			  $("input[name='aliasSortName']").val(sortname);
		              fnListSearch();
		              return;
	             } ,
	             
                columns: [
		<#list model.commonList as col>
			<#assign colName=func.convertUnderLine(col.columnName)>                          
			<#if (col.isPK) >
				{ display: '${col.getComment()}', name: '${colName}',hide:true}<#if col_has_next>,</#if>
			<#else>
				{ display: '${col.getComment()}', name: '${colName}', align: 'left', width: 80, minWidth: 60 }<#if col_has_next>,</#if>
			</#if>
		</#list>			
                ], url:'<#noparse>${</#noparse>CtxPath}/storeadmin/${sys}/${module}/${classVar}/listData.do', pageSize:30 ,rownumbers:true,pagesizeParmName:'pageSize',
                onReload:setDataToGrid,
                toolbar: { items: [
                { id:'add',text: '增加', click: add, icon: 'add' },
                { line: true },
                { id:'modify',text: '修改', click: edit, icon: 'modify' },
                { line: true },
                { id:'delete',text: '删除', click: deleteRow, img: '<#noparse>${</#noparse>CtxPath}/scripts/ligerUI/skins/icons/delete.gif' },
                { line: true },
                { id:'modify',text: '刷新', click: refresh, icon: 'refresh' }                
                ]
                }
            });             

            $("#pageloading").hide();
        });
        
        var __ctxPath = "<#noparse>${CtxPath}</#noparse>";
        function fnListSearch(){
			if(grid!=null){	            
				grid.setOptions({newPage:1});
				setDataToGrid();				
				grid.loadData();
			}
		}
		function setDataToGrid(){		
			var data = searchForm.getData();
			if(grid!=null){
				grid.set("parms",[]);
				for(var param in data){					
					$("input[name='"+param+"']").each(function() {
						var id = $(this).attr("id");						
						var paramValue = $("#" + id).val();					
                    	grid.get("parms").push({ name: param, value: paramValue });
     				});					
				}
			}			
		}
    </script>
    <script type="text/javascript" src="<#noparse>${CtxPath}</#noparse>/scripts/pai/${sys}/${module}/${classVar}.js" ></script>
</head>
<body style="overflow-x:hidden; padding:2px;">
	<div class="l-loading" style="display:block" id="pageloading"></div>
 		<a class="l-button" style="width:120px;float:left; margin-left:10px; display:none;" onclick="deleteRow()">删除选择的行</a> 
 		<div class="l-clear"></div>
 		<form id="form1"></form>
    	<div id="maingrid"></div>
   		<div style="display:none;">
	</div> 
</body>
</html>