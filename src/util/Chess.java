package util;
import java.awt.Point;

import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;

public class Chess {

	public static final int UNREVEAL = -4;
	public static final int MINE = -1;
	public static final int FLAG = -2;

	private Point mines[];
	public int chess[][];
	private int width;
	private int height;
	private int mineNum;

	public Chess() {}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMineNum() {
		return mineNum;
	}
	
	public void setMineNum(final int mineNum) {
		this.mineNum = mineNum;
	}

	public void setChessSize(final int width, final int height) {
		this.width = width;
		this.height = height;

	}

	public void createChess() {
		chess = new int[width][height];

		// fill in empty
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				chess[j][i] = UNREVEAL;

		// randomly fill mine
		mines = new Point[mineNum];
		for (int i = 0; i < mineNum; i++) {
			mines[i] = new Point();
			while (true) {
				final int rx = (int) (Math.random() * width);
				final int ry = (int) (Math.random() * height);

				// check if the mine has already existed
				int ok = 1;
				for (int j = 0; j < i; j++) {
					if (mines[j].x == rx && mines[j].y == ry)
						ok = 0;
				}
				if (ok == 1) {
					mines[i].x = rx;
					mines[i].y = ry;
					break;
				}
			}
		}
	}
	
	public void createChess(final int width, final int height, final int mineNum) {
		setChessSize(width, height);
		setMineNum(mineNum);
		createChess();
	}
	
	// backToBeginning() uses the same board except all revealed flags are back to the beginning
	public void backToBeginning() {
		for(int x = 0;x < getWidth();x ++) {
			for(int y = 0;y < getHeight();y ++) {
				chess[x][y] = UNREVEAL;
			}
		}
	}

	public void flag(final int x, final int y) {

		if (chess[x][y] == MINE || chess[x][y] >= 0)
			return;

		if (chess[x][y] == FLAG)
			chess[x][y] = UNREVEAL;
		else
			chess[x][y] = FLAG;
	}

	public void reveal(final int x, final int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;

		if (chess[x][y] == FLAG || chess[x][y] == MINE || chess[x][y] >= 0)
			return;

		// determine if (x, y) is a mine
		for (int k = 0; k < mineNum; k++) {
			if (mines[k].x == x && mines[k].y == y) {
				chess[x][y] = MINE;
				return;
			}
		}

		// determine the number of adjacent mine
		int adj_mine_num = 0;
		for (int k = 0; k < mineNum; k++) {
			if ((x - 1 <= mines[k].x && mines[k].x <= x + 1) && (y - 1 <= mines[k].y && mines[k].y <= y + 1))
				adj_mine_num++;
		}
		chess[x][y] = adj_mine_num;

		if (adj_mine_num == 0) {
			reveal(x - 1, y);
			reveal(x + 1, y);

			reveal(x - 1, y - 1);
			reveal(x, y - 1);
			reveal(x + 1, y - 1);

			reveal(x - 1, y + 1);
			reveal(x, y + 1);
			reveal(x + 1, y + 1);

		}
	}

	public boolean gameWin() {

		// if any mine is revealed
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (chess[x][y] == MINE) {
					return false;
				}
			}
		}

		// if all tiles with no mines are revealed
		int unknown_num = 0;
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (chess[x][y] == MINE)
					continue;
				if(chess[x][y] == UNREVEAL || chess[x][y] == FLAG)
					unknown_num ++;

			}
		}
		if(unknown_num == mineNum)
			return true;
		return false;
	}

	public boolean gameLose() {
		// if any mine is revealed
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (chess[x][y] == MINE) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int flagNumLeft() {
		
		int flagNum = 0;
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (chess[x][y] == FLAG) {
					flagNum ++;
				}
			}
		}
		return mineNum - flagNum;
	}

}
