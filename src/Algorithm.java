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

		if (color < 1 || color > 6) color = 1;
		else this.color = color;
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
		ArrayList<Integer[]> possibleMoves = findMoves(board, currentPos);
		
		for (int i = 0; i < possibleMoves.size(); i++) {
			
		}

		return moveList;
	}

	// Finds all possible moves given a piece's position
	private ArrayList<Integer[]> findMoves(Board board, Integer[] currentPos) {

		// Variable initialization
		ArrayList<Integer[]> movelist = new ArrayList<Integer[]>();
		Integer[] temp;
		
		// If it's the midgame, only check moves that move towards target
		if (!isEndgame) { 
			if (color == 1) { // Red, bottom
				temp = findWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findNorthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findNorthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findEast(board, currentPos);
				if (temp != null) movelist.add(temp);
			} else if (color == 2) {
				temp = findSouthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findNorthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findNorthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
			} else if (color == 3) {
				temp = findSouthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findSouthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findNorthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
			} else if (color == 4) {
				temp = findEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findSouthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findSouthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findWest(board, currentPos);
				if (temp != null) movelist.add(temp);
			} else if (color == 5) {
				temp = findNorthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findSouthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findSouthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
			} else if (color == 6) {
				temp = findNorthWest(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findNorthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findEast(board, currentPos);
				if (temp != null) movelist.add(temp);
				temp = findSouthEast(board, currentPos);
				if (temp != null) movelist.add(temp);
			}
		} else { // Check ALL moves, including ones that move backwards
			temp = findWest(board, currentPos);
			if (temp != null) movelist.add(temp);
			temp = findNorthWest(board, currentPos);
			if (temp != null) movelist.add(temp);
			temp = findNorthEast(board, currentPos);
			if (temp != null) movelist.add(temp);
			temp = findEast(board, currentPos);
			if (temp != null) movelist.add(temp);
			temp = findSouthEast(board, currentPos);
			if (temp != null) movelist.add(temp);
			temp = findSouthWest(board, currentPos);
			if (temp != null) movelist.add(temp);
		}
		return movelist;
	}
	private Integer[] findWest(Board board, Integer[] currentPos) {
		try { // W
			if (board.getBoard()[currentPos[0]][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0], currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0]][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0]][currentPos[1] - 1] == 0)
				return new Integer[] {currentPos[0], currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findNorthWest(Board board, Integer[] currentPos) {
		try { // NW
			if (board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0] - 2][currentPos[1] - 2] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findNorthEast(Board board, Integer[] currentPos) {
		try { // NE
			if (board.getBoard()[currentPos[0] - 1][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0] - 2][currentPos[1] + 2] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findEast(Board board, Integer[] currentPos) {
		try { // E
			if (board.getBoard()[currentPos[0]][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0], currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0]][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0]][currentPos[1] + 1] == 0)
				return new Integer[] {currentPos[0], currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findSouthEast(Board board, Integer[] currentPos) {
		try { // SE
			if (board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0] + 2][currentPos[1] + 2] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findSouthWest(Board board, Integer[] currentPos) {
		try { // SW
			if (board.getBoard()[currentPos[0] + 1][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0] + 2][currentPos[1] - 2] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}

	// Finds the distance between two points
	private int findDistance(int startRow, int startCol, int endRow, int endCol) {
		return (endRow - startRow) * (endRow - startRow) + (endCol - startCol) * (endCol - startCol); 
	}
	// Same as above but for an array with 4 intgers
	private int findDistance(int[] places) {
		return findDistance(places[0], places[1], places[2], places[3]); 
	}
	// Same as above but for an arraylist of integer arrays
	private int findDistance(ArrayList<Integer[]> moveList) {
		return findDistance(moveList.get(0)[0], moveList.get(0)[1], 
				moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1]);
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
