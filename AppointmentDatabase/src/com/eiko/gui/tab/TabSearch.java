package com.eiko.gui.tab;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.back.table.CellValue;
import com.eiko.back.table.ModifiableScrollTable;
import com.eiko.back.table.TableMaker;
import com.eiko.gui.main.ErrorHandle;
import com.eiko.gui.main.MainFrame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Runs general database queries on three tables. Opens panels with more
 * detailed information on request.
 * 
 * @author Melinda Robertson
 * @version 20151208
 */
public class TabSearch extends Tab {
	/**
	 * Default insets for every component.
	 */
	public final static Insets in = new Insets(5, 5, 5, 5);

	public final static String[] student_keys = { "StudentID", "FirstName", "LastName", "StudentStatus" };
	public final static String[] class_keys = { "ClassNumber", "ClassName" };
	public final static String[] visit_keys = { "StudentID", "ClassNumber", "StartDate", "StartTime", "Duration" };

	/**
	 * The stack pane on top of this tab that switches between views.
	 * When a new item should be added, this tab can push and pop panels.
	 */
	private StackPane stack;
	/**
	 * The search field for the main panel.
	 */
	private TextField field_search;
	private ToggleGroup rb_group;
	private String currentsearch = "Student";
	private ModifiableScrollTable sc_pane;

	protected TutorDBConnector c;

	/**
	 * The constructor. Makes an instance of TabSearch with a database
	 * connector that accesses a tutoring database.
	 * @param c is the database connector.
	 */
	public TabSearch(TutorDBConnector c) {
		this.setText("Search");
		this.c = c;
		stack = new StackPane();
		stack.setMinSize(500, 500);
		buildTopBox();
		this.setContent(stack);
	}

	/**
	 * Creates the top most panel in the stack pane. This is the first panel the
	 * user sees.
	 */
	private void buildTopBox() {
		GridPane pane_search = new GridPane();
		sc_pane = new ModifiableScrollTable();
		GridPane.setMargin(sc_pane, in);
		GridPane.setConstraints(sc_pane, 0, 5, 5, 8);
		// constraints: col,row,colspan,rowspan
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
		GridPane.setMargin(open, new Insets(0, 0, 0, 100));
		open.setOnAction((event) -> {
			// TODO create a new tab of the appropriate type
			// and add it to the stack
			try {
				CellValue cv = sc_pane.getItem();
				switch (currentsearch) {
				case "Student":
					push(new StudentPanel(cv));
					break;
				case "Class":
					push(new ClassPanel(cv));
					break;
				}
			} catch (NoSuchElementException e) {
				// if item isn't selected or the table isn't available
				// do nothing
			}
		});
		GridPane.setConstraints(open, 4, 4, 1, 1);
		pane_search.getChildren().addAll(field_search, btn_search, rb_student, rb_class, rb_appt, open, sc_pane);
		stack.getChildren().add(pane_search);
	}

	/**
	 * Reads the user's input and determines what query to run.
	 * 
	 * @param toggle
	 *            is the radio button that is selected.
	 * @return a query name.
	 */
	private void parseRequest() throws SQLException {
		String text = field_search.getText();
		String queryname = "";
		String[] keys = {};
		switch (currentsearch) {
		case "Student":
			keys = student_keys;
			if (MainFrame.isNumeric(text)) {
				queryname = "select_student_byid";
				System.out.println(queryname);
			} else
				queryname = "select_student_byname";
			break;
		case "Class":
			// System.out.println(currentsearch);
			keys = class_keys;
			if (MainFrame.isNumeric(text)) {
				queryname = "select_class_byid";
			} else
				queryname = "select_class_byname";
			break;
		case "Appointment":
			keys = visit_keys;
			if (MainFrame.isNumeric(text)) {
				queryname = "select_visit_byid";
			} else if (text.contains("/")) {
				queryname = "select_visit_bydate";
			}
			break;
		default:
			return;
		}
		ResultSet r = c.query(queryname, text);
		sc_pane.setTable(TableMaker.gimmeTable(keys, r));
		r.close();
	}

	/**
	 * Pushes the given panel onto the tab.
	 * 
	 * @param scrollPane
	 *            is the panel to add.
	 */
	public void push(Node e) {
		this.stack.getChildren().add(0, new ScrollPane(e));
		stack.getChildren().get(1).setVisible(false);
	}

	/**
	 * Pops off the top panel so that the previous panel shows.
	 */
	public void pop() {
		this.stack.getChildren().remove(0);
		stack.getChildren().get(0).setVisible(true);
	}

	/**
	 * Creates a new panel with a student's information.
	 * 
	 * @param cv
	 * @return
	 */
	private GridPane stpanel(CellValue cv) {
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
				pop();
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
	 * Inner class for to display student information.
	 * Add, delete, modify appointments for students.
	 * @author Melinda Robertson
	 *
	 */
	private class StudentPanel extends GridPane {

		final private String[] st_class_keys = { "ClassNumber", "Section", "ClassName" };
		final private String[] st_visit_keys = { "ClassNumber", "StartDate", "StartTime", "Duration" };

		private ModifiableScrollTable class_table;
		private ModifiableScrollTable appt_table;

		protected StudentPanel(CellValue cv) {
			// this.setBackground(new Background(new
			// BackgroundFill(Paint.valueOf("Blue"), null, null)));
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
				c.update("delete_visit", param);
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
				push(new ApptPanel(cv2));
			});

			Button add_visit = new Button("Add");
			GridPane.setMargin(add_visit, in);
			GridPane.setConstraints(add_visit, 3, 11);
			add_visit.setOnAction((event)->{
				CellValue cv2 = new CellValue();
				cv2.set("StudentID", cv.getStudentID());
				push(new ApptPanel(cv2));
			});

			this.getChildren().addAll(st_panel, cl, vs);

			GridPane.setConstraints(class_table, 0, 3, 5, 3);
			GridPane.setConstraints(appt_table, 0, 7, 5, 3);
			this.getChildren().addAll(class_table, appt_table, 
					delete_visit, mod_visit, add_visit);
		}
		
