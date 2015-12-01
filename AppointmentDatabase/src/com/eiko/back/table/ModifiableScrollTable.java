package com.eiko.back.table;

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

	public ModifiableScrollTable(){table = null;};
	public ModifiableScrollTable(TableView<CellValue> content) {
		super(content);
		this.table = content;
	}
	
	public CellValue getItem() {
		return table.getSelectionModel().getSelectedItem();
	}
	
	public void setTable(TableView<CellValue> content) {
		this.table = content;
		this.setContent(content);
	}

}
