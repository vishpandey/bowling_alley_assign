package lane;
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import java.util.Vector;

import bowler.Bowler;
import party.Party;
import endGame.EndGamePrompt;
import endGame.EndGameReport;
import misc.ReadConfig;
import pinsetter.Pinsetter;
import pinsetter.PinsetterObserver;
import score.ScoreHistoryFile;
import score.ScoreReport;

import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class Lane extends Thread implements PinsetterObserver {	
	private Party party;
	private Pinsetter setter;
	//private HashMap scores;
	private Vector subscribers;

	private boolean gameIsHalted;

	private boolean partyAssigned;	
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;

	private boolean canThrowAgain;
	
	private int[][] finalScores;
	private int gameNumber;
	
	private Bowler currentThrower;			// = the thrower who just took a throw
	public static Bowler first_ct, second_ct;

	private LaneScore ls;

	private boolean throw_allowed_flag;
	private int throw_angle;
	private int throw_velocity;
	

	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() { 
		setter = new Pinsetter();
		//scores = new HashMap();
		ls = new LaneScore();
		subscribers = new Vector();

		gameIsHalted = false;
		partyAssigned = false;
		throw_allowed_flag=false;

		gameNumber = 0;

		setter.subscribe( this );
		
		this.start();
	}

	public void start_game(int angle_value , int velocity_value){
		throw_angle = angle_value;
		throw_velocity = velocity_value;
		System.out.println("setting throw_allowed_flag as true");
		throw_allowed_flag=true;
	}

	public void highest(int []members)
	{
		int n=members.length;
		int max1=members[0],max2=members[0];
		int p=0,q=0;
		for (int i=1;i<n;i++)
		{
			if(max1<members[i])
			{
				max2=max1;
				q=p;
				max1=members[i];
				p=i;
			}
		}
		//System.out.println(p+""+q);

		int k=0;
		for (Object o: party.getMembers())
		{
			Bowler bowl=(Bowler)(o);
			if (k==p)
			{
				first_ct=bowl;
				k++;
			}
			else if (k==q)
			{
				System.out.println(bowl);
				second_ct=bowl;
				k++;
			}
		    else 
				k++;
		}
		
	}
	public void get_second_highest(Party party) {
		int n=0;
		for (Object o: party.getMembers())
		{
			Bowler bowl=(Bowler)(o);
			n++;
		}
		int []members=new int[n];
		int p=0;
		int q=0;	q=p;
		for (Object o: party.getMembers()) {
			Bowler bowl=(Bowler)(o);
			System.out.println(bowl);
			int temp_scores = ((int) ( ls.getCumulScore(q,9)));
			q++;

			members[p]=temp_scores;
			p++;
		}
		
		for(int i: members)
		System.out.println(i);
		highest(members);
		

	}

	/** run()
	 * 
	 * entry point for execution of this lane 
	 */
	public void run() {
		
		while (true) {
			if (partyAssigned && !gameFinished) {	// we have a party on this lane, 
								// so next bower can take a throw
				while (gameIsHalted) {
					try {
						sleep(10);
					} catch (Exception e) {System.err.println("Error: " + e);}
				}

				// System.out.println("4");

				if (bowlerIterator.hasNext()) {
					// System.out.println("3");
					currentThrower = (Bowler)bowlerIterator.next();
					if(currentThrower.block)
						continue;
					System.out.println("current thrower = " + currentThrower.getNick());
					canThrowAgain = true;
					tenthFrameStrike = false;
					ball = 0;
					

					while (canThrowAgain) {
						// System.out.println("1");
						try {
							sleep(10);
						} catch (Exception e) {}
						if(throw_allowed_flag){
							// System.out.println("2");
							setter.ballThrown(currentThrower.getNickName(), throw_angle, throw_velocity);		// simulate the thrower's ball hiting
							ball++;
							System.out.println("ball = " + ball);
							throw_allowed_flag = false;
						}
					}
					
					if (frameNumber == 9){
						finalScores[bowlIndex][gameNumber] = ls.getCumulScore(bowlIndex, 9);
						try{
						ScoreHistoryFile.addScore(currentThrower.getNick(), Integer.toString(ls.getCumulScore(bowlIndex, 9)));
						} catch (Exception e) {System.err.println("Exception in addScore. "+ e );} 
					}
					
					setter.reset();
					bowlIndex++;
					continue;
					
				}
				
				frameNumber++;
				bowlerIterator = (party.getMembers()).iterator();
				for (Object o: party.getMembers()) {
					Bowler bowl=(Bowler)(o);
					bowl.zeroes=0;
				}
				bowlIndex = 0;
				if (frameNumber==10) {
					for (Object o: party.getMembers()) {
						Bowler bowl=(Bowler)(o);
						bowl.block=true;
					}
					get_second_highest(party);
					System.out.println(first_ct.getFullName());
					System.out.println(second_ct.getFullName());
					
					second_ct.block=false;
				}
				if(frameNumber==11) {
					int k=-1,p=0,q=0;
					for (Object o: party.getMembers()) {
						Bowler bowl=(Bowler)(o);
						k++;
						if(bowl==first_ct)
						p=k;
						else if(bowl==second_ct)
						q=k;

					}
					int temp2= ((int) ( ls.getCumulScore(q,9)));
					int temp1 = ((int) ( ls.getCumulScore(p,10)));
					if(temp2>temp1)
						first_ct.block=false;
					else
					{
						gameFinished = true;
						gameNumber++;
						System.out.println("winner is "+first_ct.getFullName());
					}
				}
				if (frameNumber > 13) {
					int k=-1,p=0,q=0;
					for (Object o: party.getMembers()) {
						Bowler bowl=(Bowler)(o);
						k++;
						if(bowl==first_ct)
						p=k;
						else if(bowl==second_ct)
						q=k;

					}
					int temp2= ((int) ( ls.getCumulScore(q,13)));
					int temp1 = ((int) ( ls.getCumulScore(p,13)));
					if(temp1>=temp2)
					System.out.println("winner is "+first_ct.getFullName());
					else
					System.out.println("winner is "+second_ct.getFullName());
					gameFinished = true;
					gameNumber++;
				}

				continue;

			} else if (partyAssigned && gameFinished) {
				EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
				int result = egp.getResult();
				egp.distroy();
				egp = null;
				
				
				System.out.println("result was: " + result);
				
				// TODO: send record of scores to control desk
				if (result == 1) {					// yes, want to play again
					//resetScores();
					resetScores();
		
					gameFinished = false;
					frameNumber = 0;
					//updateBowlerIterator();
					bowlerIterator = (party.getMembers()).iterator();
					
				} else if (result == 2) {// no, dont want to play another game
					Vector printVector;	
					EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
					printVector = egr.getResult();
					partyAssigned = false;

					Iterator scoreIt = party.getMembers().iterator();
					party = null;
					
					publish();
					
					int myIndex = 0;

					while (scoreIt.hasNext()){
						Bowler thisBowler = (Bowler)scoreIt.next();
						ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
						sr.sendEmail(thisBowler.getEmail());
						Iterator printIt = printVector.iterator();
						while (printIt.hasNext()){
							if (thisBowler.getNick() == (String)printIt.next()){
								System.out.println("Printing " + thisBowler.getNick());
								sr.sendPrintout();
							}
						}

					}	
				}
			}
			
			
			try {
				sleep(10);
			} catch (Exception e) {System.err.println("Error: " + e);}
		}
	}

	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(boolean[] ps, boolean foul, int tn, 
		int pinsDownThisThrow, String nick) {
			
		int totalPinsDown = 0;
	
		for (int i=0; i <= 9 && !ps[i]; i++) {
			totalPinsDown++;
		}

		// System.out.println("total pins down " + totalPinsDown);
		// System.out.println("pins down this throw " + pinsDownThisThrow);
		if (pinsDownThisThrow < 0)
			return;

		if(pinsDownThisThrow==0) {
			currentThrower.zeroes++;
		}

		int[] intArgs = new int[]{frameNumber + 1, tn, 
										pinsDownThisThrow, bowlIndex, 
										ball};
		ls.markScore(currentThrower, intArgs); // this is a real throw
		publish();
		// next logic handles the ?: what conditions dont allow them another throw?
		// handle the case of 10th frame first
		if (frameNumber == 9) {
			if (totalPinsDown == 10) {
				setter.resetPins();
				if(tn == 1) {
					tenthFrameStrike = true;
				}
			}
		
			//canThrowAgain = checkThrow
			if (((totalPinsDown != 10) && (tn == 2 && tenthFrameStrike == false)) || tn == 3) {
				canThrowAgain = false;
			}

			return;
		}

		if (pinsDownThisThrow == 10 || tn == 2) {		// threw a strike
			canThrowAgain = false;
		}
	}
		
	/** assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		bowlerIterator = (party.getMembers()).iterator();
		partyAssigned = true;
		
		finalScores = new int[party.getMembers().size()][128];
		ls.initScoresArrays(party.getMembers().size());
		gameNumber = 0;
		
		//resetScores();
		resetScores();
		
		gameFinished = false;
		frameNumber = 0;

		System.out.println("Party assigned");
		publish();
	}

	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** subscribe
	 * 
	 * Method that will add a subscriber
	 * 
	 * @param subscribe	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 * 
	 * @param event	Event that is to be published
	 */

	private void publish() {
		if( subscribers.size() > 0 ) {
			Iterator eventIterator = subscribers.iterator();
			
			while ( eventIterator.hasNext() ) {
				int[] intArgs = new int[]{bowlIndex, frameNumber+1, ball};
				( (LaneObserver) eventIterator.next()).receiveLaneEvent(party, currentThrower, intArgs, gameIsHalted, ls);
			}
		}
	}

	private void publishPausedParty() {
		if( subscribers.size() > 0 ) {
			Iterator eventIterator = subscribers.iterator();
			
			while ( eventIterator.hasNext() ) {
				int[] intArgs = new int[]{bowlIndex, frameNumber+1, ball};
				( (LaneObserver) eventIterator.next()).receivePausedLaneEvent(party, currentThrower, intArgs, ls);
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame(boolean halted) {
		gameIsHalted = halted;
		publish();
	}

	public void initPausedStats(Vector paused_party_vector) {
		this.setter.setRnd((Random)paused_party_vector.get(2));
		this.setter.setPins((boolean[])paused_party_vector.get(3));
		this.setter.setFoul((boolean)paused_party_vector.get(4));
		this.setter.setThrowNumber((Integer)paused_party_vector.get(5));
		this.party = (Party) paused_party_vector.get(6);
		this.ls.setScoresHashMap((HashMap)paused_party_vector.get(7));
		this.partyAssigned = true;
		this.ls.setCumulScores((int[][])paused_party_vector.get(9));
		this.finalScores = (int[][])paused_party_vector.get(10);
		this.canThrowAgain = (boolean)paused_party_vector.get(11);
		this.gameIsHalted = false;
		this.gameFinished = (boolean)paused_party_vector.get(13);
		this.ball = (Integer)paused_party_vector.get(14);
		this.bowlIndex = (Integer)paused_party_vector.get(15);
		this.frameNumber = (Integer)paused_party_vector.get(16);
		this.tenthFrameStrike = (Boolean)paused_party_vector.get(17);
		this.gameNumber = (Integer)paused_party_vector.get(18);
		this.currentThrower = (Bowler)paused_party_vector.get(19);

		bowlerIterator = (this.party.getMembers()).iterator();

		System.out.println("expected bowler : " + this.currentThrower.getNick());
		int count = bowlIndex;
		while(bowlerIterator.hasNext() && count > 0) {
			System.out.println("iterating");
			bowlerIterator.next();
			count--;	
		}

		//System.out.println("current bowler : " + ((Bowler)bowlerIterator.next()).getNick());

		// System.out.println("ball = " + ball);
		// System.out.println("bowl index = " + bowlIndex);
		// System.out.println("frameNumber = " + frameNumber);
		// System.out.println("gameNumber = " + gameNumber);
		
		publishPausedParty();
		//this.gameIsHalted = false;
		
		publish();
 
	}


	public void savePinSetterValues(Vector new_lane_data) {
		new_lane_data.add(this.setter.getRnd());
        new_lane_data.add(this.setter.getPins());
        new_lane_data.add(this.setter.getFoul());
        new_lane_data.add(this.setter.getThrowNumber());
	}

	public void saveLaneValues(Vector new_lane_data) {
		new_lane_data.add(this.party);
        new_lane_data.add(this.ls.getScoresHashMap());
        new_lane_data.add(this.partyAssigned);
        // new_lane_data.add(this.curScores);
        new_lane_data.add(this.ls.getCumulScores());
        new_lane_data.add(this.finalScores);
        new_lane_data.add(this.canThrowAgain);
        new_lane_data.add(this.gameIsHalted);
        new_lane_data.add(this.gameFinished);
        new_lane_data.add(this.ball);
        new_lane_data.add(this.bowlIndex);
	}

	public void saveLaneFrameValues(Vector new_lane_data) {
		new_lane_data.add(this.frameNumber);
        new_lane_data.add(this.tenthFrameStrike);
        new_lane_data.add(this.gameNumber);
        new_lane_data.add(this.currentThrower);
	}

	public void saveLane() throws IOException, ClassNotFoundException, EOFException  {
        // System.out.println("saving the game state");
		Vector old_lane_vector = new Vector<>();
		try {
	        ObjectInputStream in = new ObjectInputStream(new FileInputStream
	        						(ReadConfig.GetPropValues("lane_db_filepath")));
	        old_lane_vector = (Vector) in.readObject();
	        in.close();
		} catch (Exception e) {
			System.out.println("Exception occured");
		}

        Vector new_lane_data = new Vector<>();

        new_lane_data.add(1);
        new_lane_data.add("Game" + old_lane_vector.size());
        savePinSetterValues(new_lane_data);
        saveLaneValues(new_lane_data);
        saveLaneFrameValues(new_lane_data);

        old_lane_vector.add(new_lane_data);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
        							ReadConfig.GetPropValues("lane_db_filepath")));
        out.writeObject(old_lane_vector);

        out.close();
	}

	public static Vector getPausedGames() {
		Vector old_lane_data = new Vector<>();
		try {
        	ObjectInputStream in = new ObjectInputStream(new FileInputStream(
        							ReadConfig.GetPropValues("lane_db_filepath")));
        	old_lane_data = (Vector) in.readObject();
        	in.close();
        } catch (Exception e) {
			System.out.println("Exception occured");
		}
        
        return old_lane_data;
	}

	public static void resumePausedLane(int index) { 
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
									ReadConfig.GetPropValues("lane_db_filepath")));
			Vector  old_lane_data = (Vector) in.readObject();
	        ((Vector) old_lane_data.get(index)).set(0, 0);
	        in.close();
	        
	        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
	        							ReadConfig.GetPropValues("lane_db_filepath")));
	        out.writeObject(old_lane_data);
	        out.close();
		} catch (Exception e) {
			System.out.println("Exception occured");
		} 
    }

	/** resetScores()
	 * 
	 * resets the scoring mechanism, must be called before scoring starts
	 * 
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	private void resetScores() {
		Iterator bowlIt = (party.getMembers()).iterator();

		while ( bowlIt.hasNext() ) {
			int[] toPut = new int[31];
			for ( int i = 0; i != 30; i++){
				toPut[i] = -1;
			}
			ls.setScoresInHashMap((Bowler)bowlIt.next(), toPut );
		}
	}
}
