package com.eiko.gui.tab;

import com.eiko.back.connect.Connector;
import com.eiko.gui.panels.AbstractGridPane;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;

public abstract class AbstractStackTab extends Tab {
	/**
	 * The stack pane on top of this tab that switches between views.
	 * When a new item should be added, this tab can push and pop panels.
	 */
	private StackPane stack;
	protected Connector c;
	/**
	 * The constructor. Makes an instance of TabSearch with a database
	 * connector that accesses a tutoring database.
	 * @param c is the database connector.
	 */
	public AbstractStackTab(Connector c) {
		this.c = c;
		stack = new StackPane();
		this.setContent(stack);
	}
	
	public void setSize(double wIDTH, double hEIGHT) {
		stack.setMinSize(wIDTH, hEIGHT);
	}
	
	public void push(AbstractGridPane gp) {
		if (stack.getChildren().size() < 1) {
			stack.getChildren().add(gp);
			gp.setVisible(true);
			return;
		}
		this.stack.getChildren().add(0, new ScrollPane(gp));
		stack.getChildren().get(1).setVisible(false);
	}
	
	public void pop() {
		this.stack.getChildren().remove(0);
		if (stack.getChildren().size() < 1)	return;
		stack.getChildren().get(0).setVisible(true);
	}
	
	public Connector c(){
		return c;
	}

}
