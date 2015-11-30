package com.eiko.gui.main;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.gui.tab.TabHolder;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFrame extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Tutoring Appointment Manager");
		Group group = new Group();
		Scene scene = new Scene(group);
		TabHolder tabs = new TabHolder(new TutorDBConnector());
		
		group.getChildren().add(tabs);
		
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
