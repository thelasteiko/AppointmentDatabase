package com.eiko.back.table;

import java.util.NoSuchElementException;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;

/**
 * The intent of this class is to make a ScrollPane that contains
 * a TableView that can change sporatically and the information
 * can still be retrieved and used.
 * @author Melinda Robertson
 * @version 20151201
 */
public class ModifiableScrollTable extends ScrollPane {
	
	private TableView<CellValue> table;

	public ModifiableScrollTable(){
		table = null;
	};
	public ModifiableScrollTable(TableView<CellValue> content) {
		super(content);
		this.table = content;
	}
	
	public CellValue getItem() throws NoSuchElementException {
		if (table == null || table.getSelectionModel().getSelectedIndex() < 0)
			throw new NoSuchElementException();
		return table.getSelectionModel().getSelectedItem();
	}
	
	public void setTable(TableView<CellValue> content) {
		this.table = content;
		this.setContent(content);
		int l = (table.getItems().size() + 1) * 40;
		//System.out.println(l);
		this.setMinHeight(l);
		this.setMaxHeight(l);
		this.setPrefHeight(l);
		//System.out.println("h: " + table.getPrefHeight() + "\tw: " + table.getPrefWidth());
		//System.out.println("h: " + getPrefHeight() + "\tw: " + getPrefWidth());
	}

}
