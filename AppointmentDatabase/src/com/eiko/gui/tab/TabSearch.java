package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;
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
	 * Creates the search tab
	 * @param c
	 */
	public TabSearch(TutorDBConnector c) {
		super(c);
		this.setText("Search");
		setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
		push(new SearchPanel(this));
	}
}
