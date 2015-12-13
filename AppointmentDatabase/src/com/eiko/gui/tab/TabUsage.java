package com.eiko.gui.tab;

import com.eiko.back.connect.TutorDBConnector;

public class TabUsage extends AbstractStackTab {
	
	public TabUsage(TutorDBConnector c) {
		super(c);
		this.setText("Usage");
	}

}
