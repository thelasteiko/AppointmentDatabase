package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;

import javafx.scene.control.TabPane;

/**
 * Holds two tabs that allow the user to query the database
 * in two ways, either to see current data or trend data.
 * @author Melinda Robertson
 * @version 20151211
 */
public class TabHolder extends TabPane{

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
	
}
