package com.eiko.gui.main;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.gui.tab.TabHolder;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This is the main GUI component and where the program starts.
 * @author Melinda Robertson
 * @version 20151201
 */
public class MainFrame extends Application {
	
	/**
	 * The current size of the screen used for positioning this frame.
	 */
	private static final Rectangle2D screensize = Screen.getPrimary().getBounds();
	/**
	 * Width of this frame. 
	 */
	public static double WIDTH = 450;
	/**
	 * Height of this frame.
	 */
	public static double HEIGHT = 500;
	
	/**
	 * Sets the horizontal position of this frame in the screen.
	 */
	public static final double x = ((screensize.getWidth()-WIDTH) / 2);
	/**
	 * Sets the vertical position of this frame in the screen.
	 */
	public static final double y = ((screensize.getHeight()-HEIGHT) / 2);
	/**
	 * Creates and displays the application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Tutoring Appointment Manager");
		Group group = new Group();
		Scene scene = new Scene(group);
		//This should hold the bulk of the program.
		TabHolder tabs = new TabHolder(new TutorDBConnector());
		tabs.setMinSize(WIDTH,HEIGHT);
		group.getChildren().add(tabs);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Main method.
	 * @param args are the args.
	 */
	public static void main(String[] args) {
		launch(args);
	}


}
