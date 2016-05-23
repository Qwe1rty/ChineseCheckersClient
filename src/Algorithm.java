import java.util.ArrayList;

/**
 * A class that will calculate the optimal move to make in a Chinese Checkers game,
 * given the board state.
 * 
 * @author Caleb Choi
 */
public class Algorithm {

	// tinyurl.com/chinesecheckersprotocol

	private final static int DEPTH = 20; // Please dont make this less than 1

	// If true, we include moves that move backwards
	// Be aware that if true, then hitting the max depth level is guaranteed
	// given there is an available jump in the first move
	private boolean isEndgame = true;

	// Stores all pieces that have reached their final destinations
	private ArrayList<Integer[]> settledPieces;

	// Target spaces that the algorithm tries to move pieces to
	private ArrayList<Integer[]> targetPlaces; 

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
		findTargets(board);

		// Checks if the gamestate is in endgame
		if (!isEndgame && isEndgame()) isEndgame = true;

		// Iterates through all pieces
		for (int row = 0; row < 17; row++) {
			for (int col = 0; col < 17; col++) {

				// If it finds a piece that is yours
				if (board.getBoard()[row][col] == color) {

					// Checks to see if the piece is in final place
					boolean pieceSettled = false;
					for (int i = 0; i < settledPieces.size(); i++) {
						if (row == settledPieces.get(i)[0] && col == settledPieces.get(i)[1]) {
							pieceSettled = true;
							break;
						}
					}

					// If the piece isn't settled yet
					if (!pieceSettled) {

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
		}

		// If there aren't any available moves, ACTIVATE LE ENDGAME
		if (bestMove == null && !isEndgame) {
			isEndgame = true;
			bestMove = nextMove(board);

		} else if (bestMove == null && isEndgame) // If endgame's already enabled, then there's no moves available
			return bestMove;

		return bestMove;
	}

	// Recursively searches through the board for the move that covers the most distance
	private ArrayList<Integer[]> searchMoves(Board board, ArrayList<Integer[]> moveList, int depth) {

		// If at final depth level, return watchu' got
		if (depth > DEPTH) return moveList;

		// Looks for all possible moves for the current position
		ArrayList<Integer[]> possibleMoves = findMoves(board, moveList.get(moveList.size() - 1), depth);

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
			// Also updates the board positions with the new move
			if (newBoard.move(moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1], possibleMoves.get(i)[0], possibleMoves.get(i)[1])) {

				// If the first move is a jump and not a walk, search for all further moves
				if (depth == 1 && isJump(potentialMoves)) {

					// Search tree for best move
					ArrayList<Integer[]> bestSubMoves = searchMoves(board, potentialMoves, depth + 1);

					// Checks if searched move is better than the current one
					if (bestMoves.size() == 0 || distanceTravelledToTarget(bestSubMoves) > distanceTravelledToTarget(bestMoves))
						bestMoves = bestSubMoves;

				} else if (bestMoves.size() == 0 || distanceTravelledToTarget(potentialMoves) > distanceTravelledToTarget(bestMoves)) {
					
					// If it's a walk, just check the walk distance
					bestMoves = potentialMoves; 
				}
			}
		}
		return bestMoves;
	}

