/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class for representation of the control desk
 *
 */
package controlDesk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import lane.Lane;
import lane.LaneStatusView;
import party.AddPartyView;
import party.Party;
import pinsetter.Pinsetter;
import views.Factory;

import java.util.*;

public class ControlDeskView implements ActionListener, ControlDeskObserver {

	private JButton addParty, finished, assign;
	private JFrame win;
	private JList partyList;
	
	/** The maximum  number of members in a party */
	private int maxMembers;
	
	private ControlDesk controlDesk;

	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */

	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.numLanes;
		
		Factory f = new Factory();
		
		win = new JFrame("Control Desk");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = f.CreateJPanel(new BorderLayout());

		JPanel controlsPanel = f.CreateJPanel(new GridLayout(), 
												new TitledBorder("Controls"));

		addParty = f.CreateJButton("Add Party");
		JPanel addPartyPanel = f.CreatePanelWithButton(addParty, 
														new FlowLayout(), this);
		controlsPanel.add(addPartyPanel);

		assign = f.CreateJButton("Assign Lanes");
		JPanel assignPanel = f.CreatePanelWithButton(assign, new FlowLayout(), this);
		
		finished = f.CreateJButton("Finished");
		JPanel finishedPanel = f.CreatePanelWithButton(finished, 
														new FlowLayout(), this);
		controlsPanel.add(finishedPanel);

		JPanel laneStatusPanel = f.CreateJPanel(new GridLayout(numLanes, 1), 
									new TitledBorder("Lane Status"));

		HashSet lanes=controlDesk.getLanes();
		Iterator it = lanes.iterator();
		int laneCount=0;
		while (it.hasNext()) {
			Lane curLane = (Lane) it.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.subscribe(laneStat);
			((Pinsetter)curLane.getPinsetter()).subscribe(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		JPanel partyPanel = f.CreateJPanel(new FlowLayout(), new TitledBorder("Party Queue"));

		partyList = f.CreateEmptyJList(120, 10);
		JScrollPane partyPane = f.CreateJScrollPane(partyList, "vertical");
		
		partyPanel.add(partyPane);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	/**
	 * Handler for actionEvents
	 *
	 * @param e	the ActionEvent that triggered the handler
	 *
	 */

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addParty)) {
			AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
		}
		if (e.getSource().equals(assign)) {
			controlDesk.assignLane();
		}
		if (e.getSource().equals(finished)) {
			win.hide();
			System.exit(0);
		}
	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(Party newParty) {
		controlDesk.addPartyQueue(newParty);
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(((Vector) ce.getPartyQueue()));
	}
}
