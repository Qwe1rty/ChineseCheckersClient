
public class Algorithm {

	// tinyurl.com/chinesecheckersprotocol
	
	private final static int DEPTH = 1;
	
	// If true, we include moves that move backwards
	private boolean isEndgame;
	
	// Target space that the algorithm tries to move pieces to
	private int targetRow;
	private int targetColumn;
	
	// Color of our player; determines which direction pieces go to
	private int color;
	
	public Algorithm(int color) {
		isEndgame = false;
		
		this.color = color;
	}
	
	// current row, current column, new row, new column
	public int[] nextMove(int[][] board) {
		return new int[3];
		
	}
	
	private int findDistance(int startRow, int startCol, int endRow, int endCol) {
		return (endRow - startRow) * (endRow - startRow) + (endCol - startCol) * (endCol - startCol); 
	}
	
	private void findTarget(int[][] board) {
		// Colors 3 (yellow) and 6 (purple) are hardcoded because the coordinate system
		// doesn't really allow for any decent way of searching for farthest position
		if (color == 1) { //  Red, bottom
			for (int row = 0; row < 17; row++) {
				for (int col = 0; col < 17; col++) {
					if (board[row][col] == 0) {
						targetRow = row;
						targetColumn = col;
					}
				}
			}
		} else if (color == 2) { // Orange, bottom left
			for (int col = 0; col < 17; col++) {
				for (int row = 0; row < 17; row++) {
					if (board[row][col] == 0) {
						targetRow = row;
						targetColumn = col;
					}
				}
			}
		} else if (color == 3) { // Yellow, top right
			
		} else if (color == 4) { // Green, top
			for (int row = 16; row >= 0; row--) {
				for (int col = 0; col < 17; col++) {
					if (board[row][col] == 0) {
						targetRow = row;
						targetColumn = col;
					}
				}
			}
		} else if (color == 5) { // Blue, top left
			for (int col = 16; col >= 0; col--) {
				for (int row = 0; row < 17; row++) {
					if (board[row][col] == 0) {
						targetRow = row;
						targetColumn = col;
					}
				}
			}
		} else if (color == 6) { // Purple, bottom left
			
		} else System.out.println("you dun goofed, son");
	}
	
}