		private void makeTables(CellValue cv) {
			try {
				ResultSet classes = c.query("select_st_classes", cv.getStudentID());
				class_table.setTable(TableMaker.gimmeTable(st_class_keys, classes));
				classes.close();
				ResultSet visits = c.query("select_st_visits", cv.getStudentID());
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

	/**
	 * Creates a panel to display information about a class.
	 * @author Melinda Robertson
	 * @version 20151210
	 */
	private class ClassPanel extends GridPane {

		final private String[] section_keys = { "Section", "EnrollmentCode" };
		final private String[] enr_keys = { "StudentID", "FirstName", "LastName" };

		private ModifiableScrollTable sections;
		private ModifiableScrollTable enrollment;

		public ClassPanel(CellValue cv) {
			// this.setBackground(new Background(new
			// BackgroundFill(Paint.valueOf("Blue"), null, null)));
			sections = new ModifiableScrollTable();
			GridPane.setMargin(sections, in);
			enrollment = new ModifiableScrollTable();
			GridPane.setMargin(enrollment, in);
			try {
				ResultSet sect = c.query("select_cl_sections", cv.getClassNumber());
				sections.setTable(TableMaker.gimmeTable(section_keys, sect));
				sect.close();
				ResultSet enr = c.query("select_cl_enroll", cv.getClassNumber());
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
				pop();
			});
			GridPane.setConstraints(ret, 4, 2, 1, 1);
			this.getChildren().addAll(id, st, lname, cl, vs, ret);

			GridPane.setConstraints(sections, 0, 3, 5, 5);
			GridPane.setConstraints(enrollment, 0, 9, 5, 5);
			this.getChildren().addAll(sections, enrollment);
		}
	}

	private class ApptPanel extends GridPane {
		// combos for class, time, duration
		// calendar for date

		private ComboBox<String> cb_class;
		private ComboBox<String> cb_time;
		private ComboBox<String> cb_duration;
		private DatePicker dp;

		public ApptPanel(CellValue cv) {
			//----------------LABELS------------------------------
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
			this.getChildren().addAll(lbl_class,lbl_date,lbl_time,lbl_dur);
			try {
				getChildren().add(stpanel(getStudent(cv.getStudentID())));
				//----------------FIELDS TO CHANGE VALUES---------------
				buildClassCombo(cv);
				buildDate(cv);
				buildTimeCombo(cv);
				buildDuration(cv);
				getChildren().addAll(cb_class,dp,cb_time,cb_duration);
				//--------------SAVE BUTTON----------------------------
				Button save = new Button("Save");
				save.setOnAction((event)->{
					String[] param = new String[] {
							cv.getStudentID(),
							cb_class.getValue(),
							dp.getValue().toString(),
							cb_time.getValue(),
							cb_duration.getValue()
					};
					if(cv.getClassNumber() == null) {
						c.update("insert_visit",param);
					} else {
						c.update("delete_visit", new String[] {
								cv.getStudentID(), cv.getClassNumber(),
								cv.getStartDate(), cv.getStartTime()
						});
						c.update("insert_visit", param);
					}
					pop();
					pop();
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
			ResultSet r = c.query("select_vt_classes", cv.getStudentID());
			ObservableList<String> a = FXCollections.observableArrayList();
			while (r.next()) {
				a.add(r.getString(1));
			}
			cb_class = new ComboBox<String>(a);
			GridPane.setConstraints(cb_class,1,2);
			GridPane.setMargin(cb_class, in);
			if(cv.getClassNumber() != null)
				cb_class.setValue(cv.getClassNumber());
		}

		/**
		 * Returns the student by the id number as a cell value
		 * that can be used to create the student's information panel.
		 * @param id is the student id.
		 * @return the CellValue representation of the student.
		 * @throws SQLException if the student can't be found.
		 */
		private CellValue getStudent(String id) throws SQLException {
			CellValue cv = new CellValue();
			ResultSet st = c.query("select_student_byid", id);
			st.first();
			int n = 1;
			cv.set("StudentID", st.getString(n++));
			cv.set("FirstName", st.getString(n++));
			cv.set("LastName", st.getString(n++));
			cv.set("StudentStatus", st.getString(n));
			st.close();
			return cv;
		}
		
		private void buildTimeCombo(CellValue cv) {
			ObservableList<String> a = FXCollections.observableArrayList();
			int i = 800;
			while(i < 2000) {
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
			if(cv.getStartDate() != null) dp.setValue(cv.getAsDate());
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
			if(cv.getDuration() != null)
				cb_duration.setValue(cv.getDuration());
		}
	}

}
