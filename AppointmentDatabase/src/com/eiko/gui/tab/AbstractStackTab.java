package com.eiko.gui.tab;

import com.eiko.back.connect.Connector;
import com.eiko.gui.panels.AbstractGridPane;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
/**
 * Defines a tab that holds panels in a stack. The top-most panel
 * is the one that is visible.
 * @author Melinda Robertson
 * @version 20151213
 */
public abstract class AbstractStackTab extends Tab {
	/**
	 * The stack pane on top of this tab that switches between views.
	 * When a new item should be added, this tab can push and pop panels.
	 */
	private StackPane stack;
	/**
	 * Connects to a database.
	 */
	protected Connector c;
	/**
	 * The constructor. Makes an instance of TabSearch with a database
	 * connector that accesses a tutoring database.
	 * @param c is the database connector.
	 */
	public AbstractStackTab() {
		stack = new StackPane();
		this.setContent(stack);
	}
	
	/**
	 * Sets the connection to the database.
	 * @param c is the connector.
	 */
	abstract public void setConnector(Connector c);
	
	/**
	 * Sets the size of the tab in pixels.
	 * @param width is the width.
	 * @param height is the height.
	 */
	public void setSize(double width, double height) {
		stack.setMinSize(width, height);
	}
	/**
	 * Pushes the given panel onto the stack and displays it.
	 * @param gp is the panel to add.
	 */
	public void push(AbstractGridPane gp) {
		if (stack.getChildren().size() < 1) {
			stack.getChildren().add(gp);
			gp.setVisible(true);
			return;
		}
		this.stack.getChildren().add(0, new ScrollPane(gp));
		stack.getChildren().get(1).setVisible(false);
	}
	/**
	 * Pops the most current panel off the stack and disposes it.
	 * Shows the next most current panel.
	 */
	public void pop() {
		this.stack.getChildren().remove(0);
		if (stack.getChildren().size() < 1)	return;
		stack.getChildren().get(0).setVisible(true);
	}
	
	/**
	 * Pop panels off the stack until there is only one left.
	 */
	public void top() {
		while(stack.getChildren().size() > 1) {
			pop();
		}
	}
	/**
	 * Accesses the database connection.
	 * @return the connector for the database.
	 */
	public Connector c(){
		return c;
	}

}
