package com.eiko.back.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
/**
 * HINTS:
 * 	1.	use java.sql.Date to set or add Date attributes
 * 		java.sql.Date(new java.util.Date());	<--makes today's date
 * @author Melinda Robertson
 *
 */
public abstract class Connector {
	
	protected Connection c;
	private HashMap<String,PreparedStatement> store;
	
	public Connector(String driver, String host, String username, String password) {
		this.store = new HashMap<String,PreparedStatement>();
		try {
			Class.forName(driver);
			c = DriverManager.getConnection(host,username,password);
		} catch (ClassNotFoundException e) {
			System.out.println("Could not connect: Class not found.");
		} catch (SQLException e1) {
			System.out.println("Could not connect: " + e1.getSQLState());
		}
	}
	
	public void add(String name, String st) {
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(st);
			store.put(name, ps);
		} catch (SQLException e) {
			System.out.println("Could not add statment: " + e.getSQLState());
		}
	}
	
	public void remove(String name) {
		store.remove(name);
	}
	
	/**
	 * Given parameters must match that query's number of
	 * parameters.
	 * 
	 * EX: "SELECT * FROM student WHERE student_id = ?"
	 * 		param.length is then 1
	 * @param name
	 * @param param
	 * @return
	 */
	public ResultSet select(String name, String[] param) {
		try {
			PreparedStatement ps = store.get(name);
			for(int i = 0; i < param.length; i++) {
				ps.setString(i+1, param[i]);
			}
			return ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Could not execute select query: " + e.getSQLState());
			return null;
		}
	}
	
	public void update(String name, String[] param) {
		try {
			PreparedStatement ps = store.get(name);
			for(int i = 0; i < param.length; i++) {
				ps.setString(i+1, param[i]);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Could not execute update: " + e.getSQLState());
		}
	}
	
	public ResultSet query(String stmt) {
		try {
			Statement s = c.createStatement();
			return s.executeQuery(stmt);
		} catch (SQLException e) {
			System.out.println("Could not execute general query: " + e.getSQLState());
			return null;
		}
	}
	public abstract void runQueryOn(String query, String table, String[] param);

}
