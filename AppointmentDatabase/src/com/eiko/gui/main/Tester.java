package com.eiko.gui.main;

import javafx.application.Application;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Tester extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}
	
	private TableView buildTable(String[] columns) {
		TableView table = new TableView();
		
		for(int i = 0; i < columns.length; i++) {
			table.getColumns().add(new TableColumn(columns[i]));
		}
		
		return table;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
