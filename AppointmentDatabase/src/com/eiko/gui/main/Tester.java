package com.eiko.gui.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.connect.TutorDBConnector;
import com.eiko.back.table.CellValue;
import com.eiko.back.table.TableMaker;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Tester extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Group group = new Group();
		Scene scene = new Scene(group);
		String[] student_keys = {"StudentID", "FirstName", "LastName", "StudentStatus"};
		String sql = "SELECT * FROM student "
				+ "WHERE LCASE(student.LastName) LIKE LCASE(\"%?%\") "
				+ "OR LCASE(student.FirstName) LIKE LCASE(\"%?%\")";
		TutorDBConnector c = new TutorDBConnector();
		String s2 = this.resolve(sql, new String[] {"melinda", "melinda"});
		System.out.println(s2);
		ResultSet r = c.query(s2);
		TableView<CellValue> table = TableMaker.gimmeTable(student_keys, r);
		table.setMinSize(250, 500);
		group.getChildren().add(table);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void test2() {
		String sql = "SELECT * FROM class_name WHERE "
				+ "LCASE(ClassName) LIKE LCASE(CONCAT('%','?','%'))";
		String sql2 = "SELECT * FROM class_name WHERE "
				+ "ClassNumber = ?";
		System.out.println(resolve(sql, "melinda"));
		System.out.println(resolve(sql2, "447"));
	}
	
	/**
	 * Inserts parameters into the indicated query wherever there is
	 * a '?' character.
	 * TODO need to change how the last part is added
	 * @param query is the name of the query.
	 * @param param are the parameters to give the query.
	 * @return a String representing a complete executable query.
	 */
	public String resolve(String q1, String... param) {
		//String q1 = store.get(query);
		String q = "";
		int qindex = q1.indexOf('?');
		int last = -1;
		int i = 0;
		while (qindex >= 0) {
			q += q1.substring(last+1, qindex);
			q += param[i++];
			last = qindex;
			qindex = q1.indexOf('?',last+1);
			if(i == param.length) i = 0;
		}
		if (last < q1.length() - 1) q += q1.substring(last+1);
		//System.out.println("qindex: " + qindex + "\tlast: " + last + "\tlength: " + q1.length());
		return q;
	}
	
	public static void test1() {
		TutorDBConnector c = new TutorDBConnector();
		String query = "select_any";
		String[] param = {
				"student", "LastName", "'Robertson'"
		};
		ResultSet r = c.query(query, param); 
		
		try {
			while(r.next()) {
				for (int i = 1; i <= 4; i++) {
					System.out.println(r.getString(i));
				}
			}
			r.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
