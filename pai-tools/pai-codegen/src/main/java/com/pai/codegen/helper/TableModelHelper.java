package com.pai.codegen.helper;

import com.pai.codegen.db.IDbHelper;
import com.pai.codegen.exception.GeneralException;
import com.pai.codegen.model.config.ConfigModel;
import com.pai.codegen.model.gen.GenSubTable;
import com.pai.codegen.model.gen.GenTable;
import com.pai.codegen.model.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableModelHelper
{
  public static List<TableModel> getTableModelList(IDbHelper dbHelper, ConfigModel configModel)
    throws GeneralException
  {
    List<GenTable> tables = configModel.getTables();
    List<TableModel> tableModels = new ArrayList<TableModel>();
    for (GenTable table : tables) {
      String tbName = table.getTableName();

      TableModel tableModel = dbHelper.build(tbName);

      tableModel.setVariables(table.getVariable());
      tableModel.setSub(false);

      List<GenSubTable> subtables = table.getSubtable();
      for (GenSubTable sb : subtables) {
        String tableName = sb.getTableName();
        String foreignKey = sb.getForeignKey();
        Map variables = sb.getVars();
        TableModel subTable = dbHelper.build(tableName);
        subTable.setVariables(variables);
        subTable.setSub(true);
        subTable.setForeignKey(foreignKey);
        tableModel.getSubTableList().add(subTable);
        tableModels.add(subTable);
      }
      tableModels.add(tableModel);
    }
    return tableModels;
  }
}