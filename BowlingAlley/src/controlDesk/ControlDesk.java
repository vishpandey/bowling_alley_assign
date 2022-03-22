/* ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/**
 * Class that represents control desk
 *
 */
package controlDesk;

import java.util.*;

import misc.Queue;
import party.Party;
import bowler.Bowler;
import bowler.BowlerFile;
import lane.Lane;

import java.io.*;

public class ControlDesk extends Thread {

	/** The collection of Lanes */
	private HashSet lanes;

	/** The party wait queue */
	private Queue partyQueue;

	/** The paused party wait queue */
	private Queue pausedPartyQueue;
	
	/** The collection of subscribers */
	private Vector subscribers;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numlanes	the numbler of lanes to be represented
     *
     */

	public ControlDesk(int numLanes) {
		this.lanes = new HashSet(numLanes);
		this.partyQueue = new Queue();
		this.pausedPartyQueue = new Queue();

		subscribers = new Vector();

		for (int i = 0; i < numLanes; i++) {
			this.lanes.add(new Lane());
		}
		
		this.start();

	}
	
	/**
	 * Main loop for ControlDesk's thread
	 * 
	 */
	public void run() {
		while (true) {
			
			assignLane();
			
			try {
				sleep(250);
			} catch (Exception e) {System.err.println("Error: " + e);}
		}
	}

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */

	public void assignLane() {
		Iterator it = lanes.iterator();

		while (it.hasNext() && this.partyQueue.hasMoreElements()) {
			Lane curLane = (Lane) it.next();

			if (curLane.isPartyAssigned() == false) {
				System.out.println("ok... assigning this party");
				curLane.assignParty(((Party) this.partyQueue.next()));
			}
		}

		//it = lanes.iterator();
		while(it.hasNext() && this.pausedPartyQueue.hasMoreElements()) {
			Lane curLane = (Lane) it.next();
			if (curLane.isPartyAssigned() == false) {
				System.out.println("ok... assigning this party");
				Vector paused_party_vector = (Vector) this.pausedPartyQueue.next();
				Party paused_party = (Party) paused_party_vector.get(6);
				//curLane.assignParty(paused_party);
				curLane.initPausedStats(paused_party_vector);
			}
		}
		publish();
	}

	public void resume_paused_game(Vector selected_game_vector) {
		this.pausedPartyQueue.add((Vector)selected_game_vector);
		publish();
	}

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks	A Vector of NickNames
     *
     */

	public void addPartyQueue(Party newParty) {
		this.partyQueue.add(newParty);
		publish();
	}

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
	 *
     * @return a Vecotr of Strings
     *
     */

	private Vector getPartyQueue() {
		Vector displayPartyQueue = new Vector();
		for ( int i=0; i < ( (Vector)this.partyQueue.asVector()).size(); i++ ) {
			StringBuilder nextParty = new StringBuilder();
			nextParty.append(((Bowler) ((Vector) ((Party) this.partyQueue.asVector().get( i ) ).getMembers())
					.get(0))
					.getNickName());
			nextParty.append("'s Party");
			displayPartyQueue.addElement(nextParty.toString());
		}

		for ( int i=0; i < ( (Vector)this.pausedPartyQueue.asVector()).size(); i++ ) {
			StringBuilder nextParty = new StringBuilder();
			Vector paused_party_vector = (Vector) this.pausedPartyQueue.asVector().get(i);
			Party paused_party = (Party)paused_party_vector.get(6);
			Bowler first_bowler = (Bowler) paused_party.getMembers().get(0);
			nextParty.append(first_bowler.getNickName());
			nextParty.append("'s Party");
			displayPartyQueue.addElement(nextParty.toString());
		}

		return displayPartyQueue;
	}

    /**
     * Accessor for the number of lanes represented by the ControlDesk
     * 
     * @return an int containing the number of lanes represented
     *
     */

	// public int getNumLanes() {
	// 	return this.numLanes;
	// }

    /**
     * Allows objects to subscribe as observers
     * 
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */

	public void subscribe(ControlDeskObserver adding) {
		this.subscribers.add(adding);
	}

    /**
     * Broadcast an event to subscribing objects.
     * 
     * @param event	the ControlDeskEvent to broadcast
     *
     */

	public void publish() {
		ControlDeskEvent event = new ControlDeskEvent(getPartyQueue());
		Iterator eventIterator = subscribers.iterator();
		while (eventIterator.hasNext()) {
			(
				(ControlDeskObserver) eventIterator
					.next())
					.receiveControlDeskEvent(
				event);
		}
	}

    /**
     * Accessor method for lanes
     * 
     * @return a HashSet of Lanes
     *
     */

	public HashSet getLanes() {
		return this.lanes;
	}
}
