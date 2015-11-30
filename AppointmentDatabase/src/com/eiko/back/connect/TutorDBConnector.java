package com.eiko.back.connect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TutorDBConnector extends Connector {
	
	public final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	public final static String DB_NAME = "jdbc:mysql://localhost/tutor_db";

	public TutorDBConnector() {
		super(DB_DRIVER, DB_NAME, "root", "");
		add("student_select_ID", "SELECT * FROM student WHERE StudentID = ?");
		add("class_select_number", "SELECT * FROM class_name WHERE ClassNumber = ?");
//		add("class_select_sections", "SELECT"
//				+ "class_name.ClassNumber, class_name.ClassName,"
//				+ "class_section.Clas FROM class_name INNER JOIN class_section WHERE ");
	}
	
	private void setStudent(PreparedStatement ps, String[] param) {
		try {
			ps.setInt(1, Integer.parseInt(param[0]));
			ps.setString(2, param[1]);
			ps.setString(3, param[2]);
			ps.setString(4, param[3]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void runQueryOn(String query, String table, String[] param) {
		// TODO Auto-generated method stub
		
	}

}
