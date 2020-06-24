package com.tangdao.core.mybatis.data.privilege.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;

/**
 * SqlMetadata
 * 
 * @ClassName:
 * @author: Naughty Guo
 * @date: 2016年7月13日
 */
public class SqlMetadata {

	private Table table;
//	private List<Table> tables;
	private Delete delete;
	private Update update;
	private List<Join> joins;
	private PlainSelect plainSelect;
	private Set<String> legalObjects;// original SQL legal objects.
	private Map<String, List<String>> legalJoins;// original SQL legal joins.
	private Set<String> allLegalJoins;// all legal joins, contains annotation joins.

	public SqlMetadata() {
	}

	public SqlMetadata(Table table, PlainSelect plainSelect) {
		this.table = table;
		this.plainSelect = plainSelect;
		this.joins = plainSelect.getJoins();
	}
	
	public SqlMetadata(Delete delete) {
		this.delete = delete;
		this.table = delete.getTable();
		this.joins = delete.getJoins();
	}
	
	public SqlMetadata(Update update) {
		this.update = update;
		this.table = update.getTable();
		this.joins = update.getJoins();
	}

//	public List<Table> getTables() {
//		return tables;
//	}
//
//	public void setTables(List<Table> tables) {
//		this.tables = tables;
//	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Delete getDelete() {
		return delete;
	}

	public void setDelete(Delete delete) {
		this.delete = delete;
	}

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public PlainSelect getPlainSelect() {
		return plainSelect;
	}

	public void setPlainSelect(PlainSelect plainSelect) {
		this.plainSelect = plainSelect;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	public Set<String> getLegalObjects() {
		return legalObjects;
	}

	public void setLegalObjects(Set<String> legalObjects) {
		this.legalObjects = legalObjects;
	}

	public Map<String, List<String>> getLegalJoins() {
		return legalJoins;
	}

	public void setLegalJoins(Map<String, List<String>> legalJoins) {
		this.legalJoins = legalJoins;
	}

	public Set<String> getAllLegalJoins() {
		return allLegalJoins;
	}

	public void setAllLegalJoins(Set<String> allLegalJoins) {
		this.allLegalJoins = allLegalJoins;
	}
}
