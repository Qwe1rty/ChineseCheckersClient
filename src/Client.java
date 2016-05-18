import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	
	public static final int SERVER_PORT = 420;
	
	public static final int SERVER_MOVE = 1;
	public static final int SERVER_NEW_GAME = 2;
	public static final int SERVER_PLACE_PIECE = 3;
	public static final int SERVER_TURN = 4;
	public static final int SERVER_INVALID_MOVE = 5;
	public static final int SERVER_MOVE_TIMEOUT = 6;
	public static final int SERVER_WIN = 7;
	
	public static final int CLIENT_MOVE = 1;
	
	private InputStreamReader myStream;
	private BufferedReader myReader;
	private PrintWriter myWriter;
	
	private Board board;
	private int player;
	private int currentTurn;
	
	BoardDisplay boardWindow;
	
	/** Creates and sets up a new client
	 * 
	 */
	public Client() {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Enter the Server IP Address: ");
		String IPAddress = keyboard.nextLine();

		Socket mySocket;
		try {
			mySocket = new Socket(IPAddress, SERVER_PORT);
			// Make reader
			myStream = new InputStreamReader(mySocket.getInputStream());
			myReader = new BufferedReader(myStream);
			
			// Make writer
			myWriter = new PrintWriter(mySocket.getOutputStream());	
			
			// Create board
			board = new Board();
			boardWindow = new BoardDisplay(board.getBoard());

			receiveFromServer();			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** Sends a move to the server
	 * 
	 *  @param originalRow
	 *  @param originalColumn
	 *  @param newRow
	 *  @param newColumn
	 *  @return
	 */
	public boolean sendMove(int originalRow, int originalColumn, int newRow, int newColumn) {
		myWriter.println(CLIENT_MOVE + " " + originalRow + " " + originalColumn + " " + newRow + " " + newColumn);
		myWriter.flush();
		return true;
	}
	
	/** Waits for and receives messages from the server
	 *  
	 */
	public void receiveFromServer() {
		while(true) {
			try {
				if (myReader.ready()){
					String input = myReader.readLine();
					System.out.println(input);
					read(input);
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/** Reads the message from the server and executes the appropriate actions
	 * 
	 *  @param messageType
	 *  @throws IOException
	 */
	private void read(String input) throws IOException {
		String[] message = input.split(" ");
		System.out.println(message);
		int messageType = Integer.parseInt(message[0]);
		if (messageType == SERVER_MOVE) {
			int originalRow = Integer.parseInt(message[1]);
			int originalColumn = Integer.parseInt(message[2]);
			int newRow = Integer.parseInt(message[3]);
			int newColumn = Integer.parseInt(message[4]);
			board.move(originalRow, originalColumn, newRow, newColumn);
			boardWindow.refresh();
			System.out.println("Move " + newRow + " " + newColumn);
		}
		else if (messageType == SERVER_NEW_GAME) {
			player = Integer.parseInt(message[1]);
			board.newGame();
			boardWindow.refresh();
			boardWindow.setPlayer(player);
			currentTurn = 0;
			System.out.println("New Game");
		}
		else if (messageType == SERVER_PLACE_PIECE) {
			int colour = Integer.parseInt(message[1]);
			int row = Integer.parseInt(message[2]);
			int column = Integer.parseInt(message[3]);
			board.getBoard()[row][column] = colour;
			boardWindow.refresh();
			System.out.println("Place Piece");
		}
		else if (messageType == SERVER_TURN) {
			//int[] move = Algorithm.makeMove(board, player);
			int[] move = opening();
			myWriter.println(CLIENT_MOVE + " " + move[0] + " " + move[1] + " " + move[2] + " " + move[3]);
			currentTurn++;
			System.out.println("Turn");
		}
		else if (messageType == SERVER_INVALID_MOVE) {
			// To do
			System.out.println("Invalid Move");
		}
		else if (messageType == SERVER_MOVE_TIMEOUT) {
			// To do
			System.out.println("Timeout");
		}
		else if (messageType == SERVER_WIN) {
			int winner = Integer.parseInt(message[1]);
			boardWindow.declareWinner(winner, player);
			System.out.println("Win");
		}
	}
	
	/** Gets the board 
	 *  @return
	 */
	public Board getBoard() {
		return board;
	}

	/** Gets the player that the client is playing
	 *  @return
	 */
	public int getPlayer() {
		return player;
	}

	/** Sets the board
	 *  @param board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/** Sets the player that this client is playing
	 *  @param player
	 */
	public void setPlayer(int player) {
		this.player = player;
	}

	/** Author: Ray Luo
	 * 
	 * @return
	 */
	public int[] opening() {
		int[] nextMove = new int[4];
		if (player == 1) {
			if (currentTurn == 1) {
				// y,x
				nextMove[0] = 13;
				nextMove[1] = 12;
				nextMove[2] = 12;
				nextMove[3] = 11;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
				// Send move

			} else if (currentTurn == 2) {
				nextMove[0] = 15;
				nextMove[1] = 12;
				nextMove[2] = 11;
				nextMove[3] = 10;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 3) {
				nextMove[0] = 14;
				nextMove[1] = 10;
				nextMove[2] = 10;
				nextMove[3] = 10;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 4) {
				nextMove[0] = 10;
				nextMove[1] = 10;
				nextMove[2] = 9;
				nextMove[3] = 9;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 5) {
				nextMove[0] = 16;
				nextMove[1] = 12;
				nextMove[2] = 8;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;

				return nextMove;
			}

			 
		}

		else if (player == 2) {
			if (currentTurn == 1) {
				// y,x
				nextMove[0] = 13;
				nextMove[1] = 9;
				nextMove[2] = 1;
				nextMove[3] = 12;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
				// Send move

			} else if (currentTurn == 2) {
				nextMove[0] = 11;
				nextMove[1] = 15;
				nextMove[2] = 9;
				nextMove[3] = 11;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 3) {
				nextMove[0] = 12;
				nextMove[1] = 14;
				nextMove[2] = 8;
				nextMove[3] = 10;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 4) {
				nextMove[0] = 8;
				nextMove[1] = 10;
				nextMove[2] = 8;
				nextMove[3] = 9;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;

			}

			 
		}

		else if (player == 3) {
			if (currentTurn == 1) {
				// y,x
				nextMove[0] = 7;
				nextMove[1] = 12;
				nextMove[2] = 7;
				nextMove[3] = 11;
				// Send move
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;

			} else if (currentTurn == 2) {
				nextMove[0] = 5;
				nextMove[1] = 12;
				nextMove[2] = 7;
				nextMove[3] = 1;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 3) {
				nextMove[0] = 4;
				nextMove[1] = 10;
				nextMove[2] = 10;
				nextMove[3] = 10;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 4) {
				nextMove[0] = 4;
				nextMove[1] = 10;
				nextMove[2] = 8;
				nextMove[3] = 10;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 5) {
				nextMove[0] = 12;
				nextMove[1] = 16;
				nextMove[2] = 8;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			}

			 
		} else if (player == 4) {
			if (currentTurn == 1) {
				// y,x
				nextMove[0] = 3;
				nextMove[1] = 7;
				nextMove[2] = 4;
				nextMove[3] = 7;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
				// Send move

			} else if (currentTurn == 2) {
				nextMove[0] = 1;
				nextMove[1] = 5;
				nextMove[2] = 5;
				nextMove[3] = 7;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 3) {
				nextMove[0] = 2;
				nextMove[1] = 4;
				nextMove[2] = 6;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 4) {
				nextMove[0] = 6;
				nextMove[1] = 8;
				nextMove[2] = 7;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 5) {
				nextMove[0] = 0;
				nextMove[1] = 4;
				nextMove[2] = 8;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;

			}

			 
		} else if (player == 5) {
			if (currentTurn == 1) {
				// y,x
				nextMove[0] = 4;
				nextMove[1] = 3;
				nextMove[2] = 5;
				nextMove[3] = 4;
				// Send move
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 2) {
				nextMove[0] = 4;
				nextMove[1] = 1;
				nextMove[2] = 6;
				nextMove[3] = 5;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 3) {
				nextMove[0] = 6;
				nextMove[1] = 2;
				nextMove[2] = 6;
				nextMove[3] = 6;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 4) {
				nextMove[0] = 6;
				nextMove[1] = 6;
				nextMove[2] = 7;
				nextMove[3] = 7;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 5) {
				nextMove[0] = 4;
				nextMove[1] = 0;
				nextMove[2] = 8;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;

			}

			 
		} else if (player == 6) {
			if (currentTurn == 1) {
				// y,x
				nextMove[0] = 9;
				nextMove[1] = 3;
				nextMove[2] = 9;
				nextMove[3] = 5;
				// Send move
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 2) {
				nextMove[0] = 11;
				nextMove[1] = 4;
				nextMove[2] = 9;
				nextMove[3] = 6;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 3) {
				nextMove[0] = 12;
				nextMove[1] = 6;
				nextMove[2] = 8;
				nextMove[3] = 6;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 4) {
				nextMove[0] = 8;
				nextMove[1] = 6;
				nextMove[2] = 8;
				nextMove[3] = 7;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;
			} else if (currentTurn == 5) {
				nextMove[0] = 12;
				nextMove[1] = 4;
				nextMove[2] = 8;
				nextMove[3] = 8;
				if (!board.isValidMove(nextMove[0], nextMove[1], nextMove[3], nextMove[4]))
					return nextMove;

			}

			 
		}
		return nextMove;
	}
	
	
	
	public static void main(String[] args) {
		// Create the client and go
		Client client = new Client();
	}

}
