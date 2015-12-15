package com.eiko.gui.tab;

import com.eiko.back.connect.Connector;
import com.eiko.gui.main.MainFrame;
import com.eiko.gui.panels.SearchPanel;

/**
 * Runs general database queries on three tables. Opens panels with more
 * detailed information on request.
 * 
 * @author Melinda Robertson
 * @version 20151210
 */
public class TabSearch extends AbstractStackTab {

	/**
	 * Creates the search tab.
	 */
	public TabSearch() {
		super();
		this.setText("Search");
		setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
	}
	
	/**
	 * Creates the connection to the database and initializes
	 * the search panel.
	 */
	@Override
	public void setConnector(Connector c) {
		this.c = c;
		push(new SearchPanel(this));
	}
}
