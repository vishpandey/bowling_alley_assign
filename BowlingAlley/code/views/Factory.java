package views;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Factory {
	
	public JPanel CreateJPanel(LayoutManager mgr) {
		JPanel panel = new JPanel();
		panel.setLayout(mgr);
		
		return panel;
	}
	
	public JPanel CreateJPanel(LayoutManager mgr, Border border) {
		JPanel panel = new JPanel();
		panel.setLayout(mgr);
		panel.setBorder(border);
		
		return panel;
	}
	
	public JButton CreateJButton(String label) {
		JButton button = new JButton(label);
		
		return button;
	}
	
	public JPanel CreatePanelWithButton(JButton button, LayoutManager mgr, ActionListener l) {
		JPanel panel = new JPanel();
		panel.setLayout(mgr);
		button.addActionListener(l);
		panel.add(button);
		
		return panel;
	}
	
	public JList CreateEmptyJList(int width, int rowCount) {
		Vector empty = new Vector();
		empty.add("(Empty)");
		
		JList list = new JList(empty);
		list.setFixedCellWidth(120);
		list.setVisibleRowCount(10);
		
		return list;
	}

	public JList CreateJList(Vector vec, int width, int rowCount) {
		
		JList list = new JList(vec);
		list.setFixedCellWidth(120);
		list.setVisibleRowCount(10);
		
		return list;
	}	
	
	public JScrollPane CreateJScrollPane(JList list, String scrollDirection) {
		JScrollPane pane = new JScrollPane(list);
		switch(scrollDirection) {
		case "vertical":
			pane.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			break;
		}
		
		return pane;
	}
}