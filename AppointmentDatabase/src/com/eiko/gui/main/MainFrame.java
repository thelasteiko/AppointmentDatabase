package com.eiko.gui.main;

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
		
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
