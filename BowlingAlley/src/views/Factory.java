package views;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;

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



	// public JLabel CreateJLabel(String labelText) {
	// 	JLabel label = new JLabel(labelText);

	// 	return label;
	// }

	public JPanel CreatePanelWithLabel(LayoutManager mgr, String labelText) {
		JPanel panel = CreateJPanel(mgr);
		JLabel label = new JLabel(labelText);
		panel.add(label);

		return panel;
	}

	public JPanel CreatePanelWithLabel(String labelText) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(labelText);
		panel.add(label);

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

	public JPanel CreatePanelWithButton(JButton button, ActionListener l) {
		JPanel panel = new JPanel();
		button.addActionListener(l);
		panel.add(button);
		
		return panel;
	}

	public JPanel CreatePanelWithButton(String label, ActionListener l) {
		JButton button = CreateJButton(label);
		JPanel panel = new JPanel();
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

	public JList CreateEmptyJList(int width, int rowCount,  ListSelectionListener l) {
		Vector empty = new Vector();
		empty.add("(Empty)");

		JList list = new JList(empty);
		list.setFixedCellWidth(width);
		list.setVisibleRowCount(rowCount);
		list.addListSelectionListener(l);
		
		return list;
	}

	public JList CreateJList(Vector vec, int width, int rowCount) {
		
		JList list = new JList(vec);
		list.setFixedCellWidth(width);
		list.setVisibleRowCount(rowCount);
		
		return list;
	}

	public JList CreateJList(Vector vec, int width, int rowCount,  ListSelectionListener l) {
		
		JList list = new JList(vec);
		list.setFixedCellWidth(width);
		list.setVisibleRowCount(rowCount);
		list.addListSelectionListener(l);
		
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

	public JSlider CreateSlider(int min_angle, int max_angle, LayoutManager mgr) {
		JSlider slider = new JSlider(min_angle, max_angle);
		slider.setLayout (mgr);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		return slider;
	}

	public JSlider CreateSliderWithMajorTick(int min_angle, int max_angle, int majorTick) {
		JSlider slider = new JSlider(min_angle, max_angle);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		return slider;
	}

	public void centreFrame(JFrame win) {
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();
	}

	public void AddPanelsToParentPanel(JPanel parent, JPanel[] children) {
		for(int i = 0; i < children.length; i++) {
			parent.add(children[i]);
		}
	}
}