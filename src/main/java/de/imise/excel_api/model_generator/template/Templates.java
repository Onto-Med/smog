package de.imise.excel_api.model_generator.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.imise.excel_api.model_generator.entity_specification.DataType;
import de.imise.excel_api.model_generator.entity_specification.DynamicTableSpec;
import de.imise.excel_api.model_generator.entity_specification.FieldSpec;
import de.imise.excel_api.model_generator.entity_specification.ObjectSpec;
import de.imise.excel_api.model_generator.entity_specification.TableSpec;
import de.imise.excel_api.model_generator.entity_specification.TreeSpec;
import de.imise.excel_api.model_generator.entity_specification.TreeTableSpec;

public class Templates {
	
	private Map<String, String> workbook = new HashMap<>();
	private Map<String, String> sheet = new HashMap<>();
	private Map<String, String> tabRec = new HashMap<>();
	private Map<String, String> freePosTabRec = new HashMap<>();
	private Map<String, String> treeNode = new HashMap<>();

	private Map<String, List<String>> workbookImp = new HashMap<>();
	private Map<String, List<String>> sheetImp = new HashMap<>();
	private Map<String, List<String>> tabRecImp = new HashMap<>();
	private Map<String, List<String>> freePosTabRecImp = new HashMap<>();
	private Map<String, List<String>> treeNodeImp = new HashMap<>();
	
	public Templates() {
		readTemplate(this.getClass().getClassLoader().getResourceAsStream("templates/_WORKBOOK_T.java"), workbook, workbookImp);
		readTemplate(this.getClass().getClassLoader().getResourceAsStream("templates/_SHEET_T.java"), sheet, sheetImp);
		readTemplate(this.getClass().getClassLoader().getResourceAsStream("templates/_TABLE_RECORD_T.java"), tabRec, tabRecImp);
		readTemplate(this.getClass().getClassLoader().getResourceAsStream("templates/_FREE_POSITION_TABLE_RECORD_T.java"), freePosTabRec, freePosTabRecImp);
		readTemplate(this.getClass().getClassLoader().getResourceAsStream("templates/_TREE_NODE_T.java"), treeNode, treeNodeImp);
	}
	
	public void writeWorkbook(File dir, String clsName, String packageName, Map<String, ObjectSpec> classes, List<ObjectSpec> objects) {
		Set<String> imps = new HashSet<>();
		StringBuffer sb = new StringBuffer(workbook.get("_HEADER").replace("_WORKBOOK_T", clsName)); 
		
		for (ObjectSpec cls : classes.values()) {
			imps.addAll(workbookImp.get("get_SHEET_TList"));
			String get_SHEET_TList = workbook.get("get_SHEET_TList").replace("_SHEET_T", cls.getJavaName()).replace("_CLASS_NAME", cls.getName());
			sb.append(get_SHEET_TList);
			writeSheet(dir, cls.getJavaName(), packageName, cls);
		}

		for (ObjectSpec obj : objects) {
			imps.addAll(workbookImp.get("get_SHEET_T"));
			String get_SHEET_T = workbook.get("get_SHEET_T").replace("_SHEET_T", obj.getJavaName()).replace("_SHEET_NAME", obj.getName());
			sb.append(get_SHEET_T);
			writeSheet(dir, obj.getJavaName(), packageName, obj);
		}
		
		write(getFileStringBuffer(packageName, imps, sb), dir, clsName);
	}
	
