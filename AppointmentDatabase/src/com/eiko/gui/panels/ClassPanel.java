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
 * Creates a panel to display information about a class.
 * @author Melinda Robertson
 * @version 20151210
 */
public class ClassPanel extends AbstractGridPane {
	
	final private String[] section_keys = { "Section", "EnrollmentCode" };
	final private String[] enr_keys = { "StudentID", "FirstName", "LastName" };
	
	/**
	 * The table that displays sections scheduled for this class.
	 */
	private ModifiableScrollTable sections;
	/**
	 * Table that displays the students that are enrolled.
	 */
	private ModifiableScrollTable enrollment;

	/**
	 * @param parent
	 * @param cv
	 */
	public ClassPanel(AbstractStackTab parent, CellValue cv) {
		super(parent);
		build(cv);
	}

	/* (non-Javadoc)
	 * @see com.eiko.gui.panels.AbstractGridPane#build(com.eiko.back.table.CellValue)
	 */
	@Override
	protected void build(CellValue cv) {
		sections = new ModifiableScrollTable();
		GridPane.setMargin(sections, in);
		enrollment = new ModifiableScrollTable();
		GridPane.setMargin(enrollment, in);
		try {
			ResultSet sect = parent.c().query("select_cl_sections", cv.getClassNumber());
			sections.setTable(TableMaker.gimmeTable(section_keys, sect));
			sect.close();
			ResultSet enr = parent.c().query("select_cl_enroll", cv.getClassNumber());
			enrollment.setTable(TableMaker.gimmeTable(enr_keys, enr));
			enr.close();
		} catch (SQLException e) {
			new ErrorHandle("Could not create tables for class: " + e.getSQLState());
		} catch (NullPointerException e1) {
			new ErrorHandle("Null pointer: " + e1.getCause());
			e1.printStackTrace();
		}

		Label id = new Label(cv.getClassNumber());
		GridPane.setMargin(id, in);
		GridPane.setConstraints(id, 0, 0, 1, 1);
		Label st = new Label("# out of total"); // could have number of
												// students enrolled and how
												// many seats are left
		GridPane.setMargin(st, in);
		GridPane.setConstraints(st, 4, 0, 1, 1);
		Label lname = new Label(cv.getClassName());
		GridPane.setMargin(lname, in);
		GridPane.setConstraints(lname, 0, 1, 1, 1);
		Label cl = new Label("Sections");
		GridPane.setMargin(cl, in);
		GridPane.setConstraints(cl, 0, 2, 1, 1);
		Label vs = new Label("Currently Enrolled");
		GridPane.setMargin(vs, in);
		GridPane.setConstraints(vs, 0, 8, 1, 1);

		Button ret = new Button("Return");
		ret.setOnAction((event) -> {
			parent.pop();
		});
		GridPane.setConstraints(ret, 4, 2, 1, 1);
		this.getChildren().addAll(id, st, lname, cl, vs, ret);

		GridPane.setConstraints(sections, 0, 3, 5, 5);
		GridPane.setConstraints(enrollment, 0, 9, 5, 5);
		this.getChildren().addAll(sections, enrollment);
	}

}
