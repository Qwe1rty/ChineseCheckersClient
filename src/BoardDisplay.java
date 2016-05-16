import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class BoardDisplay {
	
	private JFrame mainWindow;
	private JPanel boardPanel, playerPanel;
	
	private int[][] board;
	
	private static final Dimension BOARD_SIZE = new Dimension(600, 600);
	private static final Dimension WINDOW_SIZE = new Dimension(650, 750);
	
	private static final int FONT_SIZE = 24;
	public static final Font DEFAULT_FONT = new Font("Sans Serif", Font.BOLD, FONT_SIZE);

	public static final ImageIcon PADDING = new ImageIcon("resources/padding.png");
	
	public static final String[] PLAYER_COLOURS = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple" };
	
	public BoardDisplay(int[][] board) {
		this.board = board;
		
		mainWindow = new JFrame("Chinese Checkers");
		
		JPanel mainPanel = new JPanel();
		
		playerPanel = new JPanel();
		JLabel playerLabel = new JLabel("Player: ");
		UIManager.put("Label.font", DEFAULT_FONT);
		//playerPanel.setSize(100, 700);
		
		playerPanel.add(playerLabel);
		mainPanel.add(playerPanel);
		
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(Board.NUM_ROWS, 1));
		boardPanel.setSize(BOARD_SIZE);
		boardPanel.setMinimumSize(BOARD_SIZE);
		boardPanel.setMaximumSize(BOARD_SIZE);
		paintBoard();

		mainPanel.add(boardPanel);
		mainWindow.add(mainPanel);
		
		mainWindow.setSize(WINDOW_SIZE);
		mainWindow.setVisible(true);
	}
	
	private void paintBoard() {
		for (int row = 0; row < Board.NUM_ROWS; row++) {
			JPanel currentRow = new JPanel();
			currentRow.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			for (int numPadding = 16; numPadding > row; numPadding--) {
				JLabel padding = new JLabel(PADDING);
				currentRow.add(padding);
			}
			for (int column = 0; column < Board.NUM_COLUMNS; column++) {
				JLabel circleLabel = new JLabel(new ImageIcon("resources/Circle" + board[row][column] + ".png"));
				currentRow.add(circleLabel);
			}
			boardPanel.add(currentRow);
		}
	}
	
	public void setPlayer(int playerNumber) {
		playerPanel.removeAll();
		JLabel newPlayerLabel = new JLabel("Player " + playerNumber + ": " + PLAYER_COLOURS[playerNumber - 1]);
		playerPanel.add(newPlayerLabel);
		playerPanel.revalidate();
		playerPanel.repaint();	
	}

	public void declareWinner(int playerNumber, int currentPlayer) {
		playerPanel.removeAll();
		JLabel newPlayerLabel;
		if (playerNumber == currentPlayer) {
			newPlayerLabel = new JLabel("Victory! Player " + playerNumber + " (" + 
					PLAYER_COLOURS[playerNumber - 1] + ") wins!");
		}
		else {
			newPlayerLabel = new JLabel("Defeat! Player " + playerNumber + " (" + 
					PLAYER_COLOURS[playerNumber - 1] + ") wins!");
		}
		playerPanel.add(newPlayerLabel);
		playerPanel.revalidate();
		playerPanel.repaint();	
	}
	
	public void refresh() {
		//System.out.println("refresh");
		boardPanel.removeAll();
		paintBoard();
		boardPanel.revalidate();
		boardPanel.repaint();
	}
	

}
