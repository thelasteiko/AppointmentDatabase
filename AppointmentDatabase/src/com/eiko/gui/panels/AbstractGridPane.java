package com.eiko.gui.panels;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.table.CellValue;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.tab.AbstractStackTab;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
/**
 * An abstract class as the base for panels that go into the stack tabs.
 * In this implementation the panels access the parent's push and pop
 * methods to modify the stack.
 * @author Melinda Robertson
 * @version 20151213
 */
public abstract class AbstractGridPane extends GridPane {
	
	/**
	 * Default insets for every component.
	 */
	public final static Insets in = new Insets(5, 5, 5, 5);
	/**
	 * The parent stack tab.
	 */
	protected AbstractStackTab parent;

	/**
	 * Constructs the panel.
	 * @param parent is the stack tab that holds this panel.
	 */
	public AbstractGridPane(AbstractStackTab parent) {
		this.parent = parent;
	}
	/**
	 * Builds the panel.
	 * @param cv holds the information to display in the panel.
	 */
	abstract protected void build(CellValue cv);
	/**
	 * Creates a new panel with a student's information.
	 * 
	 * @param cv is the cell value that holds the information about
	 * 				the student.
	 * @return a GridPane that displays the student's information.
	 */
	protected GridPane stpanel(CellValue cv) {
		GridPane st = new GridPane();
		try {
			Label id = new Label(cv.getStudentID());
			GridPane.setMargin(id, in);
			GridPane.setConstraints(id, 0, 0, 1, 1);
			Label stat = new Label(cv.getStudentStatus());
			GridPane.setMargin(stat, in);
			GridPane.setConstraints(stat, 4, 0, 1, 1);
			Label lname = new Label(cv.getLastName());
			GridPane.setMargin(lname, in);
			GridPane.setConstraints(lname, 0, 1, 1, 1);
			Label fname = new Label(cv.getFirstName());
			GridPane.setMargin(fname, new Insets(5, 200, 5, 5));
			GridPane.setConstraints(fname, 1, 1, 1, 1);
			Button ret = new Button("Return");
			ret.setOnAction((event) -> {
				parent.pop();
			});
			GridPane.setConstraints(ret, 4, 1, 1, 1);
			GridPane.setConstraints(st, 0, 0, 5, 2);
			st.getChildren().addAll(id, stat, lname, fname, ret);
		} catch (NullPointerException e) {
			new ErrorHandle("Cell Value is not student.");
		}
		GridPane.setConstraints(st, 0, 0);
		GridPane.setHgrow(st, Priority.ALWAYS);
		return st;
	}

	
	/**
	 * Checks to see if a string is numeric.
	 * Located here for convenience.
	 * @param s is the string.
	 * @return true if every character represents a number, false otherwise.
	 */
	public static boolean isNumeric(String s) {
		for(char c: s.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	/**
	 * Returns the student by the id number as a cell value
	 * that can be used to create the student's information panel.
	 * @param id is the student id.
	 * @return the CellValue representation of the student.
	 * @throws SQLException if the student can't be found.
	 */
	protected CellValue getStudent(String id) throws SQLException {
		CellValue cv = new CellValue();
		ResultSet st = parent.c().query("select_student_byid", id);
		st.first();
		int n = 1;
		cv.set("StudentID", st.getString(n++));
		cv.set("FirstName", st.getString(n++));
		cv.set("LastName", st.getString(n++));
		cv.set("StudentStatus", st.getString(n));
		st.close();
		return cv;
	}
}
