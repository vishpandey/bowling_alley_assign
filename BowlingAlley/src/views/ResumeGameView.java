package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controlDesk.ControlDesk;
import lane.Lane;
import party.Party;
import bowler.Bowler;

public class ResumeGameView implements ActionListener, ListSelectionListener {
	private final JFrame win;
    private Vector partydb, paused_lanes, saved_parties_vector, selected_game;
    private final JList parties_list;
    ControlDesk controlDesk;
	private Vector paused_parties_vector;


    public ResumeGameView(ControlDesk controlDesk) {
        
        this.controlDesk = controlDesk;
        
        Factory f = new Factory();

        win = new JFrame("Resume Game");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);
        

        JPanel colPanel = f.CreateJPanel(new GridLayout(1, 2));

        JPanel saved_games_panel = f.CreateJPanel(new FlowLayout(), new TitledBorder("Game List"));

        paused_lanes = Lane.getPausedGames();
        saved_parties_vector = new Vector<>();
        Vector valid_saved_parties_vector = new Vector<>();
        for (Object paused_lane: paused_lanes)
        {
            Vector v = new Vector<>();
            Vector paused_lane_vector =  (Vector) paused_lane;
            Party paused_lane_party = (Party) paused_lane_vector.get(6); 
            for (Object bowler: paused_lane_party.getMembers()) {
            	String nickname = ((Bowler) bowler).getNick();
                v.add(nickname);
            }
            saved_parties_vector.add(v);

            if ((Integer)(((Vector) paused_lane).get(0)) == 1)
                valid_saved_parties_vector.add(v);
        }

        parties_list = f.CreateJList(valid_saved_parties_vector, 120, 8, this);
        
        JScrollPane valid_parties_pane = f.CreateJScrollPane(parties_list, "vertical");
        
        saved_games_panel.add(valid_parties_pane);
        
        JPanel buttons_panel = f.CreateJPanel(new GridLayout(1, 1));

        JButton resume_button = f.CreateJButton("Resume Game");
        JPanel resume_button_panel = f.CreatePanelWithButton(resume_button, this);
        buttons_panel.add(resume_button_panel);

        colPanel.add(saved_games_panel);
        colPanel.add(buttons_panel);
        
        win.getContentPane().add("Center", colPanel);

        win.pack();

        centre_window(win);
    }

    private void centre_window(JFrame win) {
    	Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Resume Game")) {
            int index = (saved_parties_vector.indexOf(selected_game));
            Lane.resumePausedLane(index);

            Vector selected_game_vector = (Vector)paused_lanes.get(index);
            controlDesk.resume_paused_game(selected_game_vector);
            win.setVisible(false);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(parties_list)) {
            JList temp_list = (JList) e.getSource();
            selected_game = ((Vector)((temp_list).getSelectedValue()));
        }
    }
}