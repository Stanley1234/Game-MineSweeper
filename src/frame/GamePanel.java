package frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.Chess;
import util.ImageLoader;
import util.SaveFile;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener{

	/*
	 * each squre is 10 x 10 each line is 10 x 1
	 * 
	 * The game width = 10 * mn + 1 * (mn - 1) The game height = 10 * mn + 1 *
	 * (mn - 1)
	 * 
	 * border: The left and right border is height * 2 The upper and lower
	 * border is 2 * width
	 * 
	 */

	private static final int SQUARE_LEN = 25;
	private static final int LINE_THICKNESS = 2;

	private static final int BORDER_THICKNESS = 3;

	private int boardWidth, boardHeight;
	private int gameWidth, gameHeight;
	private int totalWidth, totalHeight;

	// double buffering
	private Graphics dbg;
	private Image dbImage;
	
	// chess
	private Chess board;
	private GridFrame gFrame;
	
	// hightline effect
	private boolean isHighlighted = false;
	private int hx, hy;

	public GamePanel(Chess board, GridFrame gFrame) {
		this.boardWidth = board.getWidth();
		this.boardHeight = board.getHeight();
		this.board = board;
		this.gFrame = gFrame;

		gameWidth = SQUARE_LEN * boardWidth + LINE_THICKNESS * (boardWidth - 1);
		gameHeight = SQUARE_LEN * boardHeight + LINE_THICKNESS * (boardHeight - 1);

		totalWidth = gameWidth + BORDER_THICKNESS * 2;
		totalHeight = gameHeight + BORDER_THICKNESS * 2;
		
		setPreferredSize(new Dimension(totalWidth, totalHeight));
		setFocusable(true);
		requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (dbImage == null) {
			dbImage = this.createImage(totalWidth, totalHeight); // create the
																	// buffer

			if (dbImage == null) {
				System.err.println("Image is null!");
				return;
			} else
				dbg = dbImage.getGraphics();
		}
		// clear the background
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, totalWidth, totalHeight);
		dbg.setColor(Color.BLACK);

		// draw each square
		Font numberFont = new Font("Serif", Font.BOLD, 22);
		for (int x = 0; x < boardWidth; x++) {
			for (int y = 0; y < boardHeight; y++) {

				final int x_begin = BORDER_THICKNESS + x * SQUARE_LEN + x * LINE_THICKNESS;
				final int y_begin = BORDER_THICKNESS + y * SQUARE_LEN + y * LINE_THICKNESS;
				
				if(board.chess[x][y] == Chess.FLAG) {
					dbg.setColor(new Color(207, 207, 207));
					dbg.fillRect(x_begin, y_begin, SQUARE_LEN, SQUARE_LEN);
					dbg.drawImage(ImageLoader.FLAG, x_begin, y_begin, null);
					
					
				} else if(board.chess[x][y] == Chess.MINE) {
					dbg.setColor(new Color(207, 207, 207));
					dbg.fillRect(x_begin, y_begin, SQUARE_LEN, SQUARE_LEN);
					dbg.drawImage(ImageLoader.MINE, x_begin, y_begin, null);
					
				} else if(board.chess[x][y] >= 0) {
					
					dbg.setColor(new Color(207, 207, 207));
					dbg.fillRect(x_begin, y_begin, SQUARE_LEN, SQUARE_LEN);
					
					Color color = null;
					if(board.chess[x][y] == 1)  // medium blue
						color = new Color(57, 71, 241);
					else if(board.chess[x][y] == 2) // green
						color = new Color(57, 160, 69);
					else if(board.chess[x][y] == 3)  // dark red
						color = new Color(193, 72, 69);
					else if(board.chess[x][y] == 4)  // dark blue
						color = new Color(57, 72, 127);
					else if(board.chess[x][y] >= 5)  // purple
						color = new Color(101, 28, 127);
					dbg.setColor(color);
					dbg.setFont(numberFont);
					dbg.drawString(board.chess[x][y] + "", x_begin + SQUARE_LEN / 4,
							y_begin + SQUARE_LEN / 4 * 3);
					
					
				} else if(board.chess[x][y] == Chess.UNREVEAL) {
					// draw the square - light blue
					if(isHighlighted && hx == x && hy == y) {
						dbg.setColor(new Color(158, 255, 255));
						isHighlighted = false;
					} else
						dbg.setColor(new Color(122, 154, 250));
					
					
					
					dbg.fillRect(x_begin, y_begin, SQUARE_LEN, SQUARE_LEN);
				}
			}
		}
		// draw gap line
		dbg.setColor(Color.BLACK);
		for (int x = 0; x < boardWidth; x++)
			for (int y = 0; y < boardHeight; y++) {

				final int x_begin = BORDER_THICKNESS + x * SQUARE_LEN + x * LINE_THICKNESS;;
				final int y_begin = BORDER_THICKNESS + y * SQUARE_LEN + y * LINE_THICKNESS;

				// draw the right gap line
				
				if (x < boardWidth - 1) {	
					dbg.fillRect(x_begin + SQUARE_LEN, y_begin, LINE_THICKNESS, SQUARE_LEN);
				}

				// draw the bottom gap line
				if (y < boardHeight - 1) {
					dbg.fillRect(x_begin, y_begin + SQUARE_LEN, SQUARE_LEN, LINE_THICKNESS);
				}

			}
		
		
		// draw the upper borderline
		dbg.fillRect(0, 0, totalWidth, BORDER_THICKNESS);

		// draw the lower borderline
		dbg.fillRect(0, totalHeight - BORDER_THICKNESS, totalWidth, BORDER_THICKNESS);

		// draw the left borderline
		dbg.fillRect(0, 0, BORDER_THICKNESS, totalHeight);

		// draw the right borderline
		dbg.fillRect(totalWidth - BORDER_THICKNESS, 0, BORDER_THICKNESS, totalHeight);

		g.drawImage(dbImage, 0, 0, null);
	}
	
	
	/*
	 * drawnBySpecificBoard() is usually called when drawing a new board
	 * */
	public void drawnBySpecificBoard() {
		repaint();
	}

	/*
	 * playAnotherGame() starts a new game
	 * */
	public void playAnotherGame() {
		board.createChess();
		gFrame.setTimeToZero();
		repaint();
	}
	
	public void playSameGame() {
		board.backToBeginning();
		gFrame.setTimeToZero();
		repaint();
	}
	
	public void resizePanel(final int width, final int height) {
		boardWidth = width;
		boardHeight = height;
		gameWidth = SQUARE_LEN * boardWidth + LINE_THICKNESS * (boardWidth - 1);
		gameHeight = SQUARE_LEN * boardHeight + LINE_THICKNESS * (boardHeight - 1);

		totalWidth = gameWidth + BORDER_THICKNESS * 2;
		totalHeight = gameHeight + BORDER_THICKNESS * 2;
		setPreferredSize(new Dimension(totalWidth, totalHeight));
		dbImage = null;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(!gFrame.isStartGame()) {
			gFrame.gameStart();
		}
		
		
		int cx = e.getX();
		int cy = e.getY();
		
		cx -= BORDER_THICKNESS;
		cy -= BORDER_THICKNESS;
		
		final int xth = cx / (SQUARE_LEN + LINE_THICKNESS);
		final int yth = cy / (SQUARE_LEN + LINE_THICKNESS);
		
		if(SwingUtilities.isRightMouseButton(e)) {
			board.flag(xth, yth);
			gFrame.updateMineLeft();
		} else if(SwingUtilities.isLeftMouseButton(e)) {
			board.reveal(xth, yth);
		}
		repaint();
		
		if(board.gameWin()) {
			gFrame.gameStop();
			SaveFile.updateWinning(gFrame.mode);
			JDialog winDia = new GameWinDia(this);
		} else if(board.gameLose()) {
			gFrame.gameStop();
			SaveFile.updateLosing(gFrame.mode);
			JDialog loseDia = new GameLoseDia(this);
		}
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int cx = e.getX();
		int cy = e.getY();
		
		cx -= BORDER_THICKNESS;
		cy -= BORDER_THICKNESS;
		
		hx = cx / (SQUARE_LEN + LINE_THICKNESS);
		hy = cy / (SQUARE_LEN + LINE_THICKNESS);
		
		isHighlighted = true;
		repaint();
	}
}
