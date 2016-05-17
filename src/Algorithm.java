import java.util.ArrayList;

/**
 * A class that will calculate the optimal move to make in a Chinese Checkers game,
 * given the board state.
 * 
 * @author Caleb Choi
 */
public class Algorithm {

	// tinyurl.com/chinesecheckersprotocol
	
	private final static int DEPTH = 1; // Please dont make this less than 1
	
	// If true, we include moves that move backwards
	private boolean isEndgame;
	
	// Target space that the algorithm tries to move pieces to
	private int targetRow;
	private int targetColumn;
	
	// Color of our player; determines which direction pieces go to
	private int color;
	
	/**
	 * Creates an algorithm class for a game
	 * @param color The number representing the player
	 */
	public Algorithm(int color) {
		isEndgame = false;
		
		this.color = color;
	}
	
	
	public int[] nextMove(Board board) {
		
		// Stores the length of the distance travelled
		int[] bestMove = {0, 0, -100, -100};
		
		// Iterates through all pieces
		for (int row = 0; row < 17; row++) {
			for (int col = 0; col < 17; col++) {
				
				// If it finds a piece that is yours
				if (board.getBoard()[row][col] == color) {
					
					// Stores the current position in the first arraylist slot
					ArrayList<Integer[]> moveList = new ArrayList<Integer[]>();
					Integer[] currentPos = {row, col};
					moveList.add(currentPos);
					
					// DFS search
					moveList = searchMoves(board, moveList, 1);
					
					// Stores current position and final position
					int[] move = new int[4];
					move[0] = moveList.get(0)[0];
					move[1] = moveList.get(0)[1];
					move[2] = moveList.get(moveList.size() - 1)[0];
					move[3] = moveList.get(moveList.size() - 1)[1];
					
					// If the move is the highest so far, save it 
					if (findDistance(move) >= findDistance(bestMove))
						bestMove = move;
				}
				
			}
		}
		return bestMove;
	}
	
	// Recursively searches through the board for the move that covers the most distance
	private ArrayList<Integer[]> searchMoves(Board board, ArrayList<Integer[]> moveList, int depth) {
		if (depth > DEPTH) return moveList;
		
		Integer[] currentPos = moveList.get(0);
		return moveList;
	}
	
	// Finds the distance between two points
	private int findDistance(int startRow, int startCol, int endRow, int endCol) {
		return (endRow - startRow) * (endRow - startRow) + (endCol - startCol) * (endCol - startCol); 
	}
	// Same as above but for an array with 4 intgers
	private int findDistance(int[] places) {
		return (places[2] - places[0]) * (places[2] - places[0]) + (places[3] - places[1]) * (places[3] - places[1]); 
	}
	
	// Locates the farthest point to target depending on 
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
