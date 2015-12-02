package com.eiko.back.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.eiko.gui.main.ErrorHandle;

/**
 * Handles interactions with a database. Instead of using prepared statements
 * it passes all arguments as a Strings to the database.
 * @author Melinda Robertson
 * @version 20151201
 */
public class Connector {
	
	protected Connection c;
	/**
	 * Holds stored queries as Strings with labels.
	 * Stored queries are in the same format as PreparedStatements
	 * but have more versatility because they insert parameters verbatim.
	 */
	protected HashMap<String,String> store;
	
	/**
	 * Creates a new Connector object that gives access to the indicated
	 * database.
	 * @param driver is the driver to use, usually com.mysql.jdbc.Driver
	 * @param host is the host and database name.
	 * @param username is the current user.
	 * @param password is the password corresponding to the user.
	 */
	public Connector(String driver, String host, String username, String password) {
		this.store = new HashMap<String,String>();
		try {
			Class.forName(driver);
			c = DriverManager.getConnection(host,username,password);
		} catch (ClassNotFoundException e) {
			System.out.println("Could not connect: Class not found.");
		} catch (SQLException e1) {
			System.out.println("Could not connect: " + e1.getSQLState());
		}
	}
	
	/**
	 * Adds a pre-built query to a stored list.
	 * @param name is the name of the query.
	 * @param stmt is the statement to store.
	 */
	public void add(String name, String stmt) {
		store.put(name, stmt);
	}
	
	/**
	 * Removes one of the queries from the list.
	 * @param name is the name of the query.
	 */
	public void remove(String name) {
		store.remove(name);
	}
	
	/**
	 * Inserts parameters into the indicated query wherever there is
	 * a '?' character.
	 * TODO need to change how the last part is added
	 * @param query is the name of the query.
	 * @param param are the parameters to give the query.
	 * @return a String representing a complete executable query.
	 */
	private String resolve(String query, String... param) {
		String q1 = store.get(query);
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
		return q;
	}
	
	/**
	 * Executes the requested SELECT query. Any other type of query
	 * will throw an error.
	 * @param query is the name of the query to execute.
	 * @param param are the parameters.
	 * @return the result of the query.
	 */
	public ResultSet query(String query, String... param) {
		String q1 = resolve(query, param);
		try {
			if(!q1.toLowerCase().contains("select")) throw new IllegalArgumentException();
			Statement s = c.createStatement();
			return s.executeQuery(q1);
		} catch (SQLException e) {
			new ErrorHandle("SQL error with running query: \n" + e.getSQLState()
					+ ": " + q1);
			return null;
		} catch (IllegalArgumentException e1) {
			new ErrorHandle("Illegal Argument for query: \n" + query);
			return null;
		}
	}
	
	/**
	 * Executes a SELECT query.
	 * @param query is the query to execute.
	 * @return
	 */
	public ResultSet query(String query) {
		try {
			if(!query.toLowerCase().contains("select")) throw new IllegalArgumentException();
			Statement s = c.createStatement();
			return s.executeQuery(query);
		} catch (SQLException e) {
			new ErrorHandle("SQL error with running query: " + e.getSQLState());
			return null;
		} catch (IllegalArgumentException e1) {
			new ErrorHandle("Illegal Argument for query: " + query);
			return null;
		}
	}
	
	/**
	 * Executes a record change in the database. Must be one of
	 * UPDATE, INSERT or DELETE. Any other type of query will throw
	 * an error.
	 * @param query is the name of the query to execute.
	 * @param param are the parameters.
	 */
	public void update(String query, String... param) {
		try {
			String q = resolve(query, param);
			if (q.toLowerCase().contains("update") ||
					q.toLowerCase().contains("delete") ||
					q.toLowerCase().contains("insert"))
				throw new IllegalArgumentException();
			Statement s = c.createStatement();
			s.executeUpdate(q);
		} catch (SQLException e) {
			new ErrorHandle("SQL error with running query: " + e.getSQLState());
		} catch (IllegalArgumentException e1) {
			new ErrorHandle("Illegal Argument for query: " + query);
		}
	}
	
	public void update(String query) {
		try {
			if (query.toLowerCase().contains("update") ||
					query.toLowerCase().contains("delete") ||
					query.toLowerCase().contains("insert"))
				throw new IllegalArgumentException();
			Statement s = c.createStatement();
			s.executeUpdate(query);
		} catch (SQLException e) {
			new ErrorHandle("SQL error with running query: " + e.getSQLState());
		} catch (IllegalArgumentException e1) {
			new ErrorHandle("Illegal Argument for query: " + query);
		}
	}
}
