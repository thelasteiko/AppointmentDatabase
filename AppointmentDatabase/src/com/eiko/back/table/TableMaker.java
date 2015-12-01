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
	 * @param keys are the column names.
	 * @param r is the result of a database query.
	 * @return a TableView with CellValue as the data type.
	 * @throws SQLException if there is an error querying the database.
	 */
	public static TableView<CellValue> gimmeTable(String[] keys, ResultSet r) throws SQLException {
		return buildTable(keys, buildList(keys,r));
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
		while(r.next()) {
			CellValue cv = new CellValue();
			for(int i = 0; i < keys.length; i++) {
				cv.set(keys[i], r.getString(i)+1);
			}
			array.add(cv);
		}
		return array;
	}

	/**
	 * Creates a table using the column names provided.
	 * @param columns are the column names.
	 * @param data is the data to be inserted into the table.
	 * @return
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
