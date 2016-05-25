import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/** The Board Class
 *  Stores the data and methods relating to a Chinese Checkers game board
 *  @author Darren Chan
 *	@version May 24, 2016
 */

public class Board {
	
	private int[][] board;
	
	public static final int NUM_ROWS = 17;
	public static final int NUM_COLUMNS = 17;

	public static final int DIRECTION_SOUTH = 1;
	public static final int DIRECTION_SOUTHEAST = 2;
	public static final int DIRECTION_NORTHEAST = 3;
	public static final int DIRECTION_NORTH = 4;
	public static final int DIRECTION_NORTHWEST = 5;
	public static final int DIRECTION_SOUTHWEST = 6;
	public static final int DIRECTION_EAST = 7;
	public static final int DIRECTION_WEST = 8;
	
	private static boolean isTesting = false;
	
	/** Creates a new board with the setup specified in BoardMap
	 *  Precondition: BoardMap exists in the project folder and has been properly initialized
	 *  Postcondition: a new board with the setup specified in BoardMap has been created and returned
	 */
	public Board () {
		board = new int[NUM_ROWS][NUM_COLUMNS];
		newGame();
	}
	/** A clone constructor that creates a new board with a pre-existing board state
	 *  Precondition: board is a valid and initialized Board object
	 *  Postcondition: a new board with an identical setup has been created and returned
	 *  @param board a pre-existing board to copy
	 */
	public Board(Board board) {
		this.board = new int[NUM_ROWS][NUM_COLUMNS];
		for (int row = 0; row < NUM_ROWS; row++)
			this.board[row] = Arrays.copyOf(board.getBoard()[row], board.getBoard()[row].length);
	}
	
	/** Checks if two points on the board are adjacent and returns the direction they are adjacent in
	 *  Precondition: row1, column1, row2, and column2 are valid integers that correspond to two points
	 *  on the board
	 *  Postcondition: the direction (as an integer) the two pieces are adjacent to each other in has been returned,
	 *  otherwise 0 if the two positions are not adjacent
	 *  @param row1 the row of the piece to check if the other piece is adjacent to
	 *  @param column1 the column of the piece to check if the other piece is adjacent to
	 *  @param row2 the row of the piece that is checked for adjacency to the first piece
	 *  @param column2 the row of the piece that is checked for adjacency to the first piece
	 *  @return the direction that the pieces are adjacent in (as an integer constant), otherwise 0 if not adjacent
	 */
	public static int isAdjacent(int row1, int column1, int row2, int column2) {
		if (row1 == row2 && column1 + 1 == column2)
			return DIRECTION_SOUTHWEST;
		if (row1 == row2 && column1 - 1 == column2)
			return DIRECTION_NORTHEAST;
		if (column1 == column2 && row1 + 1 == row2)
			return DIRECTION_EAST;
		if (column1 == column2 && row1 - 1 == row2)
			return DIRECTION_WEST;
		if (row1 + 1 == row2 && column1 + 1 == column2)
			return DIRECTION_SOUTHEAST;
		if (row1 - 1 == row2 && column1 - 1 == column2)
			return DIRECTION_NORTHWEST;
		return 0;
	}
	
	/** Returns whether or not a point is a valid point on the board
	 *  Precondition: coordinates is an initialized Point object with X and Y
	 *  Postcondition: whether or not the point is a valid point has been returned
	 *  @param coordinates the coordinates to check whether or not is valid
	 *  @return whether or not the coordinates are valid for the board
	 */
	public boolean isValidPoint(Point coordinates) {
		return (coordinates.getX() < NUM_ROWS && coordinates.getX() >= 0 && coordinates.getY() >= 0 
				&& coordinates.getY() < NUM_COLUMNS && board[(int)coordinates.getX()][(int)coordinates.getY()] > -1);
	}
	
	/** Returns whether or not a point is a valid point on the board
	 *  Precondition: row and column are valid integers
	 *  Postcondition: whether or not the point is a valid point has been returned
	 *  @param row the row of the point to check if is valid
	 *  @param column the column of the point to check if is valid
	 *  @return whether or not the coordinates are valid for the board
	 */
	public boolean isValidPoint(int row, int column) {
		return (row < NUM_ROWS && row >= 0 && column >= 0 && column < NUM_COLUMNS && board[row][column] > -1);
	}
	
