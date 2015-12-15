package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;
/**
 * Will be used to display usage statistics such as in a chart
 * or graph.
 * @author Melinda Robertson
 * @version 20151213
 */
public class TabUsage extends AbstractStackTab {
	/**
	 * Constructs the tab.
	 * @param c is the connection to the database.
	 */
	public TabUsage(TutorDBConnector c) {
		super(c);
		this.setText("Usage");
	}

}
