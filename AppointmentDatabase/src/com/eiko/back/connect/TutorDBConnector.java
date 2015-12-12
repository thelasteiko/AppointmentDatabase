package com.eiko.back.connect;

/**
 * This merely adds some convenience to Connector. Inserts
 * default values for the driver, database and user. Also initializes
 * a few queries and stores them for later use.
 * @author Melinda Robertson
 * @version 20151211
 */
public class TutorDBConnector extends Connector {
	
	/**
	 * The driver for this database.
	 */
	public final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * The name of the database and where it is located.
	 */
	public final static String DB_NAME = "jdbc:mysql://localhost/tutor_db";

	/**
	 * Creates the connector by initializing queries that will be used in
	 * the program.
	 */
	public TutorDBConnector() {
		super(DB_DRIVER, DB_NAME, "root", "");
		//calling stored procedures
		add("call_apptbyid", "CALL tutor_db.getApptbyID(?)");
		add("call_apptbyname", "CALL tutor_db.getApptbyName(?)");
		add("select_any", "SELECT * FROM ? WHERE ? = ?");
		
		//----------------SEARCH TAB--------------------------------------
		add("select_student_byid", "SELECT * FROM student WHERE StudentID = ?");
		add("select_student_byname", "SELECT * FROM student WHERE "
				+ "LCASE(student.LastName) LIKE LCASE(\"%?%\") "
				+ "OR LCASE(student.FirstName) LIKE LCASE(\"%?%\")");
		
		add("select_class_byname", "SELECT * FROM class_name WHERE "
				+ "LCASE(ClassName) LIKE LCASE(\"%?%\")");
		add("select_class_byid", "SELECT * FROM class_name WHERE "
				+ "ClassNumber = ?");
		
		add("select_visit_byid", "SELECT * FROM visit WHERE StudentID = ?"
				+ " OR ClassNumber = ?");
		//---------------STUDENT PANEL-----------------------------
		add("select_visit_bydate", "SELECT * FROM visit WHERE StartDate = '?'");
		add("select_st_classes", "SELECT class_name.ClassNumber, Section, ClassName "
					+ "FROM class_name INNER JOIN ("
					+ "SELECT StudentID, Section, ClassNumber "
					+ "FROM enrolled INNER JOIN class_section "
					+ "ON enrolled.EnrollmentCode = class_section.EnrollmentCode"
					+ ") AS st_section "
					+ "ON class_name.ClassNumber = st_section.ClassNumber "
					+ "WHERE StudentID = ?");
		add("select_st_visits", "SELECT ClassNumber, StartDate, StartTime, Duration "
					+ "FROM visit WHERE StudentID = ?");
		add("delete_visit", "DELETE FROM visit WHERE StudentID = ? AND "
				+ "ClassNumber = ? AND StartDate = \"?\" AND StartTime = ?");
		//-------------------CLASS PANEL------------------------------
		add("select_cl_sections", "SELECT Section, EnrollmentCode "
					+ "FROM class_section "
					+ "WHERE ClassNumber = ?");
		add("select_cl_enroll", "SELECT StudentID, FirstName, LastName "
					+ "FROM enr_student WHERE ClassNumber = ?");
		//--------------------APPT PANEL----------------------------
		add("select_vt_classes", "SELECT ClassNumber FROM enr_student "
				+ "WHERE StudentID = ?");
		add("insert_visit", "INSERT INTO visit VALUES (?,?,\"?\",?,?)");
	}
}