	/** Gets the coordinates of the point adjacent to the specified point in the specified direction
	 *  Precondition: coordinates is an initialized Point object with X and Y and direction is an integer representing
	 *  one of the direction constants
	 *  Postcondition: the adjacent Point containing the row (X) and column (Y) has been returned
	 *  @param coordinates the original point to find the point adjacent from
	 *  @param direction the direction of the adjacent point from the original point
	 *  @return a Point containing the row (X) and column (Y) of the point adjacent to the given point in the given
	 *  direction
	 */
	public Point getAdjacent(Point coordinates, int direction) {
		return getAdjacent((int)coordinates.getX(), (int)coordinates.getY(), direction);
	}
	
	/** Gets the coordinates of the point adjacent to the specified point in the specified direction
	 *  Precondition: row and column are valid integers representing a position on the boardand direction 
	 *  is an integer representing one of the direction constants
	 *  Postcondition: the direction that the points are adjacent in has been returned
	 *  @param row the row of the original point to search from
	 *  @param column the column of the original point to search from
	 *  @param direction the direction from the original point to find the adjacent point in
	 *  @return a Point containing the row (X) and column (Y) of the point adjacent to the given point in the given
	 *  direction
	 */
	public Point getAdjacent(int row, int column, int direction) {
		if (direction == DIRECTION_SOUTHWEST)
			return new Point(row, column + 1);
		if (direction == DIRECTION_NORTHEAST)
			return new Point(row, column - 1);
		if (direction == DIRECTION_EAST)
			return new Point(row + 1, column);
		if (direction == DIRECTION_WEST)
			return new Point(row - 1, column);
		if (direction == DIRECTION_SOUTHEAST)
			return new Point(row + 1, column + 1);
		if (direction == DIRECTION_NORTHWEST)
			return new Point(row - 1, column - 1);
		return null;
	}
	
	/** A recursive algorithm for determine whether or not a piece can jump to the specified spot
	 *  Precondition: originalRow, originalColumn, newRow, and newColumn are valid integers 
	 *  that correspond to two points on the board, and alreadyChecked is an initialized NUM_ROWS by NUM_COLUMNS
	 *  integer array
	 *  Postcondition: whether or not a piece can jump from the original point to the new point has been returned
	 *  @param originalRow the original row of the piece
	 *  @param originalColumn the original column of the piece
	 *  @param newRow the row of the space to jump to
	 *  @param newColumn the column of the space to jump to
	 *  @param alreadyChecked an array listing all the spaces already checked in this iteration of the algorithm
	 *  @return whether or not a piece positioned at originalRow, originalColumn can jump to newRow, newColumn,
	 *  possibly using multiple jumps
	 */
	private boolean canJump(int originalRow, int originalColumn, int newRow, int newColumn, int[][] alreadyChecked){
		//System.out.println(originalRow + " " + originalColumn+ " " + newRow+ " " + newColumn);
		// Check if this spot has already been visited
		if (alreadyChecked[originalRow][originalColumn] == 1)
			return false;
		
		// Check if trying to jump to or from an occupied location
		if (board[newRow][newColumn] != 0)
			return false;
		
		alreadyChecked[originalRow][originalColumn] = 1;
		// Check in all possible piece directions - not north or south
		for (int direction = 1; direction <= 8; direction++) {
			if (direction != DIRECTION_NORTH && direction != DIRECTION_SOUTH) {
				// Check if there's a piece to jump over in that direction
				Point otherSpot = getAdjacent(originalRow, originalColumn, direction);
				if (isValidPoint(otherSpot) && board[(int)otherSpot.getX()][(int)otherSpot.getY()] > 0) {
					Point jumpSpot = getAdjacent(otherSpot, direction);
					// Check if that spot beyond the piece is where you want to go, or if it's possible to jump to 
					// the spot you want to go from that spot
					if (isValidPoint(jumpSpot) && board[(int)jumpSpot.getX()][(int)jumpSpot.getY()] == 0 &&
							(((int)jumpSpot.getX() == newRow && (int)jumpSpot.getY() == newColumn) ||
							canJump((int)jumpSpot.getX(), (int)jumpSpot.getY(), newRow, newColumn, alreadyChecked)))
						return true;					
				}
			}
		}
		return false;
	}
	
