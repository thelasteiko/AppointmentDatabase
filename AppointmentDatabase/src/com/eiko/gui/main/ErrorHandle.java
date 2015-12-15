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
 * Creates a window to display a message when an error occurs. The user has
 * the option to close the program or close the window.
 * @author Melinda Robertson
 * @version 20151211
 */
public class ErrorHandle extends JFrame {

	/**
	 * Super serial.
	 */
	private static final long serialVersionUID = 4343150770219581929L;

	/**
	 * Creates a frame to display an error.
	 * @throws HeadlessException
	 */
	public ErrorHandle(String error) throws HeadlessException {
		this.setBounds(
				(int)MainFrame.x,(int)MainFrame.y,
				(int)MainFrame.WIDTH,(int)MainFrame.HEIGHT/2);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Error");
		
		JLabel label = new JLabel("Error: " + error);
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
		this.add(main);
		this.setVisible(true);
	}

}
