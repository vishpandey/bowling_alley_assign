package search;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bowler.BowlerFile;
import score.ScoreHistoryFile;
import views.Factory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;


public class SearchView implements ActionListener, ListSelectionListener {
	private final JFrame win;
    private Vector bowlers_list_vector;
    private final JList result_list;
    private final JList bowlers_list;
    private String selected_bowler;


    public SearchView() {
    	Factory f = new Factory();

    	win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);


        JPanel colPanel = f.CreateJPanel(new GridLayout(1, 3));
        JPanel partyPanel = f.CreateJPanel(new FlowLayout(), new TitledBorder("Query Result"));
    
        result_list = f.CreateEmptyJList(200, 6, this);

        JScrollPane partyPane = new JScrollPane(result_list);
        partyPanel.add(partyPane);

        JPanel bowlerPanel = f.CreateJPanel(new FlowLayout(), new TitledBorder("Bowler List"));
    	
    	bowlers_list_vector = readBowlersDb();

    	bowlers_list = f.CreateJList(bowlers_list_vector, 120, 8, this);

    	JScrollPane bowlerPane = f.CreateJScrollPane(bowlers_list, "vertical");

    	bowlerPanel.add(bowlerPane);

    	JPanel buttonPanel = f.CreateJPanel(new GridLayout(5, 1));

    	JPanel addPatronPanel = f.CreatePanelWithButton("Highest score", this);
    	JPanel remPatronPanel = f.CreatePanelWithButton("Lowest score", this);
		JPanel averagePanel = f.CreatePanelWithButton("Average score", this);
		JPanel newPatronPanel = f.CreatePanelWithButton("Overall High", this);
		JPanel finishedPanel = f.CreatePanelWithButton("Overall Low", this);
    	
		JPanel[] childPanels = new JPanel[]{addPatronPanel, remPatronPanel, 
    						newPatronPanel, finishedPanel, averagePanel};
    	f.AddPanelsToParentPanel(buttonPanel, childPanels);

		childPanels = new JPanel[]{partyPanel, bowlerPanel, buttonPanel};
		f.AddPanelsToParentPanel(colPanel, childPanels);

        win.getContentPane().add("Center", colPanel);

        win.pack();


        f.centreFrame(win);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    	Vector<String> out = new Vector<>();
    	if (e.getActionCommand().equals("Highest score")) {
    		if(selected_bowler == null) {
    			return;
    		} 

    		int score = 0;
			try {
				score = ScoreHistoryFile.getHighestScore(selected_bowler);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		out.add("Highest Individual Score:");
    		out.add("Player: " + selected_bowler);
    		out.add("Score: " + score);
            result_list.setListData(out);
    	} else if (e.getActionCommand().equals("Lowest score")) {
    		if(selected_bowler == null) {
    			return;
    		}

    		int score = 0;
			try {
				score = ScoreHistoryFile.getLowestScore(selected_bowler);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		out.add("Lowest Individual Score:");
    		out.add("Player: " + selected_bowler);
    		out.add("Score: " + score);
            result_list.setListData(out);
    	} else if (e.getActionCommand().equals("Overall High")) {
    		Vector highest = new Vector<String>();
			try {
				highest = ScoreHistoryFile.getOverallHigh();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
    		out.add("Highest Score:");
    		out.add("Player: " + highest.get(0));
    		out.add("Score: " + highest.get(1));
            result_list.setListData(out);
    	} else if (e.getActionCommand().equals("Overall Low")) {
    		Vector lowest = new Vector<String>();
			try {
				lowest = ScoreHistoryFile.getOverallLow();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		out.add("Lowest Score:");
    		out.add("Player: " + lowest.get(0));
    		out.add("Score: " + lowest.get(1));
            result_list.setListData(out);
    	} else if (e.getActionCommand().equals("Average score")) {
    		if(selected_bowler == null) {
    			return;
    		}
    		int score = 0;
			try {
				score = ScoreHistoryFile.getAverageScore(selected_bowler);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		out.add("Average Individual Score:");
    		out.add("Player: " + selected_bowler);
    		out.add("Score: " + score);
            result_list.setListData(out);
    	}
    }


    private Vector readBowlersDb() {
    	try {
            bowlers_list_vector = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlers_list_vector = new Vector();
        }

        return bowlers_list_vector;
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(bowlers_list)) {
            selected_bowler = ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }
}