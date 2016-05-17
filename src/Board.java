import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


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
	
	/** Creates a new board with the setup specified in BoardMap
	 *  Precondition: BoardMap exists in the project folder and has been properly initialized
	 *  Postcondition: a new board with the setup specified in BoardMap has been created and returned
	 */
	public Board () {
		board = new int[NUM_ROWS][NUM_COLUMNS];
		newGame();
	}
	
	/** Checks if two points on the board are adjacent and returns the direction they are adjacent in
	 *  Precondition: row1, column1, row2, and column2 are valid integers
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
	 *  @return
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
	 * 
	 *  @param originalRow
	 *  @param originalColumn
	 *  @param newRow
	 *  @param newColumn
	 *  @param alreadyChecked
	 *  @return
	 */
	private boolean canJump(int originalRow, int originalColumn, int newRow, int newColumn, int[][] alreadyChecked){
		if (alreadyChecked[originalRow][originalColumn] == 1)
			return false;
		alreadyChecked[originalRow][originalColumn] = 1;
		for (int direction = 1; direction <= 6; direction++) {
			if (direction != 1 && direction != 4) {
				Point otherSpot = getAdjacent(originalRow, originalColumn, direction);
				if (board[(int)otherSpot.getX()][(int)otherSpot.getY()] > 0) {
					Point jumpSpot = getAdjacent(otherSpot, direction);
					if (isValidPoint(jumpSpot) &&
							(((int)jumpSpot.getX() == newRow && (int)jumpSpot.getY() == newColumn) ||
							canJump((int)jumpSpot.getX(), (int)jumpSpot.getY(), newRow, newColumn, alreadyChecked)))
						return true;					
				}
			}
		}
		return false;
	}
	
	/** Checks if a move is valid
	 * 
	 *  @param originalRow
	 *  @param originalColumn
	 *  @param newRow
	 *  @param newColumn
	 *  @return
	 */
	public boolean isValidMove(int originalRow, int originalColumn, int newRow, int newColumn) {
		if (newRow >= NUM_ROWS || newColumn >= NUM_COLUMNS || board[newRow][newColumn] != 0)
			return false;
		if (isAdjacent(originalRow, originalColumn, newRow, newColumn) > 0)
			return true;
		return canJump(originalRow, originalColumn, newRow, newColumn, new int[NUM_ROWS][NUM_COLUMNS]);
	}
	
	/** Moves a piece
	 *  
	 *  @param originalRow
	 *  @param originalColumn
	 *  @param newRow
	 *  @param newColumn
	 *  @return
	 */
	public boolean move(int originalRow, int originalColumn, int newRow, int newColumn) {
		if (isValidMove(originalRow, originalColumn, newRow, newColumn)) {
			board[newRow][newColumn] = board[originalRow][originalColumn];
			board[originalRow][originalColumn] = 0;
			return true;
		}
		return false;
	}
	
	/** Initializes the board for a new game
	 * 
	 *  @return
	 */
	public boolean newGame() {
		try {
			File boardMap = new File("BoardMap");
			Scanner inFile = new Scanner(boardMap);
			
			// Block off ineligible spots
			for (int row = 0; row < NUM_ROWS; row++) {
				String boardRow = inFile.nextLine();
				for (int column = 0; column < NUM_COLUMNS; column++) {
					if (boardRow.charAt(column) == 'x')
						board[row][column] = -1;
					else
						board[row][column] = Character.getNumericValue(boardRow.charAt(column));
				}
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
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
		Board newBoard = new Board();
		BoardDisplay window = new BoardDisplay(newBoard.board);
		//window.declareWinner(6,5);
	}

}
