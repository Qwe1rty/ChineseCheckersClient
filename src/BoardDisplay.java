import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class BoardDisplay {
	
	private JFrame mainWindow;
	private JPanel boardPanel;
	
	private int[][] board;
	
	private static final Dimension BOARD_SIZE = new Dimension(600, 600);
	private static final Dimension WINDOW_SIZE = new Dimension(650, 700);

	public static final ImageIcon PADDING = new ImageIcon("padding.png");
	
	
	public BoardDisplay(int[][] board) {
		this.board = board;
		
		mainWindow = new JFrame("Chinese Checkers");
		
		JPanel mainPanel = new JPanel();
		//mainPanel.setLayout(new GridLayout(2, 1));
		
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(17, 1));
		boardPanel.setMinimumSize(BOARD_SIZE);
		boardPanel.setMaximumSize(BOARD_SIZE);
		paintBoard();
		
		mainPanel.add(boardPanel);
		mainWindow.add(mainPanel);
		
		mainWindow.setSize(WINDOW_SIZE);
		mainWindow.setVisible(true);
	}
	
	private void paintBoard() {
		for (int row = 0; row < 17; row++) {
			JPanel currentRow = new JPanel();
			currentRow.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			for (int numPadding = 16; numPadding > row; numPadding--) {
				JLabel padding = new JLabel(PADDING);
				currentRow.add(padding);
			}
			for (int column = 0; column < 17; column++) {
				JLabel circleLabel = new JLabel(new ImageIcon("Circle" + board[row][column] + ".png"));
				currentRow.add(circleLabel);
			}
			boardPanel.add(currentRow);
		}
	}
	
	public void refresh() {
		System.out.println("refresh");
		boardPanel.removeAll();
		paintBoard();
		boardPanel.revalidate();
		boardPanel.repaint();
	}

}
