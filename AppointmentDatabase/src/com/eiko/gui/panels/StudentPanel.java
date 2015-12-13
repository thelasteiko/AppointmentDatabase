/**
 * 
 */
package com.eiko.gui.panels;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.table.CellValue;
import com.eiko.back.table.ModifiableScrollTable;
import com.eiko.back.table.TableMaker;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.tab.AbstractStackTab;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Class to display student information
 * Add, delete, modify appointments for students.
 * @author Melinda Robertson
 * @version 20151213
 */
public class StudentPanel extends AbstractGridPane {
	
	final private String[] st_class_keys = { "ClassNumber", "Section", "ClassName" };
	final private String[] st_visit_keys = { "ClassNumber", "StartDate", "StartTime", "Duration" };
	
	/**
	 * Table that displays the student's classes.
	 */
	private ModifiableScrollTable class_table;
	/**
	 * Table that displays the student's appointments.
	 */
	private ModifiableScrollTable appt_table;

	/**
	 * @param parent
	 */
	public StudentPanel(AbstractStackTab parent, CellValue cv) {
		super(parent);
		build(cv);
	}
	
	/**
	 * Constructs a panel that displays the student information, the classes
	 * they attend and appointments they made.
	 * @param cv is the cell value with the student's information.
	 */
	protected void build(CellValue cv) {
		class_table = new ModifiableScrollTable();
		GridPane.setMargin(class_table, in);
		appt_table = new ModifiableScrollTable();
		GridPane.setMargin(appt_table, in);
		makeTables(cv);
		// x, y, col, row
		// -------------------CREATE STUDENT PANEL---------------
		GridPane st_panel = stpanel(cv);
		// -------------------TABLE LABELS---------------------
		Label cl = new Label("Enrolled Classes");
		GridPane.setMargin(cl, in);
		GridPane.setConstraints(cl, 0, 2, 1, 1);
		Label vs = new Label("Scheduled Appointments");
		GridPane.setMargin(vs, in);
		GridPane.setConstraints(vs, 0, 6, 1, 1);
		//-----------------BUTTONS-----------------------------
		Button mod_visit = new Button("Modify");
		Button delete_visit = new Button("Delete");
		delete_visit.disableProperty().bind(appt_table.isNull());
		GridPane.setMargin(delete_visit, in);
		GridPane.setConstraints(delete_visit, 1, 11);
		delete_visit.setOnAction((event)->{
			CellValue cv2 = appt_table.getItem();
			String[] param = new String[4];
			int n = 0;
			param[n++] = cv.getStudentID();
			param[n++] = cv2.getClassNumber();
			param[n++] = cv2.getStartDate();
			param[n] = cv2.getStartTime();
			parent.c().update("delete_visit", param);
			makeTables(cv);
			delete_visit.disableProperty().bind(appt_table.isNull());
			mod_visit.disableProperty().bind(appt_table.isNull());
		});

		mod_visit.disableProperty().bind(appt_table.isNull());
		GridPane.setMargin(mod_visit, in);
		GridPane.setConstraints(mod_visit, 2, 11);
		mod_visit.setOnAction((event) -> {
			CellValue cv2 = appt_table.getItem();
			cv2.set("StudentID", cv.getStudentID());
			parent.push(new ApptPanel(parent, cv2));
		});

		Button add_visit = new Button("Add");
		GridPane.setMargin(add_visit, in);
		GridPane.setConstraints(add_visit, 3, 11);
		add_visit.setOnAction((event)->{
			CellValue cv2 = new CellValue();
			cv2.set("StudentID", cv.getStudentID());
			parent.push(new ApptPanel(parent, cv2));
		});

		this.getChildren().addAll(st_panel, cl, vs);

		GridPane.setConstraints(class_table, 0, 3, 5, 3);
		GridPane.setConstraints(appt_table, 0, 7, 5, 3);
		this.getChildren().addAll(class_table, appt_table, 
				delete_visit, mod_visit, add_visit);
	}

	/**
	 * Creates the tables to display student classes and appointments.
	 * @param cv is the cell value with the student's information.
	 */
	private void makeTables(CellValue cv) {
		try {
			ResultSet classes = parent.c().query("select_st_classes", cv.getStudentID());
			class_table.setTable(TableMaker.gimmeTable(st_class_keys, classes));
			classes.close();
			ResultSet visits = parent.c().query("select_st_visits", cv.getStudentID());
			appt_table.setTable(TableMaker.gimmeTable(st_visit_keys, visits));
			visits.close();
		} catch (SQLException e) {
			new ErrorHandle("Could not create tables for student: " + e.getSQLState());
		} catch (NullPointerException e1) {
			new ErrorHandle("Null pointer: " + e1.getCause());
			e1.printStackTrace();
		}
	}
}
