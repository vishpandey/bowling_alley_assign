/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a patron
 *
 */
package party;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import bowler.Bowler;
import bowler.BowlerFile;
import views.Factory;

import java.util.*;
import java.text.*;

public class NewPatronView implements ActionListener {

	private JFrame win;
	private JButton abort, finished;
	private JTextField nickField, fullField, emailField;
	private String nick, full, email;

	// private boolean done;

	private String selectedNick, selectedMember;
	private AddPartyView addParty;

	public NewPatronView(AddPartyView v) {

		addParty=v;	
		// done = false;

		Factory f = new Factory();

		win = new JFrame("Add Patron");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = f.CreateJPanel(new BorderLayout());

		// Patron Panel
		JPanel patronPanel = f.CreateJPanel(new GridLayout(3, 1), new TitledBorder("Your Info"));


		JPanel nickPanel = f.CreatePanelWithLabel(new FlowLayout(), "Nick Name");
		nickField = new JTextField("", 15);
		nickPanel.add(nickField);

		JPanel fullPanel = f.CreatePanelWithLabel(new FlowLayout(), "Full Name");
		fullField = new JTextField("", 15);
		fullPanel.add(fullField);

		JPanel emailPanel = f.CreatePanelWithLabel(new FlowLayout(), "E-Mail");
		emailField = new JTextField("", 15);
		emailPanel.add(emailField);

		patronPanel.add(nickPanel);
		patronPanel.add(fullPanel);
		patronPanel.add(emailPanel);

		// Button Panel
		JPanel buttonPanel = f.CreateJPanel(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		finished = f.CreateJButton("Add Patron");
		JPanel finishedPanel = f.CreatePanelWithButton(finished, new FlowLayout(), this);

		abort = f.CreateJButton("Abort");
		JPanel abortPanel = f.CreatePanelWithButton(abort, new FlowLayout(), this);

		buttonPanel.add(abortPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(patronPanel, "Center");
		colPanel.add(buttonPanel, "East");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abort)) {
			// done = true;
			win.setVisible(false);
		}

		if (e.getSource().equals(finished)) {
			nick = nickField.getText();
			full = fullField.getText();
			email = emailField.getText();
			// done = true;
			updateNewPatron();
			//addParty.updateNewPatron( this );
			win.setVisible(false);
		}

	}

	private void updateNewPatron() {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( nick );
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
					nick,
					full,
					email);
				addParty.updateNewPatron(nick);
			} else {
				System.err.println( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

}
