package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;

import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class TabUsage extends Tab {
	
	private StackPane stack;
	private GridPane pane_usage;
	
	public TabUsage(TutorDBConnector c) {
		this.setText("Usage");
		pane_usage = new GridPane();
	}

}
