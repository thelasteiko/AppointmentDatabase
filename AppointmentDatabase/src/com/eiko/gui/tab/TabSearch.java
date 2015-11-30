package com.eiko.gui.tab;

import java.sql.ResultSet;

import com.eiko.back.connect.TutorDBConnector;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class TabSearch extends Tab {
	
	private StackPane stack;
	private GridPane pane_search;
	
	private TextField field_search;
	private ToggleGroup rb_group;
	private TableView table;

	public TabSearch(TutorDBConnector c) {
		this.setText("Search");
		
		
		stack.getChildren().add(pane_search);
	}
	
	private void buildTopPane() {
		pane_search = new GridPane();
		//constraints: col,row,colspan,rowspan
		Label lbl_search = new Label("Enter Search Criteria");
		GridPane.setConstraints(lbl_search, 0, 0, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		field_search = new TextField();
		GridPane.setConstraints(field_search, 0, 1, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		
		Button btn_search = new Button();
		GridPane.setConstraints(btn_search, 1, 1, 1, 1,
				HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		btn_search.setOnAction((event)->{
			//TODO pressing the button gets the selected
			//radio button then requests that table set
			//using the given parameters
			RadioButton rb = (RadioButton) rb_group.getSelectedToggle();
			switch (rb.getText()) {
			case "Student":
				
				break;
			case "Class":
				break;
			case "Appointment":
				break;
			default:
				System.out.println(rb.getText());
			}
			
		});
		
		rb_group = new ToggleGroup();
		RadioButton rb_student = new RadioButton("Student");
		GridPane.setConstraints(rb_student, 0, 0, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		rb_student.setToggleGroup(rb_group);
		RadioButton rb_class = new RadioButton("Class");
		rb_class.setToggleGroup(rb_group);
		RadioButton rb_appt = new RadioButton("Appointment");
		rb_appt.setToggleGroup(rb_group);
		
		pane_search.getChildren().add(lbl_search);
		pane_search.getChildren().add(field_search);
		pane_search.getChildren().add(btn_search);
	}
	
	private void parseResult(ResultSet r) {
		
	}
}
