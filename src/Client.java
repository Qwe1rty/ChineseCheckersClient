import java.awt.Font;
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
			//myWriter.println(CLIENT_MOVE + " " + move[0] + " " + move[1] + " " + move[2] + " " + move[3]);
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

	public static void main(String[] args) {
		// Create the client and go
		Client client = new Client();
	}

}
