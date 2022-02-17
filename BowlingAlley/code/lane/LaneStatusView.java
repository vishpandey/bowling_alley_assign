/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package lane;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import bowler.Bowler;
import party.Party;
import pinsetter.PinSetterView;
import pinsetter.Pinsetter;
import pinsetter.PinsetterObserver;
import views.Factory;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

	private JPanel jp;

	private JLabel curBowler, pinsDown;
	private JButton viewLane;
	private JButton viewPinSetter, maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;

	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing=false;
		psShowing=false;

		psv = new PinSetterView( laneNum );
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView( lane, laneNum );
		lane.subscribe(lv);

		Factory f = new Factory();

		jp = f.CreateJPanel(new FlowLayout());
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = f.CreateJPanel(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		viewLane = f.CreateJButton("View Lane");
		JPanel viewLanePanel = f.CreatePanelWithButton(viewLane, new FlowLayout(), this);

		viewPinSetter = f.CreateJButton("Pinsetter");
		JPanel viewPinSetterPanel = f.CreatePanelWithButton(viewPinSetter, new FlowLayout(), this);

		maintenance = f.CreateJButton("     ");
		maintenance.setBackground( Color.GREEN );
		JPanel maintenancePanel = f.CreatePanelWithButton(maintenance, new FlowLayout(), this);

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );


		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);

		jp.add( cLabel );
		jp.add( curBowler );
		jp.add( pdLabel );
		jp.add( pinsDown );
		
		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}

	public void actionPerformed( ActionEvent e ) {
		if ( lane.isPartyAssigned() ) { 
			if (e.getSource().equals(viewPinSetter)) {
				if ( psShowing == false ) {
					psv.show();
					psShowing=true;
				} else if ( psShowing == true ) {
					psv.hide();
					psShowing=false;
				}
			}
		}
		if (e.getSource().equals(viewLane)) {
			if ( lane.isPartyAssigned() ) { 
				if ( laneShowing == false ) {
					lv.display(1);
					laneShowing=true;
				} else if ( laneShowing == true ) {
					lv.display(0);
					laneShowing=false;
				}
			}
		}
		if (e.getSource().equals(maintenance)) {
			if ( lane.isPartyAssigned() ) {
				lane.pauseGame(false);
				maintenance.setBackground( Color.GREEN );
			}
		}
	}

	public void receiveLaneEvent(Party party, Bowler currentThrower, int[] intArgs,
								boolean gameIsHalted, LaneScore ls) {
		curBowler.setText( currentThrower.getNickName() );
		if ( gameIsHalted ) {
			maintenance.setBackground( Color.RED );
		}	
		if ( lane.isPartyAssigned() == false ) {
			viewLane.setEnabled( false );
			viewPinSetter.setEnabled( false );
		} else {
			viewLane.setEnabled( true );
			viewPinSetter.setEnabled( true );
		}
	}

	public void receivePinsetterEvent(boolean[] ps, boolean foul, int tn, int pinsDownThisThrow) {
		int count = 0;
		
		for (int i=0; i <= 9; i++) {
			if (!ps[i]) {
				count++;
			}
		}
		pinsDown.setText( ( new Integer(count).toString()) );
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );
		
	}

}
