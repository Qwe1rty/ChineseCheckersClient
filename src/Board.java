import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Board {
	
	private int[][] board;
	
	public static final int NUM_ROWS = 17;
	public static final int NUM_COLUMNS = 17;

	static final int DIRECTION_SOUTH = 1;
	static final int DIRECTION_SOUTHEAST = 2;
	static final int DIRECTION_NORTHEAST = 3;
	static final int DIRECTION_NORTH = 4;
	static final int DIRECTION_NORTHWEST = 5;
	static final int DIRECTION_SOUTHWEST = 6;
	static final int DIRECTION_EAST = 7;
	static final int DIRECTION_WEST = 8;
	
	/** Creates a new board with the setup specified in BoardMap
	 * 
	 */
	public Board () {
		board = new int[17][17];
		newGame();
	}
	
	/** Checks if two points on the board are adjacent and returns the direction they are adjacent in
	 * 
	 *  @param row1
	 *  @param column1
	 *  @param row2
	 *  @param column2
	 *  @return
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
	 * 
	 *  @param coordinates
	 *  @return
	 */
	public boolean isValidPoint(Point coordinates) {
		return (coordinates.getX() < NUM_ROWS && coordinates.getX() >= 0 && coordinates.getY() >= 0 
				&& coordinates.getY() < NUM_COLUMNS && board[(int)coordinates.getX()][(int)coordinates.getY()] > -1);
	}
	
	/** Gets the coordinates of the point adjacent to the specified point in the specified direction
	 * 
	 *  @param coordinates
	 *  @param direction
	 *  @return
	 */
	public Point getAdjacent(Point coordinates, int direction) {
		return getAdjacent((int)coordinates.getX(), (int)coordinates.getY(), direction);
	}
	
	/** Gets the coordinates of the point adjacent to the specified point in the specified direction
	 * 
	 *  @param row
	 *  @param column
	 *  @param direction
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
	public boolean canJump(int originalRow, int originalColumn, int newRow, int newColumn, int[][] alreadyChecked){
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
		return canJump(originalRow, originalColumn, newRow, newColumn, new int[17][17]);
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
	
	public int[][] getBoard() {
		return board;
	}

}