	private void writeSheet(File dir, String clsName, String packageName, ObjectSpec obj) {
		Set<String> imps = new HashSet<>();
		StringBuffer sb = new StringBuffer(sheet.get("_HEADER").replace("_SHEET_T", clsName));
		
		for (FieldSpec field : obj.getFields()) {
			if (field.hasListDataType()) {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(sheetImp.get("get_STRING_LIST_FIELD"));
						String get_STRING_LIST_FIELD = sheet.get("get_STRING_LIST_FIELD").replace("_STRING_LIST_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_STRING_LIST_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(sheetImp.get("get_INTEGER_LIST_FIELD"));
						String get_INTEGER_LIST_FIELD = sheet.get("get_INTEGER_LIST_FIELD").replace("_INTEGER_LIST_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_INTEGER_LIST_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(sheetImp.get("get_DOUBLE_LIST_FIELD"));
						String get_DOUBLE_LIST_FIELD = sheet.get("get_DOUBLE_LIST_FIELD").replace("_DOUBLE_LIST_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DOUBLE_LIST_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(sheetImp.get("get_DATE_LIST_FIELD"));
						String get_DATE_LIST_FIELD = sheet.get("get_DATE_LIST_FIELD").replace("_DATE_LIST_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DATE_LIST_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(sheetImp.get("get_BOOLEAN_LIST_FIELD"));
						String get_BOOLEAN_LIST_FIELD = sheet.get("get_BOOLEAN_LIST_FIELD").replace("_BOOLEAN_LIST_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_BOOLEAN_LIST_FIELD);
						break;
				}
			} else {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(sheetImp.get("get_STRING_FIELD"));
						String get_STRING_FIELD = sheet.get("get_STRING_FIELD").replace("_STRING_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "");
						sb.append(get_STRING_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(sheetImp.get("get_INTEGER_FIELD"));
						String get_INTEGER_FIELD = sheet.get("get_INTEGER_FIELD").replace("_INTEGER_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "");
						sb.append(get_INTEGER_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(sheetImp.get("get_DOUBLE_FIELD"));
						String get_DOUBLE_FIELD = sheet.get("get_DOUBLE_FIELD").replace("_DOUBLE_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "");
						sb.append(get_DOUBLE_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(sheetImp.get("get_DATE_FIELD"));
						String get_DATE_FIELD = sheet.get("get_DATE_FIELD").replace("_DATE_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "");
						sb.append(get_DATE_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(sheetImp.get("get_BOOLEAN_FIELD"));
						String get_BOOLEAN_FIELD = sheet.get("get_BOOLEAN_FIELD").replace("_BOOLEAN_FIELD", field.getJavaName()).replace("_ROW", field.getRow() + "").replace("_COL", field.getCol() + "");
						sb.append(get_BOOLEAN_FIELD);
						break;
				}
			}
		}
		
		for (TableSpec tab : obj.getTables()) {
			imps.addAll(sheetImp.get("get_TABLE"));
			String javaRecordClsName = clsName + tab.getJavaName() + "Record";
			String get_TABLE = sheet.get("get_TABLE").replace("_TABLE_RECORD_T", javaRecordClsName).replace("_TABLE", tab.getJavaName()).replace("_FIRST_ROW", tab.getFirstRow()+"").replace("_FIRST_COL", tab.getFirstCol()+"").replace("_LAST_COL", tab.getLastCol()+"");
			sb.append(get_TABLE);
			writeTableRecord(dir, javaRecordClsName, packageName, tab, obj);
		}

		for (TableSpec tab : obj.getFreePositionTables()) {
			imps.addAll(sheetImp.get("get_FREE_POSITION_TABLE"));
			String javaRecordClsName = clsName + tab.getJavaName() + "Record";
			String get_FREE_POSITION_TABLE = sheet.get("get_FREE_POSITION_TABLE").replace("_FREE_POSITION_TABLE_RECORD_T", javaRecordClsName).replace("_FREE_POSITION_TABLE", tab.getJavaName()).replace("_TABLE_NAME", tab.getName()).replace("_FIELDS_NUMBER", tab.getFieldsNumber()+"");
			sb.append(get_FREE_POSITION_TABLE);
			writeFreePositionTableRecord(dir, javaRecordClsName, packageName, tab);
		}

		for (DynamicTableSpec tab : obj.getDynamicTables()) {
			imps.addAll(sheetImp.get("get_DYNAMIC_TABLE"));
			String get_DYNAMIC_TABLE = sheet.get("get_DYNAMIC_TABLE").replace("_DYNAMIC_TABLE", tab.getJavaName()).replace("_ROW", tab.getRow()+"").replace("_COL", tab.getCol()+"");
			sb.append(get_DYNAMIC_TABLE);
		}

		for (TreeSpec tree : obj.getTrees()) {
			imps.addAll(sheetImp.get("get_TREE"));
			String get_TREE = sheet.get("get_TREE").replace("_TREE", tree.getJavaName()).replace("_ROW", tree.getRow()+"").replace("_COL", tree.getCol()+"");
			sb.append(get_TREE);
		}
		
		for (TreeTableSpec tt : obj.getTreeTables()) {
			imps.addAll(sheetImp.get("get_TREE_TABLE"));
			TreeSpec tree = tt.getTreeSpec();
			TableSpec tab = tt.getTableSpec();
			String javaRecordClsName = clsName + tree.getJavaName() + "Node";
			String get_TREE_TABLE = sheet.get("get_TREE_TABLE").replace("_TREE_NODE_T", javaRecordClsName).replace("_TREE_TABLE", tree.getJavaName()).replace("_ROW", tree.getRow()+"").replace("_COL", tree.getCol()+"").replace("_FIELDS_NUMBER", tab.getFieldsNumber()+"");
			sb.append(get_TREE_TABLE);
			writeTreeNode(dir, javaRecordClsName, packageName, tab);
		}
		
		write(getFileStringBuffer(packageName, imps, sb), dir, clsName);
	}
	
	private void writeTableRecord(File dir, String clsName, String packageName, TableSpec tab, ObjectSpec obj) {
		Set<String> imps = new HashSet<>();
		StringBuffer sb = new StringBuffer(tabRec.get("_HEADER").replace("_TABLE_RECORD_T", clsName));
		
		for (FieldSpec field : tab.getFields()) {
			if (field.hasListDataType()) {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(tabRecImp.get("get_STRING_LIST_FIELD"));
						String get_STRING_LIST_FIELD = tabRec.get("get_STRING_LIST_FIELD").replace("_STRING_LIST_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_STRING_LIST_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(tabRecImp.get("get_INTEGER_LIST_FIELD"));
						String get_INTEGER_LIST_FIELD = tabRec.get("get_INTEGER_LIST_FIELD").replace("_INTEGER_LIST_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_INTEGER_LIST_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(tabRecImp.get("get_DOUBLE_LIST_FIELD"));
						String get_DOUBLE_LIST_FIELD = tabRec.get("get_DOUBLE_LIST_FIELD").replace("_DOUBLE_LIST_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DOUBLE_LIST_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(tabRecImp.get("get_DATE_LIST_FIELD"));
						String get_DATE_LIST_FIELD = tabRec.get("get_DATE_LIST_FIELD").replace("_DATE_LIST_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DATE_LIST_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(tabRecImp.get("get_BOOLEAN_LIST_FIELD"));
						String get_BOOLEAN_LIST_FIELD = tabRec.get("get_BOOLEAN_LIST_FIELD").replace("_BOOLEAN_LIST_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_BOOLEAN_LIST_FIELD);
						break;
				}
			} else {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(tabRecImp.get("get_STRING_FIELD"));
						String get_STRING_FIELD = tabRec.get("get_STRING_FIELD").replace("_STRING_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "");
						sb.append(get_STRING_FIELD);

						if (tab.hasColRef() && tab.getColRefOwn().getCol() == field.getCol()) {
							TableSpec foreignTab = obj.getTable(tab.getColRefForeignTable());
							FieldSpec foreignCol = foreignTab.getField(tab.getColRefForeignColumn());

							String javaRecordClsName = obj.getJavaName() + foreignTab.getJavaName() + "Record";

							imps.addAll(tabRecImp.get("get_COL_REF"));
							String get_COL_REF = tabRec.get("get_COL_REF").replace("_FOREIGN_TAB_RECORD_T", javaRecordClsName)
									.replace("_FOREIGN_TAB", foreignTab.getJavaName()).replace("_FOREIGN_FIRST_ROW", foreignTab.getFirstRow() + "").replace("_FOREIGN_FIRST_COL", foreignTab.getFirstCol() + "").replace("_FOREIGN_LAST_COL", foreignTab.getLastCol() + "")
									.replace("_COL_REF_OWN", tab.getColRefOwn().getCol() + "")
									.replace("_COL_REF_FOREIGN", foreignCol.getCol() + "");
							sb.append(get_COL_REF);
						}

						break;
					case DataType.INTEGER:
						imps.addAll(tabRecImp.get("get_INTEGER_FIELD"));
						String get_INTEGER_FIELD = tabRec.get("get_INTEGER_FIELD").replace("_INTEGER_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "");
						sb.append(get_INTEGER_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(tabRecImp.get("get_DOUBLE_FIELD"));
						String get_DOUBLE_FIELD = tabRec.get("get_DOUBLE_FIELD").replace("_DOUBLE_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "");
						sb.append(get_DOUBLE_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(tabRecImp.get("get_DATE_FIELD"));
						String get_DATE_FIELD = tabRec.get("get_DATE_FIELD").replace("_DATE_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "");
						sb.append(get_DATE_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(tabRecImp.get("get_BOOLEAN_FIELD"));
						String get_BOOLEAN_FIELD = tabRec.get("get_BOOLEAN_FIELD").replace("_BOOLEAN_FIELD", field.getJavaName()).replace("_COL", field.getCol() + "");
						sb.append(get_BOOLEAN_FIELD);
						break;
				}
			}
		}
		
		imps.addAll(tabRecImp.get("getRecord"));
		StringBuilder fields = new StringBuilder();
		for (FieldSpec field : tab.getFields())
			fields.append("        record.put(\"").append(field.getName()).append("\", get").append(field.getJavaName()).append("());").append(System.lineSeparator());
		
		String getRecord = tabRec.get("getRecord").replace("//_PUT_FIELDS", fields.toString());
		sb.append(getRecord);
		
		write(getFileStringBuffer(packageName, imps, sb), dir, clsName);
	}

	private void writeFreePositionTableRecord(File dir, String clsName, String packageName, TableSpec tab) {
		Set<String> imps = new HashSet<>();
		StringBuffer sb = new StringBuffer(freePosTabRec.get("_HEADER").replace("_FREE_POSITION_TABLE_RECORD_T", clsName));
		
		for (FieldSpec field : tab.getFields()) {
			if (field.hasListDataType()) {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(freePosTabRecImp.get("get_STRING_LIST_FIELD"));
						String get_STRING_LIST_FIELD = freePosTabRec.get("get_STRING_LIST_FIELD").replace("_STRING_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_STRING_LIST_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(freePosTabRecImp.get("get_INTEGER_LIST_FIELD"));
						String get_INTEGER_LIST_FIELD = freePosTabRec.get("get_INTEGER_LIST_FIELD").replace("_INTEGER_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_INTEGER_LIST_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(freePosTabRecImp.get("get_DOUBLE_LIST_FIELD"));
						String get_DOUBLE_LIST_FIELD = freePosTabRec.get("get_DOUBLE_LIST_FIELD").replace("_DOUBLE_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DOUBLE_LIST_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(freePosTabRecImp.get("get_DATE_LIST_FIELD"));
						String get_DATE_LIST_FIELD = freePosTabRec.get("get_DATE_LIST_FIELD").replace("_DATE_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DATE_LIST_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(freePosTabRecImp.get("get_BOOLEAN_LIST_FIELD"));
						String get_BOOLEAN_LIST_FIELD = freePosTabRec.get("get_BOOLEAN_LIST_FIELD").replace("_BOOLEAN_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_BOOLEAN_LIST_FIELD);
						break;
				}
			} else {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(freePosTabRecImp.get("get_STRING_FIELD"));
						String get_STRING_FIELD = freePosTabRec.get("get_STRING_FIELD").replace("_STRING_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_STRING_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(freePosTabRecImp.get("get_INTEGER_FIELD"));
						String get_INTEGER_FIELD = freePosTabRec.get("get_INTEGER_FIELD").replace("_INTEGER_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_INTEGER_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(freePosTabRecImp.get("get_DOUBLE_FIELD"));
						String get_DOUBLE_FIELD = freePosTabRec.get("get_DOUBLE_FIELD").replace("_DOUBLE_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_DOUBLE_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(freePosTabRecImp.get("get_DATE_FIELD"));
						String get_DATE_FIELD = freePosTabRec.get("get_DATE_FIELD").replace("_DATE_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_DATE_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(freePosTabRecImp.get("get_BOOLEAN_FIELD"));
						String get_BOOLEAN_FIELD = freePosTabRec.get("get_BOOLEAN_FIELD").replace("_BOOLEAN_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_BOOLEAN_FIELD);
						break;
				}
			}
		}
		
		imps.addAll(freePosTabRecImp.get("getRecord"));
		StringBuilder fields = new StringBuilder();
		for (FieldSpec field : tab.getFields())
			fields.append("        record.put(\"").append(field.getName()).append("\", get").append(field.getJavaName()).append("());").append(System.lineSeparator());
		
		String getRecord = freePosTabRec.get("getRecord").replace("//_PUT_FIELDS", fields.toString());
		sb.append(getRecord);
		
		write(getFileStringBuffer(packageName, imps, sb), dir, clsName);
	}

	private void writeTreeNode(File dir, String clsName, String packageName, TableSpec tab) {
		Set<String> imps = new HashSet<>();
		StringBuffer sb = new StringBuffer(treeNode.get("_HEADER").replace("_TREE_NODE_T", clsName));
		
		for (FieldSpec field : tab.getFields()) {
			if (field.hasListDataType()) {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(treeNodeImp.get("get_STRING_LIST_FIELD"));
						String get_STRING_LIST_FIELD = treeNode.get("get_STRING_LIST_FIELD").replace("_STRING_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_STRING_LIST_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(treeNodeImp.get("get_INTEGER_LIST_FIELD"));
						String get_INTEGER_LIST_FIELD = treeNode.get("get_INTEGER_LIST_FIELD").replace("_INTEGER_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_INTEGER_LIST_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(treeNodeImp.get("get_DOUBLE_LIST_FIELD"));
						String get_DOUBLE_LIST_FIELD = treeNode.get("get_DOUBLE_LIST_FIELD").replace("_DOUBLE_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DOUBLE_LIST_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(treeNodeImp.get("get_DATE_LIST_FIELD"));
						String get_DATE_LIST_FIELD = treeNode.get("get_DATE_LIST_FIELD").replace("_DATE_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_DATE_LIST_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(treeNodeImp.get("get_BOOLEAN_LIST_FIELD"));
						String get_BOOLEAN_LIST_FIELD = treeNode.get("get_BOOLEAN_LIST_FIELD").replace("_BOOLEAN_LIST_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "").replace("_LIST_SEPARATOR", field.getListSeparator());
						sb.append(get_BOOLEAN_LIST_FIELD);
						break;
				}
			} else {
				switch (field.getDataType()) {
					case DataType.STRING:
						imps.addAll(treeNodeImp.get("get_STRING_FIELD"));
						String get_STRING_FIELD = treeNode.get("get_STRING_FIELD").replace("_STRING_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_STRING_FIELD);
						break;
					case DataType.INTEGER:
						imps.addAll(treeNodeImp.get("get_INTEGER_FIELD"));
						String get_INTEGER_FIELD = treeNode.get("get_INTEGER_FIELD").replace("_INTEGER_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_INTEGER_FIELD);
						break;
					case DataType.DOUBLE:
						imps.addAll(treeNodeImp.get("get_DOUBLE_FIELD"));
						String get_DOUBLE_FIELD = treeNode.get("get_DOUBLE_FIELD").replace("_DOUBLE_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_DOUBLE_FIELD);
						break;
					case DataType.DATE:
						imps.addAll(treeNodeImp.get("get_DATE_FIELD"));
						String get_DATE_FIELD = treeNode.get("get_DATE_FIELD").replace("_DATE_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_DATE_FIELD);
						break;
					case DataType.BOOLEAN:
						imps.addAll(treeNodeImp.get("get_BOOLEAN_FIELD"));
						String get_BOOLEAN_FIELD = treeNode.get("get_BOOLEAN_FIELD").replace("_BOOLEAN_FIELD", field.getJavaName()).replace("_COL_INDEX", field.getTabColIndex() + "");
						sb.append(get_BOOLEAN_FIELD);
						break;
				}
			}
		}
		
		imps.addAll(treeNodeImp.get("getRecord"));
		StringBuilder fields = new StringBuilder();
		for (FieldSpec field : tab.getFields())
			fields.append("        record.put(\"").append(field.getName()).append("\", get").append(field.getJavaName()).append("());").append(System.lineSeparator());
		
		String getRecord = treeNode.get("getRecord").replace("//_PUT_FIELDS", fields.toString());
		sb.append(getRecord);
		
		write(getFileStringBuffer(packageName, imps, sb), dir, clsName);
	}
	
	private String getName(String line) {
		return line.split(" *: *")[1];
	}

	private List<String> getImports(String line) {
		String[] ar = line.split(" *: *");
		if (ar.length > 2)
			return Arrays.asList(Arrays.copyOfRange(ar, 2, ar.length));
		else
			return new ArrayList<>();
	}
	
	private StringBuffer getFileStringBuffer(String packageName, Set<String> imps, StringBuffer code) {
		StringBuffer sb = new StringBuffer("package " + packageName + ";").append(System.lineSeparator()).append(System.lineSeparator());
		for (String imp : imps)
			sb.append(imp).append(System.lineSeparator());

		return sb.append(code).append("}");
	}

	private void readTemplate(InputStream file, Map<String, String> templates, Map<String, List<String>> imports) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			StringBuffer temp = new StringBuffer();
			String line;
			String name = null;
			List<String> imps = null;
			while ((line = reader.readLine()) != null) {
				if (line.contains("_START:")) {
					temp = new StringBuffer();
					name = getName(line.trim());
					imps = getImports(line.trim());
				} else if (line.contains("_END:")) {
					templates.put(name, temp.append(System.lineSeparator()).toString());
					imports.put(name, imps);
				} else
					temp.append(line).append(System.lineSeparator());
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void write(StringBuffer sb, File dir, String clsName) {
		try {
			FileWriter writer = new FileWriter(new File(dir, clsName + ".java"));
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Templates [workbook=" + workbook + ", sheet=" + sheet + ", tabRec=" + tabRec + ", freePosTabRec="
				+ freePosTabRec + "]";
	}
	
	
}
