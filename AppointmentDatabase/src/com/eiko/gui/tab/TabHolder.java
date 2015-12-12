package com.eiko.gui.tab;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.eiko.back.connect.TutorDBConnector;

import javafx.scene.control.TabPane;

/**
 * Holds two tabs that allow the user to query the database
 * in two ways, either to see current data or trend data.
 * @author Melinda Robertson
 * @version 20151211
 */
public class TabHolder extends TabPane implements PropertyChangeListener{
	
	public static final String POP = "pop";
	public static final String PUSH = "push";
	/**
	 * Displays a search box and panels with the results
	 * of the search.
	 */
	private TabSearch tab1;
	/**
	 * This tab was meant to display usage data but currently
	 * is not being used.
	 */
	//private TabUsage tab2;
	
	/**
	 * Constructs a tab pane that initializes two tabs.
	 * The second tab is currently not in use.
	 * @param c is the connector for the database.
	 */
	public TabHolder(TutorDBConnector c) {
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tab1 = new TabSearch(c);
		//tab2 = new TabUsage(c);
		this.getTabs().add(tab1);
		//this.getTabs().add(tab2);
	}

	/**
	 * Will eventually be used to process push and pop functions for the tabs
	 * which will be essentially stacks of GridPanes. 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//TODO
	}
	
	

}
