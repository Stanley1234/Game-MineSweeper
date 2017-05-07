package frame;

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

import util.SaveFile;

@SuppressWarnings("serial")
public class GameWinDia extends JDialog implements ActionListener, WindowListener{

	private GamePanel gPanel;

	public GameWinDia(GamePanel gPanel) {
		this.gPanel = gPanel;

		setTitle("Win the Game");
		setLocationRelativeTo(gPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		addWindowListener(this);
		initComponents();

		setModal(true);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	public void initComponents() {
		// first - note
		JLabel noteLabel = new JLabel("Congratulation! You win the Game.");
		
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BorderLayout());
		notePanel.add(noteLabel, BorderLayout.CENTER);
		notePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		
		// second - two buttons
		JButton exitButton = new JButton("Exit");
		JButton replayButton = new JButton("Play another game");
		exitButton.addActionListener(this);
		replayButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(exitButton);
		buttonPanel.add(replayButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 5, 20, 5));
		
		// add panels to the game
		Container c = getContentPane();
		c.setLayout(new GridLayout(2, 1));
		c.add(notePanel);
		c.add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().toString().indexOf("Exit") != -1) {
			SaveFile.output();
			System.exit(0);
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
