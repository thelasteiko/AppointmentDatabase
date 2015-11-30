package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;

import javafx.scene.control.TabPane;

public class TabHolder extends TabPane {
	
	private TabSearch tab1;
	private TabUsage tab2;
	
	
	public TabHolder(TutorDBConnector c) {
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tab1 = new TabSearch(c);
		tab2 = new TabUsage(c);
		this.getTabs().add(tab1);
		this.getTabs().add(tab2);
	}

}
