package com.eiko.gui.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.connect.TutorDBConnector;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Tester extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		TutorDBConnector c = new TutorDBConnector();
		String query = "select_any";
		String[] param = {
				"student", "LastName", "'Robertson'"
		};
		ResultSet r = c.query(query, param); 
		
		try {
			while(r.next()) {
				for (int i = 1; i <= 4; i++) {
					System.out.println(r.getString(i));
				}
			}
			r.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
