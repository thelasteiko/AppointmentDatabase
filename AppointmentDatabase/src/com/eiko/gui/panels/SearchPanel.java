package com.eiko.gui.panels;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import com.eiko.back.table.CellValue;
import com.eiko.back.table.ModifiableScrollTable;
import com.eiko.back.table.TableMaker;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.tab.AbstractStackTab;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class SearchPanel extends AbstractGridPane {
	
	public final static String[] student_keys = { "StudentID", "FirstName", "LastName", "StudentStatus" };
	public final static String[] class_keys = { "ClassNumber", "ClassName" };
	public final static String[] visit_keys = { "StudentID", "ClassNumber", "StartDate", "StartTime", "Duration" };
	
	/**
	 * The search field for the main panel.
	 */
	private TextField field_search;
	private ToggleGroup rb_group;
	private String currentsearch = "Student";
	private ModifiableScrollTable sc_pane;

	public SearchPanel(AbstractStackTab parent) {
		super(parent);
		build(null);
	}
	
	/**
	 * Creates the top most panel in the stack pane. This is the first panel the
	 * user sees.
	 */
	protected void build(CellValue cv) {
		sc_pane = new ModifiableScrollTable();
		GridPane.setMargin(sc_pane, in);
		GridPane.setConstraints(sc_pane, 0, 5, 5, 8);
		field_search = new TextField();
		GridPane.setMargin(field_search, in);
		GridPane.setConstraints(field_search, 0, 0, 1, 1);
		// ----------------SEARCH BUTTON-------------------------------
		Button btn_search = new Button("Search");
		GridPane.setConstraints(btn_search, 1, 0, 1, 1);
		btn_search.setOnAction((event) -> {
			RadioButton rb = (RadioButton) rb_group.getSelectedToggle();
			currentsearch = rb.getText();
			try {
				parseRequest();
			} catch (SQLException e) {
				new ErrorHandle("Database request error in search tab: " + e.getSQLState());
			}
		});

		// --------------------RADIO BUTTONS----------------------------
		rb_group = new ToggleGroup();
		RadioButton rb_student = new RadioButton("Student");
		GridPane.setMargin(rb_student, in);
		GridPane.setConstraints(rb_student, 0, 1, 1, 1);
		rb_student.setToggleGroup(rb_group);

		RadioButton rb_class = new RadioButton("Class");
		GridPane.setMargin(rb_class, in);
		GridPane.setConstraints(rb_class, 0, 2, 1, 1);
		rb_class.setToggleGroup(rb_group);

		RadioButton rb_appt = new RadioButton("Appointment");
		GridPane.setMargin(rb_appt, in);
		GridPane.setConstraints(rb_appt, 0, 3, 1, 1);
		rb_appt.setToggleGroup(rb_group);

		rb_group.selectToggle(rb_student);

		// -----------------OPEN SELECTED RECORD---------------------
		Button open = new Button("Open");
		open.setDisable(true);
		open.disableProperty().bind(sc_pane.isNull());
		GridPane.setMargin(open, new Insets(0, 0, 0, 100));
		open.setOnAction((event) -> {
			try {
				CellValue cv2 = sc_pane.getItem();
				switch (currentsearch) {
				case "Student":
					parent.push(new StudentPanel(parent, cv2));
					break;
				case "Class":
					parent.push(new ClassPanel(parent,cv2));
					break;
				}
			} catch (NoSuchElementException e) {
				// if item isn't selected or the table isn't available
				// do nothing
			}
			open.disableProperty().bind(sc_pane.isNull());
		});
		GridPane.setConstraints(open, 4, 4, 1, 1);
		this.getChildren().addAll(field_search, btn_search, rb_student, rb_class, rb_appt, open, sc_pane);
		
	}
	
	/**
	 * Reads the user's input and determines what query to run.
	 * Sets the table to the output returned from the database.
	 */
	private void parseRequest() throws SQLException {
		String text = field_search.getText();
		String queryname = "";
		String[] keys = {};
		switch (currentsearch) {
		case "Student":
			keys = student_keys;
			if (isNumeric(text)) {
				queryname = "select_student_byid";
				System.out.println(queryname);
			} else
				queryname = "select_student_byname";
			break;
		case "Class":
			// System.out.println(currentsearch);
			keys = class_keys;
			if (isNumeric(text)) {
				queryname = "select_class_byid";
			} else
				queryname = "select_class_byname";
			break;
		case "Appointment":
			keys = visit_keys;
			if (isNumeric(text)) {
				queryname = "select_visit_byid";
			} else if (text.contains("/")) {
				queryname = "select_visit_bydate";
			}
			break;
		default:
			return;
		}
		ResultSet r = parent.c().query(queryname, text);
		sc_pane.setTable(TableMaker.gimmeTable(keys, r));
		r.close();
	}

}
