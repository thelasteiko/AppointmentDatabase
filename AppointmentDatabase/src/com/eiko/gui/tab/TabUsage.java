package com.eiko.gui.tab;

import com.eiko.back.connect.Connector;

/**
 * Will be used to display usage statistics such as in a chart
 * or graph.
 * @author Melinda Robertson
 * @version 20151213
 */
public class TabUsage extends AbstractStackTab {
	/**
	 * Constructs the tab.
	 */
	public TabUsage() {
		super();
		this.setText("Usage");
	}

	/**
	 * This does nothing right now.
	 */
	@Override
	public void setConnector(Connector c) {
		// TODO Auto-generated method stub
		
	}

}
