import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	
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
	
	BoardDisplay boardWindow;
	
	public Client() {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Enter the Server IP Address: ");
		String IPAddress = keyboard.nextLine();

		Socket mySocket;
		try {
			mySocket = new Socket(IPAddress, 1337);
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
	
	public boolean sendMove(int originalRow, int originalColumn, int newRow, int newColumn) {
		myWriter.println(CLIENT_MOVE);
		myWriter.println(originalRow);
		myWriter.println(originalColumn);
		myWriter.println(newRow);
		myWriter.println(newColumn);
		myWriter.flush();
		return true;
	}
	
	public void receiveFromServer() {
		while(true) {
			try {
				if (myReader.ready()){
					int messageType = myReader.read();
					System.out.println(messageType);
					read(messageType);
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	private void read(int messageType) throws IOException {
		if (messageType == SERVER_MOVE) {
			int originalRow = myReader.read();
			int originalColumn = myReader.read();
			int newRow = myReader.read();
			int newColumn = myReader.read();
			board.move(originalRow, originalColumn, newRow, newColumn);
			boardWindow.refresh();
		}
		else if (messageType == SERVER_NEW_GAME) {
			player = myReader.read();
			board.newGame();
			boardWindow.refresh();
		}
		else if (messageType == SERVER_PLACE_PIECE) {
			int colour = myReader.read();
			int row = myReader.read();
			int column = myReader.read();
			board.getBoard()[row][column] = colour;
		}
		else if (messageType == SERVER_TURN) {
			//AI.makeMove();
		}
		else if (messageType == SERVER_INVALID_MOVE) {
			// To do
		}
		else if (messageType == SERVER_MOVE_TIMEOUT) {
			// To do
		}
		else if (messageType == SERVER_WIN) {
			// To do
		}
	}
	
	public Board getBoard() {
		return board;
	}

	public int getPlayer() {
		return player;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public static void main(String[] args) {
		Client client = new Client();
	}

}
