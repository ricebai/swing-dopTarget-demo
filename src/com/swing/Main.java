package com.swing;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class Main {
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		final JFrame f = new JFrame("JEditor Pane Drop Target Example 1");

		JEditorPane pane = new JEditorPane();

		// Add a drop target to the JEditorPane
		// EditorDropTarget target = 
				new EditorDropTarget(pane);
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JScrollPane(pane), BorderLayout.CENTER);
		f.setSize(500, 400);
		f.setVisible(true);
	}
}
