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
	private int targetCol;

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
		int[] bestMove = null;
		
		// Calculates the target given the board position
		findTarget(board);
		
		// Iterates through all pieces
		for (int row = 0; row < 17; row++) {
			for (int col = 0; col < 17; col++) {

				// If it finds a piece that is yours
				if (board.getBoard()[row][col] == color) {

					// Stores the current position in the first arraylist slot
					ArrayList<Integer[]> moveList = new ArrayList<Integer[]>();
					moveList.add(new Integer[] {row, col});

					// DFS search
					moveList = searchMoves(board, moveList, 1);
					
					// If the movelist actually have a move
					if (moveList.size() > 0) {

						// Stores current position and final position
						int[] move = new int[4];
						move[0] = moveList.get(0)[0];
						move[1] = moveList.get(0)[1];
						move[2] = moveList.get(moveList.size() - 1)[0];
						move[3] = moveList.get(moveList.size() - 1)[1];
	
						// If the move is the highest so far, save it 
						if (bestMove == null || distanceTravelledToTarget(move) >= distanceTravelledToTarget(bestMove))
							bestMove = move;
					}

				}

			}
		}
		return bestMove;
	}

	// Recursively searches through the board for the move that covers the most distance
	private ArrayList<Integer[]> searchMoves(Board board, ArrayList<Integer[]> moveList, int depth) {
		
		// If at final depth level, return watchu' got
		if (depth > DEPTH) return moveList;

		// Looks for all possible moves for the current position
		ArrayList<Integer[]> possibleMoves = findMoves(board, moveList.get(0));
		
		// Stores the best move found so far
		ArrayList<Integer[]> bestMoves = new ArrayList<Integer[]>();
		
		// Iterating through all possible moves
		for (int i = 0; i < possibleMoves.size(); i++) {
			
			// Create new moveset with added potential move
			// Also the shallow copy/deep copy thing is annoying
			ArrayList<Integer[]> potentialMoves = new ArrayList<Integer[]>();
			for (int j = 0; j < moveList.size(); j++)
				potentialMoves.add(moveList.get(j));
			potentialMoves.add(possibleMoves.get(i));
			
			// Update the board with new move
			Board newBoard = new Board(board);
			
			// If the move is valid (sometimes it might not be)
			if (newBoard.move(moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1], possibleMoves.get(i)[0], possibleMoves.get(i)[1])) {

				// Search tree for best move
				ArrayList<Integer[]> bestSubMoves = searchMoves(board, potentialMoves, depth + 1);

				// Checks if searched move is better than the current one
				if (bestMoves.size() == 0 || distanceTravelledToTarget(bestSubMoves) > distanceTravelledToTarget(bestMoves))
					bestMoves = bestSubMoves;
			
			}
				
		}

		return bestMoves;
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
			if (board.getBoard()[currentPos[0] - 1][currentPos[1]] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1]};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1]] != -1
					&& board.getBoard()[currentPos[0] - 2][currentPos[1]] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1]};
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
			if (board.getBoard()[currentPos[0] + 1][currentPos[1]] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1]};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1]] != -1
					&& board.getBoard()[currentPos[0] + 2][currentPos[1]] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1]};
			else return null;
		} catch (Exception e) {return null;}
	}

	// Finds the distance between two points
	private int distanceTravelledToTarget(int startRow, int startCol, int endRow, int endCol) {
		int currentDistance = (targetRow - startRow) * (targetRow - startRow) + (targetCol - startCol) * (targetCol - startCol); 
		int endDistance = (targetRow - endRow) * (targetRow - endRow) + (targetCol - endCol) * (targetCol - endCol);
		return currentDistance - endDistance;
	}
	// Same as above but for an array with 4 intgers
	private int distanceTravelledToTarget(int[] places) {
		return distanceTravelledToTarget(places[0], places[1], places[2], places[3]); 
	}
	// Same as above but for an arraylist of integer arrays
	private int distanceTravelledToTarget(ArrayList<Integer[]> moveList) {
		return distanceTravelledToTarget(moveList.get(0)[0], moveList.get(0)[1], 
				moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1]);
	}

	// Locates the farthest point to target depending on 
	private void findTarget(Board board) {
		
		if (color == 1) { //  Red, bottom
			for (int row = 0; row < 17; row++) {
				for (int col = 0; col < 17; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			targetRow = Integer.MAX_VALUE;
			targetCol = Integer.MAX_VALUE;
			
		} else if (color == 2) { // Orange, bottom left
			for (int col = 0; col < 17; col++) {
				for (int row = 0; row < 17; row++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			targetRow = Integer.MAX_VALUE;
			targetCol = Integer.MAX_VALUE;
			
		} else if (color == 3) { // Yellow, top right
			if (board.getBoard()[12][4] == 0) {
				targetRow = 12;
				targetCol = 4;
				return;
			}
			for (int row = 11; row < 13; row++) {
				for (int col = 4; col < 6; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			for (int row = 10; row < 13; row++) {
				for (int col = 4; col < 7; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			for (int row = 9; row < 13; row++) {
				for (int col = 4; col < 8; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			targetRow = Integer.MAX_VALUE;
			targetCol = Integer.MAX_VALUE;
			
		} else if (color == 4) { // Green, top
			for (int row = 16; row >= 0; row--) {
				for (int col = 0; col < 17; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			
		} else if (color == 5) { // Blue, top left
			for (int col = 16; col >= 0; col--) {
				for (int row = 0; row < 17; row++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			targetRow = Integer.MAX_VALUE;
			targetCol = Integer.MAX_VALUE;
			
		} else if (color == 6) { // Purple, bottom left
			if (board.getBoard()[4][12] == 0) {
				targetRow = 4;
				targetCol = 12;
				return;
			} 
			for (int row = 4; row < 6; row++) {
				for (int col = 11; col < 13; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			for (int row = 4; row < 7; row++) {
				for (int col = 10; col < 13; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			for (int row = 4; row < 8; row++) {
				for (int col = 9; col < 13; col++) {
					if (board.getBoard()[row][col] == 0) {
						targetRow = row;
						targetCol = col;
						return;
					}
				}
			}
			targetRow = Integer.MAX_VALUE;
			targetCol = Integer.MAX_VALUE;
			
		} else System.out.println("you dun goofed, son");
	}

}
