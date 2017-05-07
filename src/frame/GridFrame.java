package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import util.Chess;
import util.Constant;
import util.ImageLoader;
import util.SaveFile;

/*
 * *** JMenu ***** 
 * New Game
 * ------------------
 * Statistics - Configuration
 * Optional Panel - easy, medium, lunatic, customize
 * 
 * ------------------
 * 
 * *** Panel *****
 * Time   Restart   Mine_Left_Num
 * *** Game panel ***
 * 
 * */

@SuppressWarnings("serial")
public class GridFrame extends JFrame implements ActionListener {

	private static final String GAME_MENU_NAME = "Game";
	private static final String RESTART_OPTION_NAME = "Restart";
	private static final String STATISTICS_NAME = "Statistics";
	private static final String GAME_OPTION_NAME = "Game Option";
	private static final String EXIT_NAME = "Exit";

	private static final int EMPTY_BORDER = 30;
	private static final int EMPTY_BOTTOM_BORDER = 10;

	// board
	private Chess board;
	public static final int EASY = 0;
	public static final int NORMAL = 1;
	public static final int LUNATIC = 2;
	public static final int CUSTOMIZE = 3;
	public static int mode;

	// components related 
	private GamePanel gamePanel;
	private JLabel mineLeftLabel;
	private JLabel timeLabel;
	private boolean isStartGame = false;
	public static int timeElapsed;

	public GridFrame() {
		setTitle("MineSweeper");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set the window to the centre
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Rectangle screen = gd.getDefaultConfiguration().getBounds();
		setLocation(screen.width / 3, screen.height / 6);

		initChess();
		initComponents();

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// only output the record when the program is over
				// this is much more efficient when having a large amount of
				// data
				SaveFile.output();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		setResizable(false);
		pack();

		setVisible(true);
	}

	private void initChess() {
		board = new Chess();
		board.createChess(Constant.EASY_WIDTH, Constant.EASY_HEIGHT, Constant.EASY_MINE);
		mode = EASY;
	}

	private void initComponents() {
		createMenuBar();

		// two labels - time label, mine left label
		mineLeftLabel = new JLabel("Mine left: " + board.flagNumLeft() + "");
		mineLeftLabel.setFont(new Font("Serif", Font.BOLD, 22));
		mineLeftLabel.setForeground(Color.BLUE);

		timeLabel = new JLabel("Time Elapsed: 0");
		timeLabel.setFont(new Font("Serif", Font.BOLD, 22));
		timeLabel.setForeground(Color.BLUE);
		Timer timer = new Timer("Display Timer");
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// Task to be executed every second
				if (!isStartGame)
					return;

				timeLabel.setText("Time Elapsed: " + timeElapsed + "");
				timeElapsed++;
			}
		};
		// This will invoke the timer every second
		timer.scheduleAtFixedRate(task, 0, 1000);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(mineLeftLabel, BorderLayout.WEST);
		infoPanel.add(timeLabel, BorderLayout.EAST);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		infoPanel.setOpaque(false);

		// game panel
		gamePanel = new GamePanel(board, this);
		JPanel gamePanelContainer = new JPanel();
		gamePanelContainer.setLayout(new BorderLayout());
		gamePanelContainer.add(gamePanel, BorderLayout.CENTER);
		gamePanelContainer.setBorder(
				BorderFactory.createEmptyBorder(EMPTY_BORDER, EMPTY_BORDER, EMPTY_BOTTOM_BORDER, EMPTY_BORDER));
		gamePanelContainer.setOpaque(false);

		// manipulation panel
		JPanel manipulatePanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				g.drawImage(ImageLoader.BACKGROUND, 0, 0, null);
			};
		};
		manipulatePanel.setLayout(new BorderLayout());
		manipulatePanel.add(gamePanelContainer, BorderLayout.CENTER);
		manipulatePanel.add(infoPanel, BorderLayout.SOUTH);

		// add all the panel
		setContentPane(manipulatePanel);
	}

	private void createMenuBar() {
		// initialize the menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu(GAME_MENU_NAME);

		JMenuItem restartOption = new JMenuItem(RESTART_OPTION_NAME);
		JMenuItem statistics = new JMenuItem(STATISTICS_NAME);
		JMenuItem gameOption = new JMenuItem(GAME_OPTION_NAME);
		JMenuItem exitOption = new JMenuItem(EXIT_NAME);

		restartOption.addActionListener(this);
		statistics.addActionListener(this);
		gameOption.addActionListener(this);
		exitOption.addActionListener(this);

		gameMenu.add(restartOption);
		gameMenu.addSeparator();
		gameMenu.add(statistics);
		gameMenu.add(gameOption);
		gameMenu.addSeparator();
		gameMenu.add(exitOption);

		menuBar.add(gameMenu);
		setJMenuBar(menuBar);
	}

	public void updateMineLeft() {
		mineLeftLabel.setText("Mine left: " + board.flagNumLeft() + "");
	}

	public void gameStart() {
		timeElapsed = 0;
		isStartGame = true;
	}

	public void gameStop() {
		isStartGame = false;
	}

	public int getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeToZero() {
		timeElapsed = 0;
		timeLabel.setText("Time Elapsed: 0");
	}

	public boolean isStartGame() {
		return isStartGame;
	}

	// resize() is called in GameOptionDia
	public void resizeFrame(final int width, final int height, final int mineNum) {
		// unchanged, then no resize occurs, only start a new game
		if (width == board.getWidth() && height == board.getHeight() && mineNum == board.getMineNum()) {
			gamePanel.playAnotherGame();
			return;
		}

		setResizable(true);
		gameStop();
		setTimeToZero();

		board.createChess(width, height, mineNum);
		gamePanel.resizePanel(width, height);
		updateMineLeft();
		
		pack();
		setResizable(false);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object o = e.getSource();

		if (o.toString().indexOf(RESTART_OPTION_NAME, 0) != -1) {
			board.createChess();
			gamePanel.drawnBySpecificBoard();
			setTimeToZero();
		} else if (o.toString().indexOf(EXIT_NAME) != -1) {
			SaveFile.output();
			System.exit(0);
		} else if (o.toString().indexOf(STATISTICS_NAME) != -1) {
			StatisticsDia sDia = new StatisticsDia(this);
		} else if (o.toString().indexOf(GAME_OPTION_NAME) != -1) {
			GameOptionDia gDia = new GameOptionDia(this);
		}

	}

}
