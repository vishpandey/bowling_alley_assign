/* $Id$
 *
 * Revisions:
 *   $Log: LaneObserver.java,v $
 *   Revision 1.2  2003/01/30 21:44:25  ???
 *   Fixed speling of received in may places.
 *
 *   Revision 1.1  2003/01/19 22:12:40  ???
 *   created laneevent and laneobserver
 *
 *
 */
package lane;

import java.util.HashMap;

import bowler.Bowler;
import party.Party;

public interface LaneObserver {
	public void receiveLaneEvent(Party party, int bowlIndex, Bowler currentThrower,
			int frameNumber,  int ball, 
            boolean gameIsHalted, LaneScore ls);
};

