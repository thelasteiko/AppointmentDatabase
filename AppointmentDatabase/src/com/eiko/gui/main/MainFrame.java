package com.eiko.gui.main;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.gui.tab.TabHolder;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main GUI component and where the program starts.
 * @author Melinda Robertson
 * @version 20151201
 */
public class MainFrame extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Tutoring Appointment Manager");
		Group group = new Group();
		Scene scene = new Scene(group);
		//This should hold the bulk of the program.
		TabHolder tabs = new TabHolder(new TutorDBConnector());
		
		group.getChildren().add(tabs);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static boolean isNumeric(String s) {
		for(char c: s.toCharArray()) {
			if (!Character.isDefined(c)) return false;
		}
		return true;
	}

}
