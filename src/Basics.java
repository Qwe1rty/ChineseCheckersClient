
class Basics {
	int[][] array = new int[17][17];
	int color;
	int[] nextMove = new int[4];
	String go = "4";

	public void Opening() {
		boolean done = false;
		int step = 1;
		if (color == 1) {
			while (!done) {
				if (!this.canMove(nextMove[0], nextMove[1]))
					break;
				if (go.equals("4")) {
					if (step == 1) {
						//y,x
						nextMove[0] = 13;
						nextMove[1] = 12;
						nextMove[2] = 12;
						nextMove[3] = 11;
						//Send move

					} else if (step == 2) {
						nextMove[0] = 15;
						nextMove[1] = 12;
						nextMove[2] = 11;
						nextMove[3] = 10;
					} else if (step == 3) {
						nextMove[0] = 14;
						nextMove[1] = 10;
						nextMove[2] = 10;
						nextMove[3] = 10;
					} else if (step == 4) {
						nextMove[0] = 10;
						nextMove[1] = 10;
						nextMove[2] = 9;
						nextMove[3] = 9;
					} else if (step == 5) {
						nextMove[0] = 16;
						nextMove[1] = 12;
						nextMove[2] = 8;
						nextMove[3] = 8;
						done=true;
					}

					step++;
				}

			}
		} else if (color == 2) {
			if (step == 1) {
				//y,x
				nextMove[0] = 13;
				nextMove[1] = 9;
				nextMove[2] = 1;
				nextMove[3] = 12;
				//Send move

			} else if (step == 2) {
				nextMove[0] = 11;
				nextMove[1] = 15;
				nextMove[2] = 9;
				nextMove[3] = 11;
			} else if (step == 3) {
				nextMove[0] = 12;
				nextMove[1] = 14;
				nextMove[2] = 8;
				nextMove[3] = 10;
			} else if (step == 4) {
				nextMove[0] = 8;
				nextMove[1] = 10;
				nextMove[2] = 8;
				nextMove[3] = 9;
			} else if (step == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				done=true;
			}

			step++;
		}

		else if (color == 3) {
			if (step == 1) {
				//y,x
				nextMove[0] = 7;
				nextMove[1] = 12;
				nextMove[2] = 7;
				nextMove[3] = 11;
				//Send move

			} else if (step == 2) {
				nextMove[0] = 5;
				nextMove[1] = 12;
				nextMove[2] = 7;
				nextMove[3] = 1	;
			} else if (step == 3) {
				nextMove[0] = 4;
				nextMove[1] = 10;
				nextMove[2] = 10;
				nextMove[3] = 10;
			} else if (step == 4) {
				nextMove[0] = 4;
				nextMove[1] = 10;
				nextMove[2] = 8;
				nextMove[3] = 10;
			} else if (step == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				done=true;
			}

			step++;
		} else if (color == 4) {
			if (step == 1) {
				//y,x
				nextMove[0] = 12;
				nextMove[1] = 13;
				nextMove[2] = 11;
				nextMove[3] = 12;
				//Send move

			} else if (step == 2) {
				nextMove[0] = 12;
				nextMove[1] = 15;
				nextMove[2] = 10;
				nextMove[3] = 11;
			} else if (step == 3) {
				nextMove[0] = 10;
				nextMove[1] = 14;
				nextMove[2] = 10;
				nextMove[3] = 10;
			} else if (step == 4) {
				nextMove[0] = 10;
				nextMove[1] = 10;
				nextMove[2] = 9;
				nextMove[3] = 9;
			} else if (step == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				done=true;
			}

			step++;
		} else if (color == 5) {
			if (step == 1) {
				//y,x
				nextMove[0] = 12;
				nextMove[1] = 13;
				nextMove[2] = 11;
				nextMove[3] = 12;
				//Send move

			} else if (step == 2) {
				nextMove[0] = 12;
				nextMove[1] = 15;
				nextMove[2] = 10;
				nextMove[3] = 11;
			} else if (step == 3) {
				nextMove[0] = 10;
				nextMove[1] = 14;
				nextMove[2] = 10;
				nextMove[3] = 10;
			} else if (step == 4) {
				nextMove[0] = 10;
				nextMove[1] = 10;
				nextMove[2] = 9;
				nextMove[3] = 9;
			} else if (step == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				done=true;
			}

			step++;
		} else if (color == 6) {
			if (step == 1) {
				//y,x
				nextMove[0] = 12;
				nextMove[1] = 13;
				nextMove[2] = 11;
				nextMove[3] = 12;
				//Send move

			} else if (step == 2) {
				nextMove[0] = 12;
				nextMove[1] = 15;
				nextMove[2] = 10;
				nextMove[3] = 11;
			} else if (step == 3) {
				nextMove[0] = 10;
				nextMove[1] = 14;
				nextMove[2] = 10;
				nextMove[3] = 10;
			} else if (step == 4) {
				nextMove[0] = 10;
				nextMove[1] = 10;
				nextMove[2] = 9;
				nextMove[3] = 9;
			} else if (step == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				done=true;
			}

			step++;
		}

	}

	public boolean canMove(int x, int y)

	{

		return true;
	}

}