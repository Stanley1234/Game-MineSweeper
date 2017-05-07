package frame;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import util.SaveFile;

@SuppressWarnings("serial")
public class GameLoseDia extends JDialog implements ActionListener, WindowListener{
	
	
	private GamePanel gPanel;
	
	public GameLoseDia(GamePanel gPanel) {
		this.gPanel = gPanel;
		
		setTitle("Lose Game");
		setLocationRelativeTo(gPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addWindowListener(this);
		initComponents();
		
		setModal(true);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	private void initComponents() {
		Container pane = getContentPane();
		pane.setLayout(new GridLayout(4, 1));
		
		// first - JLabel
		JLabel noteLabel = new JLabel("You are so stupid. You lose the game.");
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BorderLayout());
		notePanel.setBorder(BorderFactory.createEmptyBorder(5, 70, 5, 20));
		notePanel.add(noteLabel, BorderLayout.CENTER);
		
		// second - time
		JLabel timeLabel = new JLabel("time: " + GridFrame.timeElapsed + " secs");
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout());
		timePanel.add(timeLabel, BorderLayout.WEST);
		
		// third - Statistics
		JLabel gamePlayedLabel = new JLabel("You have played " + SaveFile.getGamePlayedTime(GridFrame.mode) + " times");
		JLabel gameWonLabel = new JLabel("You have won " + SaveFile.getWinTime(GridFrame.mode) + " times");
		JLabel percentLabel = new JLabel("Percentage: " + SaveFile.getPercent(GridFrame.mode) + "%");
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(2, 1));
		panel1.add(gamePlayedLabel);
		panel1.add(gameWonLabel);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(percentLabel, BorderLayout.SOUTH);
		
		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setLayout(new GridLayout(1, 2));
		statisticsPanel.add(panel1);
		statisticsPanel.add(panel2);
		
		// fourth - exit / restart / play the same game
		JButton exitButton = new JButton("Exit");
		JButton restartButton = new JButton("Restart the same game");
		JButton replayButton = new JButton("Play another game");
		
		exitButton.addActionListener(this);
		restartButton.addActionListener(this);
		replayButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(exitButton);
		buttonPanel.add(restartButton);
		buttonPanel.add(replayButton);
		
		// add all panels
		pane.add(notePanel);
		pane.add(timePanel);
		pane.add(statisticsPanel);
		pane.add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().toString().indexOf("Exit") != -1) {
			SaveFile.output();
			System.exit(0);
		} else if(e.getSource().toString().indexOf("Restart the same game") != -1) {
			gPanel.playSameGame();
			dispose();
		} else if(e.getSource().toString().indexOf("Play another game") != -1) {
			gPanel.playAnotherGame();
			dispose();
		}
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		gPanel.playAnotherGame();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
