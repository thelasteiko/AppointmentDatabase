package com.eiko.back.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Creates a table on demand with the indicated column names
 * using the result set from a database query.
 * @author Melinda Robertson
 * @version 20151201
 */
public class TableMaker {
	
	/**
	 * Creates a new TableView based on the columns and ResultSet.
	 * Autosizes column width based on content.
	 * @param keys are the column names.
	 * @param r is the result of a database query.
	 * @return a TableView with CellValue as the data type.
	 * @throws SQLException if there is an error querying the database.
	 */
	public static TableView<CellValue> gimmeTable(String[] keys, ResultSet r) throws SQLException {
		ObservableList<CellValue> data = FXCollections.observableArrayList();
		TableView<CellValue> table = new TableView<CellValue>(data);
		int[] maxSize = new int[keys.length];
		if (r == null) {
			System.out.println("No result.");
			return table;
		}
		while(r.next()) {
			//System.out.println(r.getString(1));
			CellValue cv = new CellValue();
			for(int i = 0; i < keys.length; i++) {
				String s = r.getString(i+1);
				cv.set(keys[i], s);
				if (s.length() > maxSize[i]) {
					maxSize[i] = s.length();
				}
				
			}
			data.add(cv);
		}
		
		for(int i = 0; i < keys.length; i++) {
			TableColumn<CellValue,String> tv = new TableColumn<CellValue,String>(keys[i]);
			tv.setCellValueFactory(new PropertyValueFactory<CellValue,String>(keys[i]));
			table.getColumns().add(tv);
			tv.setMinWidth(maxSize[i]*10);
		}
		
		return table;
	}
	
	/**
	 * Creates an observable list to be used in a TableView.
	 * @param keys are the columns names.
	 * @param r is the result of a database query.
	 * @return an observable list of CellValues.
	 * @throws SQLException if there is an error querying the database.
	 */
	public static ObservableList<CellValue> buildList(String[] keys, ResultSet r) throws SQLException {
		ObservableList<CellValue> array = FXCollections.observableArrayList();
		int maxCol = 0;
		int maxSize = 0;
		if (r == null) {
			System.out.println("No result.");
			return array;
		}
		while(r.next()) {
			//System.out.println(r.getString(1));
			CellValue cv = new CellValue();
			for(int i = 0; i < keys.length; i++) {
				String s = r.getString(i+1);
				cv.set(keys[i], s);
				if (s.length() > maxSize) {
					maxSize = s.length();
					maxCol = i;
				}
				
			}
			array.add(cv);
		}
		return array;
	}

	/**
	 * Creates a table using the column names provided. Does not
	 * auto size the columns to the data.
	 * @param columns are the column names.
	 * @param data is the data to be inserted into the table.
	 * @return a table view.
	 */
	public static <S, T> TableView<S> buildTable(String[] columns, ObservableList<S> data) {
		TableView<S> table = new TableView<S>(data);
		for(int i = 0; i < columns.length; i++) {
			TableColumn<S,T> tv = new TableColumn<S,T>(columns[i]);
			tv.setCellValueFactory(new PropertyValueFactory<S, T>(columns[i]));
			table.getColumns().add(tv);
		}
		
		return table;
	}
}
