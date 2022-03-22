/*
 *  constructs a prototype Lane View
 *
 */
package lane;

import java.awt.*;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import party.Party;
import views.Factory;
import bowler.Bowler;

import java.util.*;

import misc.ReadConfig;

public class LaneView implements LaneObserver, ActionListener {

	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	//JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;

	
	JSlider angle_slider;
	JSlider velocity_slider;
	JLabel emoticon_label;
	JLabel emoticon_text_label;
	JComboBox<Integer> angle_combo;
	JComboBox<Integer> velocity_combo;

	JButton maintenance;
	Lane lane;
	
	public int chance=31;
	public int grid=14;

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

		Factory f = new Factory();
		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][chance];
		ballLabel = new JLabel[numBowlers][chance];
		JPanel[][] scores = new JPanel[numBowlers][grid];
		scoreLabel = new JLabel[numBowlers][grid];
		ballGrid = new JPanel[numBowlers][grid];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != chance; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != grid; j++) {
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
			pins[i].setLayout(new GridLayout(0, grid));
			for (int k = 0; k != grid; k++) {
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

		JButton throw_ball = f.CreateJButton("Throw Ball");
		JPanel buttonPanel = f.CreatePanelWithButton(throw_ball, new FlowLayout(), this);
		
		JPanel angle_slider_panel=new JPanel();
		JPanel angle_slider_label = f.CreatePanelWithLabel("angle");
		//angle_slider = f.CreateSlider(-60, 60, new GridLayout ( 2, 2 ));
		Integer angle_options[]={-60, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60};        
    	angle_combo=new JComboBox(angle_options);    
    	angle_combo.setBounds(50, 50,90,20);    
		//angle_slider_panel.add(angle_slider,BorderLayout.NORTH);
		angle_slider_panel.add(angle_combo, BorderLayout.NORTH);
		angle_slider_panel.add(angle_slider_label,BorderLayout.SOUTH);


		JPanel slidelab2 = f.CreatePanelWithLabel("velocity");
		JPanel velocity_slider_panel = new JPanel();

		//velocity_slider = f.CreateSliderWithMajorTick(0, 50, 10);
		Integer velocity_options[]={0, 10, 20, 30, 40, 50};        
    	velocity_combo=new JComboBox(velocity_options);    
    	//velocity_combo.setBounds(50, 50,90,20);
		velocity_slider_panel.add(velocity_combo,BorderLayout.NORTH);
		velocity_slider_panel.add(slidelab2,BorderLayout.SOUTH);
		
		JPanel final_panel = new JPanel();
		final_panel.add(velocity_slider_panel);
		final_panel.add(angle_slider_panel);
		panel.add(final_panel);

		JPanel emoticon_panel = new JPanel(new BorderLayout());
		emoticon_label = new JLabel();
		emoticon_text_label = new JLabel();
		emoticon_label.setPreferredSize(new Dimension(30, 30));
		emoticon_panel.add(emoticon_label, BorderLayout.CENTER);
		emoticon_panel.add(emoticon_text_label, BorderLayout.NORTH);

		JPanel lane_action_panel = f.CreateJPanel(new FlowLayout());
		JButton pause_lane = f.CreateJButton("Pause Lane");
		JButton resume_lane = f.CreateJButton("Resume Lane");
		JButton save_lane = f.CreateJButton("Save Lane");
		JPanel pause_panel = f.CreatePanelWithButton(pause_lane, new FlowLayout(), this);
		JPanel resume_panel = f.CreatePanelWithButton(resume_lane, new FlowLayout(), this);
		JPanel save_panel = f.CreatePanelWithButton(save_lane, new FlowLayout(), this);
		lane_action_panel.add(pause_panel);
		lane_action_panel.add(resume_panel);
		lane_action_panel.add(save_panel);

		panel.add(lane_action_panel);
		panel.add(buttonPanel);
		panel.add(emoticon_panel);

		initDone = true;
		return panel;
	} 

	public void receivePausedLaneEvent(Party party, Bowler currentThrower,
								 int[] intArgs, LaneScore ls) {
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

	public void receiveLaneEvent(Party party, Bowler currentThrower,
								 int[] intArgs, boolean gameIsHalted, LaneScore ls) {

		if (lane.isPartyAssigned()) {
			int numBowlers = party.getMembers().size();
			while (!initDone) {
				//System.out.println("chillin' here.");
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					System.err.println("Error: " + e);
				}
			}

			if ((intArgs[1] == 1
							&& intArgs[2] == 0
							&& intArgs[0] == 0)) {
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
				for (int i = 0; i < chance-2; i++) {
					int[] temp_scores = ((int[]) ((HashMap) ls.getScoresHashMap()).get(bowlers.get(k)));
					System.out.println("score received " + temp_scores[i]);
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
		if (e.getActionCommand().equals("Throw Ball")) {
			System.out.println("Ball throw initialized");
			try {
			 //System.out.println("Angle "+av+" velocity "+vv);
				lane.start_game((Integer)angle_combo.getItemAt(angle_combo.getSelectedIndex()), 
							(Integer)velocity_combo.getItemAt(velocity_combo.getSelectedIndex()));
			}
			catch (Exception ex) {
				System.out.println("eror in throwing the ball");
			}
		}

		if (e.getSource().equals(maintenance)) {
			lane.pauseGame(true);
		}

		if (e.getActionCommand().equals("Pause Lane")) {
			lane.pauseGame(true);
		}
		if (e.getActionCommand().equals("Resume Lane")) {
			lane.pauseGame(false);
		}

		if(e.getActionCommand().equals("Save Lane")){
			lane.pauseGame(true);
			try {
				lane.saveLane();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void displayEmoticon(int pinsDownThisThrow, int tn, int count, String nick) throws IOException {
		System.out.println("Displaying emoticon");
		ImageIcon icon;

		if(tn == 2) {
			if(count == 10) {
				emoticon_label.setIcon(new ImageIcon(ReadConfig.GetPropValues("victory_emoji"),
					                                 "Congrats You scored " + pinsDownThisThrow));
				emoticon_text_label.setText("Congrats " + nick + " hit all 10 pins.");
			} else {
				emoticon_label.setIcon(new ImageIcon(ReadConfig.GetPropValues("sorry_emoji"),
		                                 "Sorry you could only hit " + count + " pins"));
				emoticon_text_label.setText("Sorry " + nick + " you could only hit " + count + " pins.");
			}
		} else if (tn == 1 && count > 0) {
			if (count > 6) {
				emoticon_label.setIcon(new ImageIcon(ReadConfig.GetPropValues("congrats_emoji"),
					                                 "Congrats You scored " + count));
				emoticon_text_label.setText("Well done!" + nick + " You hit " + count + " pins.");
			} else {
				emoticon_label.setIcon(new ImageIcon(ReadConfig.GetPropValues("sorry_emoji"),
		                                 "Sorry you could only hit " + count + " pins"));
				emoticon_text_label.setText("Sorry " + nick + " you could only hit " + count + " pins.");
			}
		} else if (tn == 1 && count == 0) {
			emoticon_label.setIcon(null);
			emoticon_text_label.setText("");
		}
	}
}
