/**
 * 
 */
package com.eiko.gui.main;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Mindy
 *
 */
public class ErrorHandle extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4343150770219581929L;

	/**
	 * @throws HeadlessException
	 */
	public ErrorHandle(String error) throws HeadlessException {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Error");
		
		JLabel label = new JLabel("Eroor: " + error);
		JButton exit = new JButton("Exit Program");
		exit.addActionListener((event)->{
			System.exit(0);
		});
		
		JButton close = new JButton("Close Window");
		close.addActionListener((event)->{
			this.dispose();
		});
		
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		main.add(label,BorderLayout.CENTER);
		
		JPanel btn = new JPanel();
		btn.add(exit);
		btn.add(close);
		
		main.add(btn, BorderLayout.SOUTH);
		this.setVisible(true);
	}

}