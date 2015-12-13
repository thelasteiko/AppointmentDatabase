package com.eiko.back.table;

import java.util.NoSuchElementException;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;

/**
 * The intent of this class is to make a ScrollPane that contains
 * a TableView that can change sporatically and the information
 * can still be retrieved and used.
 * @author Melinda Robertson
 * @version 20151211
 */
public class ModifiableScrollTable extends ScrollPane {
	/**
	 * The table for this view.
	 */
	private TableView<CellValue> table;

	/**
	 * Creates a new scrollable panel.
	 */
	public ModifiableScrollTable(){
		table = null;
	};
	/**
	 * Creates a new scrollable panel with table content.
	 * @param content is the table to add to this panel.
	 */
	public ModifiableScrollTable(TableView<CellValue> content) {
		super(content);
		this.table = content;
	}
	/**
	 * Gets the selected item as a CellValue.
	 * @return the selected item.
	 * @throws NoSuchElementException if there is no table or the selected
	 * index is less than zero.
	 */
	public CellValue getItem() {
		if (table == null || table.getSelectionModel().getSelectedIndex() < 0)
			return null;
		return table.getSelectionModel().getSelectedItem();
	}
	/**
	 * Determines if there is a table and if it has items.
	 * @return true if the table exists and there is at least one item as content,
	 * 			false otherwise.
	 */
	public boolean hasItems() {
		if (table == null) return false;
		if (table.getItems().size() == 0) return false;
		return true;
	}
	/**
	 * Returns the value of the table's selection model item property.
	 * Basically lets a listener know if an item has been selected.
	 * @return true if an item is selected, false otherwise; as an ObservableValue.
	 */
	public ObservableValue<? extends Boolean> isNull() {
		if (table == null) return null;
		return table.getSelectionModel().selectedItemProperty().isNull();
	}
	/**
	 * Sets the content of the scroll pane which must be a table.
	 * Also resizes this component to accommodate the table.
	 * @param content is the content to add to the pane.
	 */
	public void setTable(TableView<CellValue> content) {
		this.table = content;
		this.setContent(content);
		int l = (table.getItems().size() + 1) * 40;
		this.setMinHeight(l);
		this.setMaxHeight(l);
		this.setPrefHeight(l);
	}

}