	// Finds all possible moves given a piece's position
	private ArrayList<Integer[]> findMoves(Board board, Integer[] currentPos, int depth) {

		// Variable initialization
		ArrayList<Integer[]> movelist = new ArrayList<Integer[]>();
		Integer[] temp;

		// If it's the midgame, only check moves that move towards target
		if (!isEndgame) { 
			if (color == 1) { // Red, bottom
				temp = findWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findNorthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findNorthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
			} else if (color == 2) {
				temp = findSouthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findNorthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findNorthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
			} else if (color == 3) {
				temp = findSouthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findSouthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findNorthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
			} else if (color == 4) {
				temp = findEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findSouthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findSouthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
			} else if (color == 5) {
				temp = findNorthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findSouthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findSouthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
			} else if (color == 6) {
				temp = findNorthWest(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findNorthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
				temp = findSouthEast(board, currentPos, depth);
				if (temp != null) movelist.add(temp);
			}
		} else { // Check ALL moves, including ones that move backwards
			temp = findWest(board, currentPos, depth);
			if (temp != null) movelist.add(temp);
			temp = findNorthWest(board, currentPos, depth);
			if (temp != null) movelist.add(temp);
			temp = findNorthEast(board, currentPos, depth);
			if (temp != null) movelist.add(temp);
			temp = findEast(board, currentPos, depth);
			if (temp != null) movelist.add(temp);
			temp = findSouthEast(board, currentPos, depth);
			if (temp != null) movelist.add(temp);
			temp = findSouthWest(board, currentPos, depth);
			if (temp != null) movelist.add(temp);
		}
		return movelist;
	}
	private Integer[] findWest(Board board, Integer[] currentPos, int depth) {
		try { // W
			if (depth == 1 && board.getBoard()[currentPos[0]][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0], currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0]][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0]][currentPos[1] - 1] == 0)
				return new Integer[] {currentPos[0], currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findNorthWest(Board board, Integer[] currentPos, int depth) {
		try { // NW
			if (depth == 1 && board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0] - 2][currentPos[1] - 2] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findNorthEast(Board board, Integer[] currentPos, int depth) {
		try { // NE
			if (depth == 1 && board.getBoard()[currentPos[0] - 1][currentPos[1]] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1]};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1]] != -1
					&& board.getBoard()[currentPos[0] - 2][currentPos[1]] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1]};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findEast(Board board, Integer[] currentPos, int depth) {
		try { // E
			if (depth == 1 && board.getBoard()[currentPos[0]][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0], currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0]][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0]][currentPos[1] + 1] == 0)
				return new Integer[] {currentPos[0], currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findSouthEast(Board board, Integer[] currentPos, int depth) {
		try { // SE
			if (depth == 1 && board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0] + 2][currentPos[1] + 2] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}
	private Integer[] findSouthWest(Board board, Integer[] currentPos, int depth) {
		try { // SW
			if (depth == 1 && board.getBoard()[currentPos[0] + 1][currentPos[1]] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1]};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1]] != -1
					&& board.getBoard()[currentPos[0] + 2][currentPos[1]] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1]};
			else return null;
		} catch (Exception e) {return null;}
	}

	// Finds the distance between two points
	private int distanceTravelledToTarget(int startRow, int startCol, int endRow, int endCol) {
		int currentDistance = Integer.MAX_VALUE, endDistance = Integer.MAX_VALUE;
		for (int i = 0; i < targetPlaces.size(); i++) {
			int j = (targetPlaces.get(i)[0] - startRow) * (targetPlaces.get(i)[0] - startRow) +
					(targetPlaces.get(i)[1] - startCol) * (targetPlaces.get(i)[1] - startCol);
			if (j < currentDistance) currentDistance = j;
		}
		for (int i = 0; i < targetPlaces.size(); i++) {
			int j = (targetPlaces.get(i)[0] - endRow) * (targetPlaces.get(i)[0] - endRow) +
					(targetPlaces.get(i)[1] - endCol) * (targetPlaces.get(i)[1] - endCol);
			if (j < endDistance) endDistance = j;
		}
		if (currentDistance == Integer.MAX_VALUE || endDistance == Integer.MAX_VALUE) return Integer.MIN_VALUE;
		else return currentDistance - endDistance;
	}
	// Same as above but for an array with 4 integers
	private int distanceTravelledToTarget(int[] places) {
		return distanceTravelledToTarget(places[0], places[1], places[2], places[3]); 
	}
	// Same as above but for an arraylist of integer arrays
	private int distanceTravelledToTarget(ArrayList<Integer[]> moveList) {
		if (moveList.size() > 0) {
			return distanceTravelledToTarget(moveList.get(0)[0], moveList.get(0)[1], 
					moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1]);
		} else return Integer.MIN_VALUE;
	}

	// Determines if the most recent move is a jump
	private boolean isJump(ArrayList<Integer[]> moveList) {
		Integer[] currentMove = moveList.get(moveList.size() - 1);
		Integer[] lastMove = moveList.get(moveList.size() - 2);
		return ((int) Math.abs(currentMove[0] - lastMove[0]) > 1) || ((int) Math.abs(currentMove[1] - lastMove[1]) > 1); 
	}

	// Determines if the state of the game is in its endgame
	private boolean isEndgame() {
		return settledPieces.size() > 0;
	}

	// Locates the farthest row that needs to be filled
	private void findTargets(Board board) {

		// Initializashuns!
		targetPlaces = new ArrayList<Integer[]>();
		settledPieces = new ArrayList<Integer[]>();
		boolean lastRowFull = true;

		if (color == 1) { //  Red, bottom
			for (int row = 0; row < 17; row++) {
				if (lastRowFull) {
					for (int col = 0; col < 17; col++) {
						if (board.getBoard()[row][col] == 0) {
							lastRowFull = false;
							targetPlaces.add(new Integer[] {row, col});
						} else if (board.getBoard()[row][col] != -1) {
							settledPieces.add(new Integer[] {row, col});
						}
					}
				} else return;
			}

		} else if (color == 2) { // Orange, bottom left
			for (int col = 0; col < 17; col++) {
				if (lastRowFull) {
					for (int row = 0; row < 17; row++) {
						if (board.getBoard()[row][col] == 0) {
							lastRowFull = false;
							targetPlaces.add(new Integer[] {row, col});
						} else if (board.getBoard()[row][col] != -1) {
							settledPieces.add(new Integer[] {row, col});
						}
					}
				} else return;
			}

		} else if (color == 3) { // Yellow, top right
			if (board.getBoard()[12][4] == 0) {
				targetPlaces.add(new Integer[] {12, 4});
				lastRowFull = false;
			} else settledPieces.add(new Integer[] {12, 4});

			if (lastRowFull) {
				int row = 11, col = 4;
				while (row < 13 && col < 6) {
					if (board.getBoard()[row][col] == 0) {
						lastRowFull = false;
						targetPlaces.add(new Integer[] {row, col});
					} else if (board.getBoard()[row][col] != -1) {
						settledPieces.add(new Integer[] {row, col});
					}
					row++;
					col++;
				}
			}
			if (lastRowFull) {
				int row = 10, col = 4;
				while (row < 13 && col < 7) {
					if (board.getBoard()[row][col] == 0) {
						lastRowFull = false;
						targetPlaces.add(new Integer[] {row, col});
					} else if (board.getBoard()[row][col] != -1) {
						settledPieces.add(new Integer[] {row, col});
					}
					row++;
					col++;
				}
			}
			if (lastRowFull) {
				int row = 9, col = 4;
				while (row < 13 && col < 8) {
					if (board.getBoard()[row][col] == 0) {
						lastRowFull = false;
						targetPlaces.add(new Integer[] {row, col});
					} else if (board.getBoard()[row][col] != -1) {
						settledPieces.add(new Integer[] {row, col});
					}
					row++;
					col++;
				}
			}

		} else if (color == 4) { // Green, top
			for (int row = 16; row >= 0; row--) {
				if (lastRowFull) {
					for (int col = 0; col < 17; col++) {
						if (board.getBoard()[row][col] == 0) {
							lastRowFull = false;
							targetPlaces.add(new Integer[] {row, col});
						} else if (board.getBoard()[row][col] != -1) {
							settledPieces.add(new Integer[] {row, col});
						}
					}
				} else return;
			}

		} else if (color == 5) { // Blue, top left
			for (int col = 16; col >= 0; col--) {
				if (lastRowFull) {
					for (int row = 0; row < 17; row++) {
						if (board.getBoard()[row][col] == 0) {
							lastRowFull = false;
							targetPlaces.add(new Integer[] {row, col});
						} else if (board.getBoard()[row][col] != -1) {
							settledPieces.add(new Integer[] {row, col});
						}
					}
				} else return;
			}

		} else if (color == 6) { // Purple, bottom left
			if (board.getBoard()[4][12] == 0) {
				targetPlaces.add(new Integer[] {4, 12});
				lastRowFull = false;
			} else settledPieces.add(new Integer[] {4, 12});

			if (lastRowFull) {
				int row = 4, col = 11;
				while (row < 6 && col < 13) {
					if (board.getBoard()[row][col] == 0) {
						lastRowFull = false;
						targetPlaces.add(new Integer[] {row, col});
					} else if (board.getBoard()[row][col] != -1) {
						settledPieces.add(new Integer[] {row, col});
					}
					row++;
					col++;
				}
			}
			if (lastRowFull) {
				int row = 4, col = 10;
				while (row < 7 && col < 13) {
					if (board.getBoard()[row][col] == 0) {
						lastRowFull = false;
						targetPlaces.add(new Integer[] {row, col});
					} else if (board.getBoard()[row][col] != -1) {
						settledPieces.add(new Integer[] {row, col});
					}
					row++;
					col++;
				}
			}
			if (lastRowFull) {
				int row = 4, col = 9;
				while (row < 8 && col < 13) {
					if (board.getBoard()[row][col] == 0) {
						lastRowFull = false;
						targetPlaces.add(new Integer[] {row, col});
					} else if (board.getBoard()[row][col] != -1) {
						settledPieces.add(new Integer[] {row, col});
					}
					row++;
					col++;
				}
			}

		} else System.out.println("you dun goofed, son");
	}

}
