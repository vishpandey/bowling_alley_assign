/*
 *  constructs a prototype Lane View
 *
 */
package lane;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;

import party.Party;
import views.Factory;
import bowler.Bowler;

import java.util.*;

public class LaneView implements LaneObserver, ActionListener {

	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	//JPanel[][] scores;
	JLabel[][] scoreLabel;
	//JPanel[][] ballGrid;
	//JPanel[] pins;

	JButton maintenance;
	Lane lane;

	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void display(int num) {
		if(num == 0) {
			frame.hide();
			return;
		}
		
		frame.show();
	}

	// public void hide() {
	// 	frame.hide();
	// }

	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		JPanel[][] scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		JPanel[][] ballGrid = new JPanel[numBowlers][10];
		JPanel[] pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNick()));
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void receiveLaneEvent(Party party, Bowler currentThrower,
								 int[] intArgs, boolean gameIsHalted, LaneScore ls) {

		if (lane.isPartyAssigned()) {
			int numBowlers = party.getMembers().size();
			while (!initDone) {
				//System.out.println("chillin' here.");
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}

			if (intArgs[1] == 1
				&& intArgs[2] == 0
				&& intArgs[0] == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(party), "Center");

				Factory f = new Factory();
				// Button Panel
				JPanel buttonPanel = f.CreateJPanel(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = f.CreatePanelWithButton(maintenance, new FlowLayout(), this);

				buttonPanel.add(maintenancePanel);

				cpanel.add(buttonPanel, "South");

				frame.pack();

			}

			int[][] lescores = ls.getCumulScores();
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= intArgs[1] - 1; i++) {
					if (lescores[k][i] != 0)
						scoreLabel[k][i].setText(
							(new Integer(lescores[k][i])).toString());
				}
				for (int i = 0; i < 21; i++) {
					int[] temp_scores = ((int[]) ((HashMap) ls.getScoresHashMap()).get(bowlers.get(k)));
					if (temp_scores[i] != -1)
						if (temp_scores[i] == 10
							&& (i % 2 == 0 || i == 19))
							ballLabel[k][i].setText("X");
						else if (i > 0 && temp_scores[i] + temp_scores[i- 1] == 10 && i % 2 == 1)
							ballLabel[k][i].setText("/");
						else if (temp_scores[i] == -2 ){
							
							ballLabel[k][i].setText("F");
						} else
							ballLabel[k][i].setText(
								(new Integer(temp_scores[i])).toString());
				}
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame(true);
		}
	}
}
