import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Enter the Server IP Address: ");
		String IPAddress = keyboard.nextLine();

		Socket mySocket;
		try {
			mySocket = new Socket(IPAddress, 1337);
			// Make reader
			InputStreamReader myStream = new InputStreamReader(mySocket.getInputStream());
			BufferedReader myReader = new BufferedReader(myStream);
			
			// Make writer
			PrintWriter myWriter = new PrintWriter(mySocket.getOutputStream());			

			while(true) {
				try {
					if (myReader.ready()){
						String line = myReader.readLine();
						System.out.println(line);
					}
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
