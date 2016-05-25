import java.util.ArrayList;

/**
 * A class that will calculate the optimal move to make in a Chinese Checkers game,
 * given the board state.
 * 
 * @author Caleb Choi
 */
public class Algorithm {

	// tinyurl.com/chinesecheckersprotocol

	// Please dont make this less than 1
	// Note that whatever the depth is, the algorithm will guaranteed reach the 
	// max depth level assuming a single jump is possible anywhere at the start
	private final static int DEPTH = 14; 
	
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
		if (color < 1 || color > 6) color = 1;
		else this.color = color;
	}

	/**
	 * Given the current state of the board, it will calculate the optimal
	 * move to make
	 * 
	 * @param board Chinese checkers game board
	 * @return A 4 element long 2D integer array which contains the row and column 
	 * of the piece to be moved, followed by the row and column of its final position
	 */
	public int[] nextMove(Board board) {

		// Stores the length of the distance travelled
		int[] bestMove = null;

		// Calculates the target given the board position
		findTargets(board);

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
		return bestMove;
	}

	/**
	 * Recursively searches through the board for the move that covers the most distance.
	 * Includes both walks and jumps, and will internally make sure that the only moves
	 * it branches out into are valid
	 * @param board Chinese checkers game board
	 * @param moveList A list of moves taken on a certain branch
	 * @param depth The current recursive iteration depth
	 * @return An arraylist of the sequential moves of the branch that has progressed
	 * most to the target
	 */
	private ArrayList<Integer[]> searchMoves(Board board, ArrayList<Integer[]> moveList, int depth) {

		// If at final depth level, return watchu' got
		if (depth > DEPTH) {

			// If the final position of the piece is in another person's home space, discard result
			if (board.notAllowedHome(moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1], color)
					&& board.getBoard()[moveList.get(moveList.size() - 1)[0]][moveList.get(moveList.size() - 1)[1]] != 1) {
				return new ArrayList<Integer[]>(); // Length zero, will be discarded later in program
			}

			// Otherwise return delicious list
			else return moveList;
		}

		// Stores the best move found so far
		ArrayList<Integer[]> bestMoves = new ArrayList<Integer[]>();

		// Looks for all possible moves for the current position
		ArrayList<Integer[]> possibleMoves = findMoves(board, moveList.get(moveList.size() - 1), depth);
		
		// Removes all previously visited possible moves
		for (int i = possibleMoves.size() - 1; i >= 0; i--) {
			for (int j = 0; j < moveList.size(); j++) {
				if (possibleMoves.get(i)[0] == moveList.get(j)[0] && possibleMoves.get(i)[1] == moveList.get(j)[1]) {
					possibleMoves.remove(i);
					break;
				}
			}
		}

		// If there are no possible moves afterwards that advance the branch, it
		// means you've reached the end of the branch. Will just return the sublist within 
		// the current move list that makes it most to the target row
		if (possibleMoves.size() == 0) {
			
			// Iterates through all sub move list lengths
			for (int i = moveList.size() - 1; i > 0; i--) {
				
				// Fills in all the sub move list values from the beginning on up
				ArrayList<Integer[]> subMoveList = new ArrayList<Integer[]>();
				for (int j = 0; j <= i; j++) subMoveList.add(moveList.get(j));
				
				// If this submove turns out to be the best so far, keep it
				if (bestMoves.size() == 0 || distanceTravelledToTarget(subMoveList) > distanceTravelledToTarget(bestMoves))
					
					// If the last move isn't on someone else's home territory
					if (!board.notAllowedHome(subMoveList.get(subMoveList.size() - 1)[0], subMoveList.get(subMoveList.size() - 1)[1], color)
							&& board.getBoard()[subMoveList.get(subMoveList.size() - 1)[0]][subMoveList.get(subMoveList.size() - 1)[1]] != 1)
						
						bestMoves = subMoveList;
			}
		}
		
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
			if (newBoard.move(moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1], 
					possibleMoves.get(i)[0], possibleMoves.get(i)[1])) {
				
				// If the first move is a jump and not a walk, search for all further moves
				if (isJump(potentialMoves)) {
					
					// Search tree for best move
					ArrayList<Integer[]> bestSubMoves = searchMoves(board, potentialMoves, depth + 1);

					// Checks if searched move is better than the current one
					if (bestMoves.size() == 0 ||
							distanceTravelledToTarget(bestSubMoves) > distanceTravelledToTarget(bestMoves))
						bestMoves = bestSubMoves;

				} else if (bestMoves.size() == 0 || distanceTravelledToTarget(potentialMoves) > distanceTravelledToTarget(bestMoves)) {
					// If it's a walk, just check the walk distance
					
					// If the final position of the piece is not in another person's home space, keep result
					if (!board.notAllowedHome(potentialMoves.get(potentialMoves.size() - 1)[0], potentialMoves.get(potentialMoves.size() - 1)[1], color))
						bestMoves = potentialMoves;
				}
			}
		}

		if (bestMoves == null) return new ArrayList<Integer[]>(); // Blank arraylists are disposed of later
		return bestMoves;
	}

	/**
	 * Finds all immediate possible moves given a piece's position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An arraylist of all the possible immediate moves the piece could make
	 */
	private ArrayList<Integer[]> findMoves(Board board, Integer[] currentPos, int depth) {

		// Variable initialization
		ArrayList<Integer[]> movelist = new ArrayList<Integer[]>();
		Integer[] temp;

		// Searches for all possible moves
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

		return movelist;
	}

	/**
	 * Finds possible moves west of current position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An integer array with two elements containing the row and column of the
	 * move the current piece can make. If no move is possible, returns null
	 */
	private Integer[] findWest(Board board, Integer[] currentPos, int depth) {
		try { // W
			if (depth == 1 && board.getBoard()[currentPos[0]][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0], currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0]][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0]][currentPos[1] - 1] != 0
					&& board.getBoard()[currentPos[0]][currentPos[1] - 2] == 0)
				return new Integer[] {currentPos[0], currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}

	/**
	 * Finds possible moves northwest of current position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An integer array with two elements containing the row and column of the
	 * move the current piece can make. If no move is possible, returns null
	 */
	private Integer[] findNorthWest(Board board, Integer[] currentPos, int depth) {
		try { // NW
			if (depth == 1 && board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1] - 1};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] != -1
					&& board.getBoard()[currentPos[0] - 1][currentPos[1] - 1] != 0
					&& board.getBoard()[currentPos[0] - 2][currentPos[1] - 2] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1] - 2};
			else return null;
		} catch (Exception e) {return null;}
	}

	/**
	 * Finds possible moves northeast of current position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An integer array with two elements containing the row and column of the
	 * move the current piece can make. If no move is possible, returns null
	 */
	private Integer[] findNorthEast(Board board, Integer[] currentPos, int depth) {
		try { // NE
			if (depth == 1 && board.getBoard()[currentPos[0] - 1][currentPos[1]] == 0) 
				return new Integer[] {currentPos[0] - 1, currentPos[1]};
			else if (board.getBoard()[currentPos[0] - 1][currentPos[1]] != -1
					&& board.getBoard()[currentPos[0] - 1][currentPos[1]] != 0
					&& board.getBoard()[currentPos[0] - 2][currentPos[1]] == 0)
				return new Integer[] {currentPos[0] - 2, currentPos[1]};
			else return null;
		} catch (Exception e) {return null;}
	}

	/**
	 * Finds possible moves east of current position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An integer array with two elements containing the row and column of the
	 * move the current piece can make. If no move is possible, returns null
	 */
	private Integer[] findEast(Board board, Integer[] currentPos, int depth) {
		try { // E
			if (depth == 1 && board.getBoard()[currentPos[0]][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0], currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0]][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0]][currentPos[1] + 1] != 0
					&& board.getBoard()[currentPos[0]][currentPos[1] + 2] == 0)
				return new Integer[] {currentPos[0], currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}

	/**
	 * Finds possible moves south east of current position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An integer array with two elements containing the row and column of the
	 * move the current piece can make. If no move is possible, returns null
	 */
	private Integer[] findSouthEast(Board board, Integer[] currentPos, int depth) {
		try { // SE
			if (depth == 1 && board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1] + 1};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] != -1
					&& board.getBoard()[currentPos[0] + 1][currentPos[1] + 1] != 0
					&& board.getBoard()[currentPos[0] + 2][currentPos[1] + 2] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1] + 2};
			else return null;
		} catch (Exception e) {return null;}
	}

	/**
	 * Finds possible moves southwest of current position
	 * @param board Chinese checkers game board
	 * @param currentPos Current position of the piece to check
	 * @param depth Current iteration depth
	 * @return An integer array with two elements containing the row and column of the
	 * move the current piece can make. If no move is possible, returns null
	 */
	private Integer[] findSouthWest(Board board, Integer[] currentPos, int depth) {
		try { // SW
			if (depth == 1 && board.getBoard()[currentPos[0] + 1][currentPos[1]] == 0) 
				return new Integer[] {currentPos[0] + 1, currentPos[1]};
			else if (board.getBoard()[currentPos[0] + 1][currentPos[1]] != -1
					&& board.getBoard()[currentPos[0] + 1][currentPos[1]] != 0
					&& board.getBoard()[currentPos[0] + 2][currentPos[1]] == 0)
				return new Integer[] {currentPos[0] + 2, currentPos[1]};
			else return null;
		} catch (Exception e) {return null;}
	}

	/**
	 * Finds the total distance travelled to the target given a starting position
	 * and end position
	 * @param startRow Start position row
	 * @param startCol Start position column
	 * @param endRow End position row 
	 * @param endCol End position column
	 * @return An integer denoting the total distance travelled towards the target
	 */
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
	/**
	 * Finds the total distance travelled to the target given a starting position
	 * and end position
	 * @param places An integer array of four elements containing the starting row, 
	 * starting column, end row, and end column in that respective order 
	 * @return An integer denoting the total distance travelled towards the target
	 */
	private int distanceTravelledToTarget(int[] places) {
		return distanceTravelledToTarget(places[0], places[1], places[2], places[3]); 
	}
	/**
	 * Finds the total distance travelled to the target given a starting position
	 * and end position
	 * @param moveList An arraylist of integer arrays where the starting position is the
	 * first element and the end position is the last element 
	 * @return An integer denoting the total distance travelled towards the target
	 */
	private int distanceTravelledToTarget(ArrayList<Integer[]> moveList) {
		if (moveList.size() > 0) {
			return distanceTravelledToTarget(moveList.get(0)[0], moveList.get(0)[1], 
					moveList.get(moveList.size() - 1)[0], moveList.get(moveList.size() - 1)[1]);
		} else return Integer.MIN_VALUE;
	}

	/**
	 * Determines if the most recent move is a jump
	 * @param moveList A list of moves taken
	 * @return Returns true if the most recent move taken is a jump. Returns false if
	 * it is a walk
	 */
	private boolean isJump(ArrayList<Integer[]> moveList) {
		Integer[] currentMove = moveList.get(moveList.size() - 1);
		Integer[] lastMove = moveList.get(moveList.size() - 2);
		return ((int) Math.abs(currentMove[0] - lastMove[0]) > 1) || ((int) Math.abs(currentMove[1] - lastMove[1]) > 1); 
	}

	/**
	 * Locates the farthest row that needs to be filled given the player
	 * @param board Current state of the board
	 */
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
			if (lastRowFull) {
				int row = 8, col = 4;
				while (row < 13 && col < 9) {
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
				int row = 7, col = 4;
				while (row < 13 && col < 10) {
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
			if (lastRowFull) {
				int row = 4, col = 8;
				while (row < 9 && col < 13) {
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
				int row = 4, col = 7;
				while (row < 10 && col < 13) {
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
