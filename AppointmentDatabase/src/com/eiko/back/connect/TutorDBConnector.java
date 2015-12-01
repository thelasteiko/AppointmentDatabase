package com.eiko.back.connect;

/**
 * This merely adds some convenience to Connector. Inserts
 * default values for the driver, database and user. Also initializes
 * a few stored procedures directly related to this database.
 * @author Melinda Robertson
 * @version 20151201
 */
public class TutorDBConnector extends Connector {
	
	public final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	public final static String DB_NAME = "jdbc:mysql://localhost/tutor_db";

	public TutorDBConnector() {
		super(DB_DRIVER, DB_NAME, "root", "");
		//using a view
		add("enr_student", "SELECT * FROM enr_student WHERE StudentID = ?");
		//calling stored procedures
		add("call_apptbyid", "CALL tutor_db.getApptbyID(?)");
		add("call_apptbyname", "CALL tutor_db.getApptbyName(?)");
		add("select_any", "SELECT * FROM ? WHERE ? = ?");
//		add("class_select_sections", "SELECT"
//				+ "class_name.ClassNumber, class_name.ClassName,"
//				+ "class_section.Clas FROM class_name INNER JOIN class_section WHERE ");
	}
}
