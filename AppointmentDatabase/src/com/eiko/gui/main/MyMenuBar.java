/**
 * 
 */
package com.eiko.gui.main;

import com.eiko.back.connect.TutorDBConnector;

import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Let's the user sign into a database.
 * @author Melinda Robertson
 * @version 20151215
 */
public class MyMenuBar extends MenuBar {
	
	/**
	 * The connector for the database.
	 */
	private TutorDBConnector c;
	/**
	 * The owner of this menu bar.
	 */
	private MainFrame parent;

	/**
	 * Creates the menu bar with file and account options.
	 */
	public MyMenuBar(MainFrame parent) {
		this.parent = parent;
		build();
	}
	/**
	 * Creates the components of the menu bar.
	 */
	private void build() {
		Menu file = new Menu("File");
		Menu account = new Menu("Account");
		
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction((event)->{
			System.exit(0);
		});
		file.getItems().add(exit);
		
		//-----------------DATABASE SIGN IN-----------------------------------------
		TextBox driver = new TextBox("Driver\t\t","com.mysql.jdbc.Driver");
		TextBox host = new TextBox("Host\t\t\t","jdbc:mysql://localhost/tutor_db");
		TextBox username = new TextBox("Username\t","root");
		TextBox password = new TextBox("Password\t\t","");
		
		MenuItem signin = new MenuItem("Sign in");
		signin.setOnAction((event)->{
			c = new TutorDBConnector(driver.getUserText(), host.getUserText(),
					username.getUserText(), password.getUserText());
			parent.setConnector(c);
		});
		
		account.getItems().addAll(driver,host,username,password,signin);
		getMenus().addAll(file,account);
	}
	
	/**
	 * Creates a text field in as a menu item that can be edited
	 * by the user.
	 * @author Melinda Robertson
	 * @version 20151215
	 */
	private class TextBox extends CustomMenuItem {
		/**
		 * This shows what the text field is for.
		 */
		Label lbl;
		/**
		 * Holds the user input.
		 */
		TextField field;
		
		/**
		 * Sets the initial state of the menu item.
		 * @param label is the label for the text field.
		 * @param content is the initial content of the text field.
		 */
		public TextBox(String label, String content) {
			lbl = new Label(label);
			lbl.setTextFill(Color.BLACK);
			field = new TextField(content);
			this.setOnAction((event)->{
				field.selectAll();
			});
			HBox con = new HBox();
			con.getChildren().addAll(lbl,field);
			this.setContent(con);
			setHideOnClick(false);
		}
		
		/**
		 * Retrieves the input from the user.
		 * @return the content of the text field.
		 */
		public String getUserText() {
			return field.getText();
		}
	}

}
