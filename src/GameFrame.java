import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import util.Chess;
import util.ImageLoader;


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
 * 
 * 
 * 
 * */

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements ActionListener, MouseListener{

	private static final String GAME_MENU_NAME = "Game";
	private static final String RESTART_OPTION_NAME = "Restart";
	private static final String STATISTICS_NAME = "Statistics";
	private static final String GAME_OPTION_NAME = "Game Option";
	private static final String EXIT_NAME = "Exit";
	
	private Chess board;
	private GameButton[][] buttonGroup; 
	
	public GameFrame() {
		this.setTitle("MineSweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// set the window to the centre
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Rectangle screen = gd.getDefaultConfiguration().getBounds();
		
		this.setLocation(screen.width / 3, screen.height / 6);
		this.setSize(320, 340);
		
		initComponents();
		
		this.setVisible(true);
	}	
	
	private void initComponents() {
		
		createMenuBar();
		//JPanel configPanel = createConfigPanel();
		JPanel bgPanel = initBottom();
		
		this.setLayout(new BorderLayout());
		//this.add(configPanel, BorderLayout.NORTH);
		this.add(bgPanel, BorderLayout.CENTER);
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
		this.setJMenuBar(menuBar);
	}
	
	private JPanel createConfigPanel() {
		return null;
	}
	
	public JPanel initBottom() {
		final int width = board.getWidth();
		final int height = board.getHeight();
		
		// initialize buttons
		buttonGroup = new GameButton[width][height];
		for(int i = 0;i < width;i ++) {
			for(int j = 0;j < height;j ++) {
				buttonGroup[i][j] = new GameButton(i, j, this);
			}
		}
		
		// add buttons
		JPanel bgPanel = new JPanel();
		
		bgPanel.setLayout(new GridLayout(height, width));
		for(int j = 0;j < height;j ++) {
			for(int i = 0;i < width;i ++) {
				bgPanel.add(buttonGroup[i][j]);
			}
		}
		
		return bgPanel;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object o = e.getSource();
		
		if(o.toString().indexOf(RESTART_OPTION_NAME, 0) != -1) {
			//initComponents();
		} 
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		String[] info = e.getSource().toString().substring("[GButton] ".length()).split(",");
		final int x = Integer.parseInt(info[0]);
		final int y = Integer.parseInt(info[1]);
		
		if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
			board.flag(x, y);
			
		} else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
			board.reveal(x, y);
		}
		
		for(int i = 0;i < board.getWidth();i ++) {
			for(int j = 0;j < board.getHeight();j ++) {
				
				if(board.chess[i][j] == Chess.UNREVEAL)
					buttonGroup[i][j].setIcon(null);       // no icon
				else if(board.chess[i][j] == Chess.FLAG)
					buttonGroup[i][j].setIcon(new ImageIcon(ImageLoader.FLAG));
				else if(board.chess[i][j] == Chess.MINE) {
					buttonGroup[i][j].setEnabled(false);
					buttonGroup[i][j].setText("*");
				} else if(board.chess[i][j] >= 0) {
					buttonGroup[i][j].setEnabled(false);
					buttonGroup[i][j].setBackground(new Color(217, 217, 217));
					
					buttonGroup[i][j].setTextByCount(board.chess[i][j]);
				}
				
				
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
