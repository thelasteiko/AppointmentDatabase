package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;

import javafx.scene.control.TabPane;

/**
 * Holds two tabs that allow the user to query the database
 * in two ways, either to see current data or trend data.
 * @author Melinda Robertson
 * @version 20151201
 */
public class TabHolder extends TabPane {
	
	private TabSearch tab1;
	private TabUsage tab2;
	
	/**
	 * Constructs a tab pane that initializes two tabs.
	 * @param c
	 */
	public TabHolder(TutorDBConnector c) {
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		this.setMinSize(500, 500);
		tab1 = new TabSearch(c);
		tab2 = new TabUsage(c);
		this.getTabs().add(tab1);
		this.getTabs().add(tab2);
	}

}
