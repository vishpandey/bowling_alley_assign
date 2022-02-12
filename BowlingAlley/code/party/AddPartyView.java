package party;
/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import bowler.Bowler;
import bowler.BowlerFile;
import controlDesk.ControlDeskView;
import views.Factory;

import java.util.*;
import java.text.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	private int maxSize;

	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList partyList, allBowlers;
	private Vector party, bowlerdb;
	private Integer lock;

	private ControlDeskView controlDesk;

	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		Factory f = new Factory();
		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = f.CreateJPanel(new GridLayout(1, 3));

		// Party Panel
		JPanel partyPanel = f.CreateJPanel(new FlowLayout(), new TitledBorder("Your Party"));


		party = new Vector();

		partyList = f.CreateEmptyJList(120, 5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = f.CreateJPanel(new FlowLayout(), new TitledBorder("Bowler Database"));

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = f.CreateJList(bowlerdb, 120, 8);

		JScrollPane bowlerPane = f.CreateJScrollPane(allBowlers, "vertical");
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = f.CreateJPanel(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		addPatron = f.CreateJButton("Add to Party");
		JPanel addPatronPanel = f.CreatePanelWithButton(addPatron, new FlowLayout(), this);

		remPatron = f.CreateJButton("Remove Member");
		JPanel remPatronPanel = f.CreatePanelWithButton(remPatron, new FlowLayout(), this);

		newPatron = f.CreateJButton("New Patron");
		JPanel newPatronPanel = f.CreatePanelWithButton(newPatron, new FlowLayout(), this);

		finished = f.CreateJButton("Finished");
		JPanel finishedPanel = f.CreatePanelWithButton(finished, new FlowLayout(), this);

		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron)) {
			if (selectedNick != null && this.party.size() < maxSize) {
				if (this.party.contains(selectedNick)) {
					System.err.println("Member already in Party");
				} else {
					this.party.add(selectedNick);
					partyList.setListData(this.party);
				}
			}
		}
		if (e.getSource().equals(remPatron)) {
			if (selectedMember != null) {
				this.party.removeElement(selectedMember);
				partyList.setListData(this.party);
			}
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView( this );
		}
		if (e.getSource().equals(finished)) {
			if ( this.party != null && this.party.size() > 0) {
				addPartyQueue();
				//controlDesk.updateAddParty( this.party );
			}
			win.hide();
		}

	}

	/**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks	A Vector of NickNames
     *
     */

	public void addPartyQueue() {
		Vector partyBowlers = new Vector();
		for (int i = 0; i < this.party.size(); i++) {
			Bowler newBowler = Bowler.registerPatron(((String) this.party.get(i)));
			partyBowlers.add(newBowler);
		}
		Party newParty = new Party(partyBowlers);

		controlDesk.updateAddParty(newParty);
	}

/**
 * Handler for List actions
 * @param e the ListActionEvent that triggered the handler
 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

/**
 * Called by NewPatronView to notify AddPartyView to update
 * 
 * @param newPatron the NewPatronView that called this method
 */

	public void updateNewPatron(String nick) {
		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.err.println("File I/O Error");
		}
		allBowlers.setListData(bowlerdb);
		party.add(nick);
		partyList.setListData(party);
	}
}
