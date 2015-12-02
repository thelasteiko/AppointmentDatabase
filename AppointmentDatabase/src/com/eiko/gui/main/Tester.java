package com.eiko.gui.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.eiko.back.connect.TutorDBConnector;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Tester extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		String sql = "SELECT * FROM class_name WHERE "
				+ "LCASE(ClassName) LIKE LCASE(CONCAT('%','?','%'))";
		String sql2 = "SELECT * FROM class_name WHERE "
				+ "ClassNumber = ?";
		System.out.println(resolve(sql, "melinda"));
		System.out.println(resolve(sql2, "447"));
		System.exit(0);
	}
	
	/**
	 * Inserts parameters into the indicated query wherever there is
	 * a '?' character.
	 * TODO need to change how the last part is added
	 * @param query is the name of the query.
	 * @param param are the parameters to give the query.
	 * @return a String representing a complete executable query.
	 */
	public static String resolve(String q1, String... param) {
		//String q1 = store.get(query);
		String q = "";
		int qindex = q1.indexOf('?');
		int last = -1;
		int i = 0;
		while (qindex >= 0 && i < param.length) {
			q += q1.substring(last+1, qindex);
			q += param[i++];
			last = qindex;
			qindex = q1.indexOf('?',last+1);
		}
		if (last < q1.length() - 1) q += q1.substring(last);
		System.out.println("qindex: " + qindex + "\tlast: " + last + "\tlength: " + q1.length());
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