	/** Checks if moving a piece from one position to another is valid
	 *  Precondition: originalRow, originalColumn, newRow, and newColumn are valid integers 
	 *  that correspond to two points on the board
	 *  Postcondition: whether or not a piece moving from the original spot to the new spot would be
	 *  valid has been returned
	 *  @param originalRow the original row of the piece
	 *  @param originalColumn the original column of the piece
	 *  @param newRow the row of the space to check if a move to there is valid
	 *  @param newColumn the column of the space to check if a move to there is valid
	 *  @return whether or not a piece positioned at originalRow, originalColumn can move to the spot at
	 *  newRow, newColumn
	 */
	public boolean isValidMove(int originalRow, int originalColumn, int newRow, int newColumn) {
		// Check if the original and new spots are valid, if the new spot is empty
		if (!isValidPoint(originalRow, originalColumn) || !isValidPoint(newRow, newColumn)
				|| board[newRow][newColumn] != 0)
			return false;
		
		// Check if the spots are identical
		if (originalRow == newRow && originalColumn == newColumn)
			return false;
		
		// Check if the two spots are beside each other
		if (isAdjacent(originalRow, originalColumn, newRow, newColumn) > 0)
			return true;
		
		// Check if you can jump from the original point to the new point
		return canJump(originalRow, originalColumn, newRow, newColumn, new int[NUM_ROWS][NUM_COLUMNS]);
	}
	
	/** Checks to see if a spot is in a home that the player should not enter (i.e. not the player's
	 *  own home or the target home)
	 *  Precondition: row, column, and thisPlayer are valid integers, and thisPlayer is a valid player
	 *  number (i.e. between 1 and 6 inclusive)
	 *  Postcondition: whether or not the spot is in a home that the player should not enter has been returned
	 *  @param row the row of the spot to check
	 *  @param column the column of the spot to check
	 *  @param thisPlayer the player of the piece the spot is considered for (determines which homes
	 *  are allowed and which are not)
	 *  @return true if the spot is in a home that the player should not enter, false otherwise
	 */
	public boolean notAllowedHome(int row, int column, int thisPlayer) {
		if (thisPlayer == 1 || thisPlayer == 4) return 
				isHome(row, column, 2) ||
				isHome(row, column, 3) || 
				isHome(row, column, 5) ||
				isHome(row, column, 6);
		else if (thisPlayer == 2 || thisPlayer == 5) return
				isHome(row, column, 1) ||
				isHome(row, column, 3) || 
				isHome(row, column, 4) ||
				isHome(row, column, 6);
		else if (thisPlayer == 3 || thisPlayer == 6) return
				isHome(row, column, 1) ||
				isHome(row, column, 2) || 
				isHome(row, column, 4) ||
				isHome(row, column, 5);
		return false;
	}
	
	/** Checks if a spot is in the home of a player
	 *  Precondition: row, column, and player are valid integers, and player is a valid player
	 *  number (i.e. between 1 and 6 inclusive)
	 *  Postcondition: whether or not the spot is in player's home has been returned
	 *  @param row the row of the spot to check
	 *  @param column the column of the spot to check
	 *  @param player the player whose home to check if the spot is in
	 *  @return true if the spot is in player's home, false otherwise
	 */
	public boolean isHome(int row, int column, int player) {
		if (!isValidPoint(row, column))
			return false;
		if (player == 1 && row >= 13) {
			return true;
		}
		else if (player == 2 && column >= 13) {
			return true;
		}
		else if (player == 3 && row >= 4 && row <= 7 && column >= row + 5) {
			return true;
		}
		else if (player == 4 && row <= 3) {
			return true;
		}
		else if (player == 5 && column <= 3) {
			return true;
		}
		else if (player == 6 && row >= 9 && row <= 12 && column <= row - 5) {
			return true;
		}
		return false;
	}
	
	/** Checks if a spot is a valid move, counting other player's homes as invalid
	 *  Precondition: originalRow, originalColumn, newRow, and newColumn are valid integers, and currentPlayer
	 *  is a proper player number (between 1 to 6 inclusive)
	 *  Postcondition: whether or not the move is valid for the current player has been returned
	 *  @param originalRow the original row of the piece to move
	 *  @param originalColumn the original column of the piece to move
	 *  @param newRow the row of the position to check if the piece can move to
	 *  @param newColumn the column of the position to check if the piece can move to
	 *  @param currentPlayer the player that the piece belongs to
	 *  @return true if the piece is allowed to move from the original position to the new position, false 
	 *  otherwise
	 */
	public boolean isValidMoveNoOtherHomes(int originalRow, int originalColumn, int newRow, int newColumn, 
			int currentPlayer) {
		if (notAllowedHome(newRow, newColumn, currentPlayer))
			return false;
		return isValidMove(originalRow, originalColumn, newRow, newColumn);
	}
	
