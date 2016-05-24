import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/** The Board Display Class
 *  Handles the display for the current state of the Chinese Checkers board
 *  @author Darren Chan
 *	@version May 17, 2016
 */

public class BoardDisplay {
	
	private JFrame mainWindow;
	private JPanel boardPanel, playerPanel, turnPanel;
	private JLabel turnLabel;
	
	private int[][] board;
	
	private static final Dimension BOARD_SIZE = new Dimension(600, 600);
	private static final Dimension WINDOW_SIZE = new Dimension(650, 750);
	
	private static final int FONT_SIZE = 24;
	public static final Font DEFAULT_FONT = new Font("Sans Serif", Font.BOLD, FONT_SIZE);

	public static final ImageIcon PADDING = new ImageIcon("resources/padding.png");
	
	public static final String[] PLAYER_COLOURS = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple" };
	
	/** Creates and sets up the board display
	 *  Precondition: board is an initialized 2D array of integers
	 *  Postcondition: a new window has been created displaying the current state of the board
	 *  @param board the board to make the display of
	 */
	public BoardDisplay(int[][] board) {
		this.board = board;
		
		mainWindow = new JFrame("Chinese Checkers");
		
		JPanel mainPanel = new JPanel();
		
		playerPanel = new JPanel();
		UIManager.put("Label.font", DEFAULT_FONT);
		turnPanel = new JPanel();
		JLabel playerLabel = new JLabel("Player:     ");
		turnLabel = new JLabel("Turn: ");
		//playerPanel.setSize(100, 700);
		
		playerPanel.add(playerLabel);
		turnPanel.add(turnLabel);
		playerPanel.add(turnPanel);
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
	
	/** Paints boardPanel with the current state of the board
	 *  Precondition: All the padding and circle images are in the resources folder, and board has been 
	 *  initialized as a 2D array
	 *  Postcondition: the boardPanel has been drawn with the current state of the board
	 */
	private void paintBoard() {
		for (int row = 0; row < Board.NUM_ROWS; row++) {
			JPanel currentRow = new JPanel();
			currentRow.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			
			// Put in padding to make the board display correctly
			for (int numPadding = 16; numPadding > row; numPadding--) {
				JLabel padding = new JLabel(PADDING);
				currentRow.add(padding);
			}
			
			// Place the actual circles to represent the pieces
			for (int column = 0; column < Board.NUM_COLUMNS; column++) {
				JLabel circleLabel = new JLabel(new ImageIcon("resources/Circle" + board[row][column] + ".png"));
				currentRow.add(circleLabel);
			}
			boardPanel.add(currentRow);
		}
	}
	
	/** Displays the current player
	 *  Precondition: playerNumber is a valid integer and player number (0 < playerNumber <= PLAYER_COLOURS.size)
	 *  Postcondition: the current player (playerNumber) is now displayed at the top of the window
	 *  @param playerNumber the number of the player that the client is currently playing as
	 */
	public void setPlayer(int playerNumber) {
		playerPanel.removeAll();
		JLabel newPlayerLabel = new JLabel("Player " + playerNumber + ": " + PLAYER_COLOURS[playerNumber - 1] + "   ");
		playerPanel.add(newPlayerLabel);
		playerPanel.revalidate();
		playerPanel.repaint();	
	}
	
	/** Displays the current turn
	 *  @param turn 
	 */
	public void setTurn(int turn) {
		turnPanel.removeAll();
		turnLabel = new JLabel("Turn: " + turn);
		turnPanel.add(turnLabel);
		turnLabel.revalidate();
		turnLabel.repaint();	
	}

	/** Displays that a player has won and whether or not this player (the client) has won
	 *  Precondition: playerNumber and currentPlayer are valid integers corresponding to a valid
	 *  player number (greater than 0 and less than or equal to PLAYER_COLOURS.size)
	 *  Postcondition: The winner and whether or not the client won is now displayed at the top
	 *  of the window
	 *  @param playerNumber the number of the player that won
	 *  @param currentPlayer the number of the player that the client is playing as
	 */
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
	
	/** Refreshes the board to display the current state
	 *  Precondition: All the padding and circle images are in the resources folder, and board has been 
	 *  initialized as a 2D array
	 *  Postcondition: the board window has been refreshed and now displays the current state of the board
	 */
	public void refresh() {
		//System.out.println("refresh");
		boardPanel.removeAll();
		paintBoard();
		boardPanel.revalidate();
		boardPanel.repaint();
	}
	

}
