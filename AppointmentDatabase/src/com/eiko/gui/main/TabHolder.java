package com.eiko.gui.main;

import javafx.scene.control.TabPane;

public class TabHolder extends TabPane {
	
	private TabSearch tab1;
	private TabUsage tab2;
	
	
	public TabHolder() {
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tab1 = new TabSearch();
		tab2 = new TabUsage();
		this.getTabs().add(tab1);
		this.getTabs().add(tab2);
	}

}
