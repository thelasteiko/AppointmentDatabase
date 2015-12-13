package com.eiko.gui.panels;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.table.CellValue;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.tab.AbstractStackTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Displays the visit data or blank spaces to input a new appointment.
 * 
 * @author Melinda Robertson
 * @version 20151213
 */
public class ApptPanel extends AbstractGridPane {

	/**
	 * The combo box that displays the classes the student is enrolled in.
	 */
	private ComboBox<String> cb_class;
	private ComboBox<String> cb_time;
	private ComboBox<String> cb_duration;
	private DatePicker dp;

	/**
	 * @see {@link AbstractGridPane.AbstractGridPane(AbstractStackTab)}
	 * @param cv
	 */
	public ApptPanel(AbstractStackTab parent, CellValue cv) {
		super(parent);
		build(cv);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eiko.gui.panels.AbstractGridPane#build(com.eiko.back.table.CellValue)
	 */
	@Override
	protected void build(CellValue cv) {
		// ----------------LABELS------------------------------
		Label lbl_class = new Label("Class");
		GridPane.setConstraints(lbl_class, 0, 2);
		GridPane.setMargin(lbl_class, in);
		Label lbl_date = new Label("Date");
		GridPane.setConstraints(lbl_date, 0, 3);
		GridPane.setMargin(lbl_date, in);
		Label lbl_time = new Label("Time");
		GridPane.setConstraints(lbl_time, 0, 4);
		GridPane.setMargin(lbl_time, in);
		Label lbl_dur = new Label("Duration");
		GridPane.setConstraints(lbl_dur, 0, 5);
		GridPane.setMargin(lbl_dur, in);
		this.getChildren().addAll(lbl_class, lbl_date, lbl_time, lbl_dur);
		try {
			getChildren().add(stpanel(getStudent(cv.getStudentID())));
			// ----------------FIELDS TO CHANGE VALUES---------------
			buildClassCombo(cv);
			buildDate(cv);
			buildTimeCombo(cv);
			buildDuration(cv);
			getChildren().addAll(cb_class, dp, cb_time, cb_duration);
			// --------------SAVE BUTTON----------------------------
			Button save = new Button("Save");
			save.setOnAction((event) -> {
				String[] param = new String[] { cv.getStudentID(), cb_class.getValue(), dp.getValue().toString(),
						cb_time.getValue(), cb_duration.getValue() };
				if (cv.getClassNumber() == null) {
					parent.c().update("insert_visit", param);
				} else {
					parent.c().update("delete_visit", new String[] { cv.getStudentID(), cv.getClassNumber(),
							cv.getStartDate(), cv.getStartTime() });
					parent.c().update("insert_visit", param);
				}
				parent.pop();
				parent.pop();
			});
			GridPane.setConstraints(save, 2, 6);
			GridPane.setMargin(save, in);
			getChildren().add(save);
		} catch (SQLException e) {
			new ErrorHandle("Visit panel broke: " + e.getSQLState());
		} catch (NullPointerException e1) {
			new ErrorHandle("Null pointer: " + e1.getCause());
			e1.printStackTrace();
		}
	}

	private void buildClassCombo(CellValue cv) throws SQLException {
		ResultSet r = parent.c().query("select_vt_classes", cv.getStudentID());
		ObservableList<String> a = FXCollections.observableArrayList();
		while (r.next()) {
			a.add(r.getString(1));
		}
		cb_class = new ComboBox<String>(a);
		GridPane.setConstraints(cb_class, 1, 2);
		GridPane.setMargin(cb_class, in);
		if (cv.getClassNumber() != null)
			cb_class.setValue(cv.getClassNumber());
	}

	private void buildTimeCombo(CellValue cv) {
		ObservableList<String> a = FXCollections.observableArrayList();
		int i = 800;
		while (i < 2000) {
			a.add(String.valueOf(i));
			i += 100;
		}
		cb_time = new ComboBox<String>(a);
		GridPane.setConstraints(cb_time, 1, 4);
		GridPane.setMargin(cb_time, in);
		if (cv.getStartTime() != null)
			cb_time.setValue(cv.getStartTime());
	}

	private void buildDate(CellValue cv) {
		dp = new DatePicker();
		GridPane.setConstraints(dp, 1, 3);
		GridPane.setMargin(dp, in);
		if (cv.getStartDate() != null)
			dp.setValue(cv.getAsDate());
	}

	private void buildDuration(CellValue cv) {
		ObservableList<String> a = FXCollections.observableArrayList();
		int i = 15;
		while (i <= 60) {
			a.add(String.valueOf(i));
			i += 15;
		}
		cb_duration = new ComboBox<String>(a);
		GridPane.setConstraints(cb_duration, 1, 5);
		GridPane.setMargin(cb_duration, in);
		if (cv.getDuration() != null)
			cb_duration.setValue(cv.getDuration());
	}
}
