import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Board {
	
	private int[][] board;
	
	static final int DIRECTION_NORTHWEST = 1;
	static final int DIRECTION_NORTHEAST = 2;
	static final int DIRECTION_EAST = 3;
	static final int DIRECTION_SOUTHEAST = 4;
	static final int DIRECTION_SOUTHWEST = 5;
	static final int DIRECTION_WEST = 6;
	
	public Board () throws FileNotFoundException {
		File boardMap = new File("BoardMap");
		Scanner inFile = new Scanner(boardMap);
		
		board = new int[17][17];
		
		// Block off ineligible spots
		for (int row = 0; row < board.length; row++) {
			String boardRow = inFile.nextLine();
			for (int column = 0; column < board.length; column++) {
				if (boardRow.charAt(column) == 'x')
					board[row][column] = -1;
				else
					board[row][column] = Character.getNumericValue(boardRow.charAt(column));
			}
		}
	}
	
	/** Checks if two points on the board are adjacent and returns the direction they are adjacent in
	 * 
	 *  @param x1
	 *  @param y1
	 *  @param x2
	 *  @param y2
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
	
	public Point getAdjacent(Point coordinates, int direction) {
		return getAdjacent((int)coordinates.getX(), (int)coordinates.getY(), direction);
	}
	
	public boolean isValidPoint(Point coordinates) {
		return (coordinates.getX() < 17 && coordinates.getX() >= 0 && coordinates.getY() >= 0 && coordinates.getY() < 17 &&
				board[(int)coordinates.getX()][(int)coordinates.getY()] > -1);
	}
	
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
	
	public boolean canJump(int originalRow, int originalColumn, int newRow, int newColumn, int[][] alreadyChecked){
		if (alreadyChecked[originalRow][originalColumn] == 1)
			return false;
		alreadyChecked[originalRow][originalColumn] = 1;
		for (int direction = 1; direction <= 6; direction++) {
			Point otherSpot = getAdjacent(originalRow, originalColumn, direction);
			if (board[(int)otherSpot.getX()][(int)otherSpot.getY()] > 0) {
				Point jumpSpot = getAdjacent(otherSpot, direction);
				if (isValidPoint(jumpSpot) &&
						(((int)jumpSpot.getX() == newRow && (int)jumpSpot.getY() == newColumn) ||
						canJump((int)jumpSpot.getX(), (int)jumpSpot.getY(), newRow, newColumn, alreadyChecked)))
					return true;					
			}
		}
		return false;
	}
	
	public boolean isValidMove(int originalRow, int originalColumn, int newRow, int newColumn) {
		if (board[newRow][newColumn] != 0)
			return false;
		if (isAdjacent(originalRow, originalColumn, newRow, newColumn) > 0)
			return true;
		return canJump(originalRow, originalColumn, newRow, newColumn, new int[17][17]);
	}
	
	public boolean move(int originalRow, int originalColumn, int newRow, int newColumn) {
		if (isValidMove(originalRow, originalColumn, newRow, newColumn)) {
			board[newRow][newColumn] = board[originalRow][originalColumn];
			board[originalRow][originalColumn] = 0;
			return true;
		}
		return false;
	}
	
	public static void main (String[] args) {
		try {
			Board mainBoard = new Board();
			BoardDisplay window = new BoardDisplay(mainBoard.board);
			
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
