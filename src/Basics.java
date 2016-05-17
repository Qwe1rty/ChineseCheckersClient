import java.awt.Point;

class Basics {
	int[][] board = new int[17][17];
	int color;
	int[] nextMove = new int[4];
	String go = "4";

	static final int DIRECTION_NORTHWEST = 1;
	static final int DIRECTION_NORTHEAST = 2;
	static final int DIRECTION_EAST = 3;
	static final int DIRECTION_SOUTHEAST = 4;
	static final int DIRECTION_SOUTHWEST = 5;
	static final int DIRECTION_WEST = 6;
	
	
	
	
	
	public 
	
	
	
	
	

	public void Opening() {
		boolean done = false;
		int step = 1;

		while (!done) {

			if (go.equals("4")) {
				if (color == 1) {
					if (step == 1) {
						// y,x
						nextMove[0] = 13;
						nextMove[1] = 12;
						nextMove[2] = 12;
						nextMove[3] = 11;
						if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
							return;
						// Send move

					} else if (step == 2) {
						nextMove[0] = 15;
						nextMove[1] = 12;
						nextMove[2] = 11;
						nextMove[3] = 10;
						if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
							return;
					} else if (step == 3) {
						nextMove[0] = 14;
						nextMove[1] = 10;
						nextMove[2] = 10;
						nextMove[3] = 10;
						if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
							return;
					} else if (step == 4) {
						nextMove[0] = 10;
						nextMove[1] = 10;
						nextMove[2] = 9;
						nextMove[3] = 9;
						if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
							return;
					} else if (step == 5) {
						nextMove[0] = 16;
						nextMove[1] = 12;
						nextMove[2] = 8;
						nextMove[3] = 8;
						if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
							return;

						return;
					}

					step++;
				}

			}

			else if (color == 2) {
				if (step == 1) {
					// y,x
					nextMove[0] = 13;
					nextMove[1] = 9;
					nextMove[2] = 1;
					nextMove[3] = 12;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
					// Send move

				} else if (step == 2) {
					nextMove[0] = 11;
					nextMove[1] = 15;
					nextMove[2] = 9;
					nextMove[3] = 11;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 3) {
					nextMove[0] = 12;
					nextMove[1] = 14;
					nextMove[2] = 8;
					nextMove[3] = 10;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 4) {
					nextMove[0] = 8;
					nextMove[1] = 10;
					nextMove[2] = 8;
					nextMove[3] = 9;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 5) {
					nextMove[0] = 12;
					nextMove[1] = 16;
					nextMove[2] = 8;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				
				}

				step++;
			}

			else if (color == 3) {
				if (step == 1) {
					// y,x
					nextMove[0] = 7;
					nextMove[1] = 12;
					nextMove[2] = 7;
					nextMove[3] = 11;
					// Send move
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;

				} else if (step == 2) {
					nextMove[0] = 5;
					nextMove[1] = 12;
					nextMove[2] = 7;
					nextMove[3] = 1;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 3) {
					nextMove[0] = 4;
					nextMove[1] = 10;
					nextMove[2] = 10;
					nextMove[3] = 10;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 4) {
					nextMove[0] = 4;
					nextMove[1] = 10;
					nextMove[2] = 8;
					nextMove[3] = 10;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 5) {
					nextMove[0] = 12;
					nextMove[1] = 16;
					nextMove[2] = 8;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				}

				step++;
			} else if (color == 4) {
				if (step == 1) {
					// y,x
					nextMove[0] = 3;
					nextMove[1] = 7;
					nextMove[2] = 4;
					nextMove[3] = 7;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
					// Send move

				} else if (step == 2) {
					nextMove[0] = 1;
					nextMove[1] = 5;
					nextMove[2] = 5;
					nextMove[3] = 7;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 3) {
					nextMove[0] = 2;
					nextMove[1] = 4;
					nextMove[2] = 6;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 4) {
					nextMove[0] = 6;
					nextMove[1] = 8;
					nextMove[2] = 7;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 5) {
					nextMove[0] = 0;
					nextMove[1] = 4;
					nextMove[2] = 8;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
					
				}

				step++;
			} else if (color == 5) {
				if (step == 1) {
					// y,x
					nextMove[0] = 4;
					nextMove[1] = 3;
					nextMove[2] = 5;
					nextMove[3] = 4;
					// Send move
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 2) {
					nextMove[0] = 4;
					nextMove[1] = 1;
					nextMove[2] = 6;
					nextMove[3] = 5;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 3) {
					nextMove[0] = 6;
					nextMove[1] = 2;
					nextMove[2] = 6;
					nextMove[3] = 6;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 4) {
					nextMove[0] = 6;
					nextMove[1] = 6;
					nextMove[2] = 7;
					nextMove[3] = 7;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 5) {
					nextMove[0] = 4;
					nextMove[1] = 0;
					nextMove[2] = 8;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
					
				}

				step++;
			} else if (color == 6) {
				if (step == 1) {
					// y,x
					nextMove[0] = 9;
					nextMove[1] = 3;
					nextMove[2] = 9;
					nextMove[3] = 5;
					// Send move
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 2) {
					nextMove[0] = 11;
					nextMove[1] = 4;
					nextMove[2] = 9;
					nextMove[3] = 6;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 3) {
					nextMove[0] = 12;
					nextMove[1] = 6;
					nextMove[2] = 8;
					nextMove[3] = 6;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 4) {
					nextMove[0] = 8;
					nextMove[1] = 6;
					nextMove[2] = 8;
					nextMove[3] = 7;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
				} else if (step == 5) {
					nextMove[0] = 12;
					nextMove[1] = 4;
					nextMove[2] = 8;
					nextMove[3] = 8;
					if (!this.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
						return;
					
					
				}

				step++;
			}
		}
	}

	/**
	 * Checks if two points on the board are adjacent and returns the direction
	 * they are adjacent in
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int isAdjacent(int x1, int y1, int x2, int y2) {
		if (x1 == x2 && y1 + 1 == y2)
			return DIRECTION_SOUTHWEST;
		if (x1 == x2 && y1 - 1 == y2)
			return DIRECTION_NORTHEAST;
		if (y1 == y2 && x1 + 1 == x2)
			return DIRECTION_EAST;
		if (y1 == y2 && x1 - 1 == x2)
			return DIRECTION_WEST;
		if (x1 + 1 == x2 && y1 + 1 == y2)
			return DIRECTION_SOUTHEAST;
		if (x1 - 1 == x2 && y1 - 1 == y2)
			return DIRECTION_NORTHWEST;
		return 0;
	}

	public Point getAdjacent(Point coordinates, int direction) {
		return getAdjacent((int) coordinates.getX(), (int) coordinates.getY(), direction);
	}

	public Point getAdjacent(int x, int y, int direction) {
		if (direction == DIRECTION_SOUTHWEST)
			return new Point(x, y + 1);
		if (direction == DIRECTION_NORTHEAST)
			return new Point(x, y - 1);
		if (direction == DIRECTION_EAST)
			return new Point(x + 1, y);
		if (direction == DIRECTION_WEST)
			return new Point(x - 1, y);
		if (direction == DIRECTION_SOUTHEAST)
			return new Point(x + 1, y + 1);
		if (direction == DIRECTION_NORTHWEST)
			return new Point(x - 1, y - 1);
		return null;
	}

	public boolean canJump(int originalX, int originalY, int newX, int newY) {
		for (int direction = 1; direction <= 6; direction++) {
			Point otherSpot = getAdjacent(originalX, originalY, direction);
			if (board[(int) otherSpot.getX()][(int) otherSpot.getY()] > 0) {
				Point jumpSpot = getAdjacent(otherSpot, direction);
				return (((int) jumpSpot.getX() == newX && (int) jumpSpot.getY() == newY)
						|| canJump((int) jumpSpot.getX(), (int) jumpSpot.getY(), newX, newY));
			}
		}
		return false;
	}

	public boolean isValidMove(int originalX, int originalY, int newX, int newY) {
		if (board[newX][newY] != 0)
			return false;
		if (isAdjacent(originalX, originalY, newX, newY) > 0)
			return true;
		return canJump(originalX, originalY, newX, newY);
	}

	public boolean move(int originalX, int originalY, int newX, int newY) {
		if (isValidMove(originalX, originalY, newX, newY)) {
			board[newX][newY] = board[originalX][originalY];
			board[originalX][originalY] = 0;
			return true;
		}
		return false;
	}

}