	/** Checks if a piece can be moved to a particular spot and moves it if the move is valid
	 *  Precondition: originalRow, originalColumn, newRow, and newColumn are valid integers 
	 *  that correspond to two points on the board
	 *  Postcondition: whether or not the move was successfully made has been returned
	 *  @param originalRow the original row of the piece
	 *  @param originalColumn the original column of the piece
	 *  @param newRow the row of the space to move the piece to
	 *  @param newColumn the column of the space to move the piece to
	 *  @return whether or not the move was successfully made (i.e. whether or not it is valid)
	 */
	public boolean moveNoOtherHomes(int originalRow, int originalColumn, int newRow, int newColumn, int player) {
		if (isValidMoveNoOtherHomes(originalRow, originalColumn, newRow, newColumn, player)) {
			board[newRow][newColumn] = board[originalRow][originalColumn];
			board[originalRow][originalColumn] = 0;
			return true;
		}
		return false;
	}
	
	/** Checks if a piece can be moved to a particular spot and moves it if the move is valid
	 *  Precondition: originalRow, originalColumn, newRow, and newColumn are valid integers 
	 *  that correspond to two points on the board
	 *  Postcondition: whether or not the move was successfully made has been returned
	 *  @param originalRow the original row of the piece
	 *  @param originalColumn the original column of the piece
	 *  @param newRow the row of the space to move the piece to
	 *  @param newColumn the column of the space to move the piece to
	 *  @return whether or not the move was successfully made (i.e. whether or not it is valid)
	 */
	public boolean move(int originalRow, int originalColumn, int newRow, int newColumn) {
		if (isValidMove(originalRow, originalColumn, newRow, newColumn)) {
			board[newRow][newColumn] = board[originalRow][originalColumn];
			board[originalRow][originalColumn] = 0;
			return true;
		}
		return false;
	}
	
	/** Checks if a piece can be moved to a particular spot and moves it if the move is valid
	 *  Precondition: originalRow, originalColumn, newRow, and newColumn are valid integers 
	 *  that correspond to two points on the board
	 *  Postcondition: whether or not the move was successfully made has been returned
	 *  @param originalRow the original row of the piece
	 *  @param originalColumn the original column of the piece
	 *  @param newRow the row of the space to move the piece to
	 *  @param newColumn the column of the space to move the piece to
	 *  @return whether or not the move was successfully made (i.e. whether or not it is valid)
	 */
	public boolean moveNoErrorChecking(int originalRow, int originalColumn, int newRow, int newColumn) {
		board[newRow][newColumn] = board[originalRow][originalColumn];
		board[originalRow][originalColumn] = 0;
		return true;
	}
	
	/** Initializes the board for a new game
	 *  Precondition: BoardMap exists in the project folder and has been properly initialized
	 *  Postcondition: the board has been initialized using the setup encoded in BoardMap
	 *  @return whether or not the board was successfully initialized
	 */
	public boolean newGame() {
		try {
			// Read the board map
			File boardMap = new File("BoardMap");
			Scanner inFile = new Scanner(boardMap);
			
			// Block off ineligible spots
			for (int row = 0; row < NUM_ROWS; row++) {
				String boardRow = inFile.nextLine();
				for (int column = 0; column < NUM_COLUMNS; column++) {
					if (boardRow.charAt(column) == 'x')
						board[row][column] = -1;
					else if (isTesting)
						board[row][column] = Character.getNumericValue(boardRow.charAt(column));
				}
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed setup!");
			return false;
		}
	}
	
	/** Gets the board
	 *  @return the board
	 */
	public int[][] getBoard() {
		return board;
	}
	
	public static void main (String[] args) {
		// Board testing code
		isTesting = true;
		
		Board newBoard = new Board();
		BoardDisplay window = new BoardDisplay(newBoard.board);
		
		// Testing
		Algorithm[] algorithms = new Algorithm[6];
		for (int player = 0; player < 6; player++) {
			algorithms[player] = new Algorithm(player + 1);
		}

		try {
			Thread.sleep(500);
		}
		catch (Exception e) {
		}
		
		int currentTurn = 1;
		while(true) {
			for (int player = 1; player <= 6; player++) {
				System.out.println(player);
				window.setTurn(currentTurn);
				int[] move = Client.opening(player, currentTurn, newBoard);
				if (move == null)
					move = algorithms[player - 1].nextMove(newBoard);
				System.out.println(Arrays.toString(move));
				if (move != null)
					System.out.println(newBoard.move(move[0], move[1], move[2], move[3]));
				window.refresh();
				try {
					Thread.sleep(50);
				}
				catch (Exception e) {
					
				}
			}
			currentTurn++;
		}
		
		
	}

}
