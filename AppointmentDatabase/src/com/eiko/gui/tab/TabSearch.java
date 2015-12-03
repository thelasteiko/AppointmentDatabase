package com.eiko.gui.tab;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.back.table.CellValue;
import com.eiko.back.table.ModifiableScrollTable;
import com.eiko.back.table.TableMaker;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.main.MainFrame;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Runs general database queries on three tables. Opens panels with more detailed information on request.
 * @author Melinda Robertson
 * @version 20151201
 */
public class TabSearch extends Tab {
	
	public final static String[] student_keys = {"StudentID", "FirstName", "LastName", "StudentStatus"};
	public final static String[] class_keys = {"ClassNumber", "ClassName"};
	public final static String[] visit_keys = {"StudentID", "ClassNumber", "StartDate", "StartTime", "Duration"};
	
	private StackPane stack;
	private TextField field_search;
	private ToggleGroup rb_group;
	private String currentsearch = "Student";
	private ModifiableScrollTable sc_pane;

	protected TutorDBConnector c;
	
	public TabSearch(TutorDBConnector c) {
		this.setText("Search");
		this.c = c;
		initQueries();
		stack = new StackPane();
		stack.setMinSize(500, 500);
		buildTopBox();
		this.setContent(stack);
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
				+ "LCASE(student.LastName) LIKE LCASE(\"%?%\") "
				+ "OR LCASE(student.FirstName) LIKE LCASE(\"%?%\")");
		
		c.add("select_class_byname", "SELECT * FROM class_name WHERE "
				+ "LCASE(ClassName) LIKE LCASE(\"%?%\")");
		c.add("select_class_byid", "SELECT * FROM class_name WHERE "
				+ "ClassNumber = ?");
		
		c.add("select_visit_byid", "SELECT * FROM visit WHERE StudentID = ?"
				+ " OR ClassNumber = ?");
		c.add("select_visit_bydate", "SELECT * FROM visit WHERE StartDate = '?'");
		//c.add("select_visit_bytimerange", "SELECT * FROM visit WHERE StartTime BETWEEN '?' AND '?'");
	}
	
	/**
	 * Creates the top most panel in the stack pane. This is the first panel
	 * the user sees.
	 */
	private void buildTopBox() {
		GridPane pane_search = new GridPane();
		sc_pane = new ModifiableScrollTable();
		GridPane.setConstraints(sc_pane, 0, 5, 5, 8);
		//constraints: col,row,colspan,rowspan
		field_search = new TextField();
		GridPane.setConstraints(field_search, 0, 0, 1, 1);
		//----------------SEARCH BUTTON-------------------------------
		Button btn_search = new Button("Search");
		GridPane.setConstraints(btn_search, 1, 0, 1, 1);
		btn_search.setOnAction((event)->{
			RadioButton rb = (RadioButton) rb_group.getSelectedToggle();
			currentsearch = rb.getText();
			try {
				parseRequest();
			} catch (SQLException e) {
				new ErrorHandle("Database request error in search tab: " + e.getSQLState());
			}			
		});
		
		//--------------------RADIO BUTTONS----------------------------
		rb_group = new ToggleGroup();
		RadioButton rb_student = new RadioButton("Student");
		GridPane.setConstraints(rb_student, 0, 1, 1, 1);
		rb_student.setToggleGroup(rb_group);
		RadioButton rb_class = new RadioButton("Class");
		GridPane.setConstraints(rb_class, 0, 2, 1, 1);
		rb_class.setToggleGroup(rb_group);
		RadioButton rb_appt = new RadioButton("Appointment");
		GridPane.setConstraints(rb_appt, 0, 3, 1, 1);
		rb_appt.setToggleGroup(rb_group);
		rb_group.selectToggle(rb_student);
		
		//-----------------OPEN SELECTED RECORD---------------------
		Button open = new Button("Open");
		open.setOnAction((event)->{
			//TODO create a new tab of the appropriate type
			//and add it to the stack
			CellValue cv = sc_pane.getItem();
			switch(currentsearch) {
			case "Student":
				new StudentPanel(cv);
			}
		});
		GridPane.setConstraints(open, 4, 4, 1, 1);
		pane_search.getChildren().addAll(
				field_search, btn_search, rb_student, rb_class, rb_appt, open, sc_pane);
		stack.getChildren().add(pane_search);
	}
	
	/**
	 * Reads the user's input and determines what query to run.
	 * @param toggle is the radio button that is selected.
	 * @return a query name.
	 */
	private void parseRequest() throws SQLException {
		String text = field_search.getText();
		String queryname = "";
		String[] keys = {};
		switch(currentsearch) {
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
		ResultSet r = c.query(queryname, text);
		sc_pane.setTable(TableMaker.gimmeTable(keys, r));
		r.close();
	}
	
	public void push(GridPane gp) {
		this.stack.getChildren().add(0, gp);
	}
	
	public void pop() {
		this.stack.getChildren().remove(0);
	}
	
	private class StudentPanel extends GridPane {
		
		final private String[] st_class_keys = {"ClassNumber",  "Section", "ClassName"};
		final private String[] st_visit_keys = {"ClassNumber", "StartDate", "StartTime", "Duration"};

		private ModifiableScrollTable class_table;
		private ModifiableScrollTable appt_table;
		
		protected StudentPanel(CellValue cv) {			
			class_table = new ModifiableScrollTable();
			appt_table = new ModifiableScrollTable();
			
			ResultSet classes = c.query("SELECT class_name.ClassNumber, Section, ClassName "
					+ "FROM class_name INNER JOIN ("
					+ "SELECT StudentID, Section, ClassNumber "
					+ "FROM enrolled INNER JOIN class_section "
					+ "ON enrolled.EnrollmentCode = class_section.EnrollmentCode"
					+ ") AS st_section"
					+ "ON class_name.ClassNumber = st_section.ClassNumber "
					+ "WHERE StudentID = " + cv.getStudentID());
			ResultSet visits = c.query("SELECT ClassNumber, StartDate, StartTime, Duration "
					+ "FROM visit WHERE StudentID = " + cv.getStudentID());
			try {
				class_table.setTable(TableMaker.gimmeTable(st_class_keys, classes));
				appt_table.setTable(TableMaker.gimmeTable(st_visit_keys, visits));
				classes.close();
				visits.close();
			} catch (SQLException e) {
				new ErrorHandle("Could not create tables for student: " + e.getSQLState());
			}
			
			Label id = new Label(cv.getStudentID());
			GridPane.setConstraints(id, 0, 0, 1, 1);
			Label st = new Label(cv.getStudentStatus());
			GridPane.setConstraints(st, 3, 0, 1, 1);
			Label lname = new Label(cv.getLastName());
			GridPane.setConstraints(lname, 0, 1, 1, 1);
			Label fname = new Label(cv.getFirstName());
			GridPane.setConstraints(fname, 1, 1, 1, 1);
			Label cl = new Label("Enrolled Classes");
			GridPane.setConstraints(cl, 0, 2, 1, 1);
			Label vs = new Label("Scheduled Appointments");
			GridPane.setConstraints(vs, 0, 8, 1, 1);
			
			this.getChildren().addAll(id,st,lname,fname,cl,vs);

			GridPane.setConstraints(class_table, 0, 3, 4, 5);
			GridPane.setConstraints(appt_table, 0, 9, 4, 5);
			this.getChildren().addAll(class_table, appt_table);
		}
	}
	
}
