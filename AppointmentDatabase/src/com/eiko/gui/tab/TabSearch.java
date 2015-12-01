package com.eiko.gui.tab;

import java.sql.SQLException;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.back.table.ModifiableScrollTable;
import com.eiko.back.table.TableMaker;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.main.MainFrame;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Runs general database queries on three tables. Opens panels with more detailed information on request.
 * @author Melinda Robertson
 * @version 20151201
 */
public class TabSearch extends Tab {
	
	public final static String[] student_keys = {
			"StudentID", "FirstName", "LastName", "StudentStatus"
	};
	
	public final static String[] class_keys = {
			"ClassNumber", "ClassName"
	};
	
	public final static String[] visit_keys = {
			"StudentID", "ClassNumber", "StartDate", "StartTime", "Duration"
	};
	
	private StackPane stack;
	
	private TextField field_search;
	private ToggleGroup rb_group;
	private ModifiableScrollTable sc_pane;

	private TutorDBConnector c;
	
	public TabSearch(TutorDBConnector c) {
		this.setText("Search");
		this.c = c;
		initQueries();
		stack = new StackPane();
		buildTopBox();
	}
	
	/**
	 * Queries support searching for:
	 * 	Student: name, id
	 *  Class: name, number
	 *  Appt: id, date, time, class
	 * Dates and times are in ranges.
	 * @param c is the connector utility for the database.
	 */
	private void initQueries() {
		c.add("select_student_byid", "SELECT * FROM student WHERE StudentID = ?");
		c.add("select_student_byname", "SELECT * FROM student WHERE "
				+ "LCASE(student.LastName) LIKE LCASE(CONCAT('%',?,'%')) "
				+ "OR LCASE(student.FirstName) LIKE LCASE(CONCAT('%',?,'%'))");
		
		c.add("select_class_byname", "SELECT * FROM class_name WHERE "
				+ "LCASE(ClassName) LIKE LCASE(CONCAT('%',?,'%'))");
		c.add("select_class_byid", "SELECT * FROM class_name WHERE "
				+ "ClassNumber = ?");
		
		c.add("select_visit_byid", "SELECT * FROM visit WHERE StudentID = ?"
				+ " OR ClassNumber = ?");
		c.add("select_visit_bydate", "SELECT * FROM visit WHERE StartDate = ?");
		c.add("select_visit_bytimerange", "SELECT * FROM visit WHERE StartTime BETWEEN ? AND ?");
	}
	
	/**
	 * Creates the top most panel in the stack pane. This is the first panel
	 * the user sees.
	 */
	private void buildTopBox() {
		GridPane pane_search = new GridPane();
		sc_pane = new ModifiableScrollTable();
		//constraints: col,row,colspan,rowspan
		field_search = new TextField();
		GridPane.setConstraints(field_search, 0, 0, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		
		//----------------SEARCH BUTTON-------------------------------
		Button btn_search = new Button();
		GridPane.setConstraints(btn_search, 1, 0, 1, 1,
				HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		btn_search.setOnAction((event)->{
			RadioButton rb = (RadioButton) rb_group.getSelectedToggle();
			try {
				parseRequest(rb.getText());
			} catch (SQLException e) {
				new ErrorHandle("Database request error in search tab: " + e.getSQLState());
			}			
		});
		
		//--------------------RADIO BUTTONS----------------------------
		rb_group = new ToggleGroup();
		RadioButton rb_student = new RadioButton("Student");
		GridPane.setConstraints(rb_student, 0, 1, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		rb_student.setToggleGroup(rb_group);
		RadioButton rb_class = new RadioButton("Class");
		GridPane.setConstraints(rb_class, 0, 2, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		rb_class.setToggleGroup(rb_group);
		RadioButton rb_appt = new RadioButton("Appointment");
		GridPane.setConstraints(rb_appt, 0, 3, 1, 1,
				HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(0));
		rb_appt.setToggleGroup(rb_group);
		rb_group.selectToggle(rb_student);
		
		//-----------------OPEN SELECTED RECORD---------------------
		Button open = new Button("Open");
		open.setOnAction((event)->{			
			
		});
		
		//pane_search.getChildren().add(lbl_search);
		pane_search.getChildren().add(field_search);
		pane_search.getChildren().add(btn_search);
		stack.getChildren().add(pane_search);
	}
	
	/**
	 * Reads the user's input and determines what query to run.
	 * @param toggle is the radio button that is selected.
	 * @return a query name.
	 */
	private void parseRequest(String toggle) throws SQLException {
		String text = field_search.getText();
		String queryname = "";
		String[] keys = {};
		switch(toggle) {
		case "Student":
			keys = student_keys;
			if(MainFrame.isNumeric(text)) {
				queryname =  "select_student_byid";
			} else queryname = "select_student_byname";
		case "Class":
			keys = class_keys;
			if(MainFrame.isNumeric(text)) {
				queryname = "select_class_byid";
			} else queryname = "select_class_byname";
		case "Appointment":
			keys = visit_keys;
			if(MainFrame.isNumeric(text)) {
				queryname = "select_visit_byid";
			} else if (text.contains("/")) {
				queryname = "select_visit_bydate";
			}
		default:
			keys = student_keys;
			queryname = "select_student_byname";
		}
		sc_pane.setTable(TableMaker.gimmeTable(keys, c.query(queryname, text)));
	}
	
}
