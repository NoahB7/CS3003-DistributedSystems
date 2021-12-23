import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client_v2 {
	
	//LIMITATIONS FOR THIS VERSION OF CLIENT:
	//I couldnt figure out how to make it execute multiple commands through one connection
	//must connect send a command and exit

	public static void main(String[] args) {

		String host = "127.0.0.1";
		int port = 32261;
		System.out.println("Starting Client");

		try {
			Socket socket = new Socket(host, port);


			//pw sends data to server
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			//recieves data from server
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String input;

			//assumes it comes back on one line
			while ((input = br.readLine()) != null) {
				System.out.println(input);

				String message = JOptionPane.showInputDialog("Enter message:");
				if (message.equalsIgnoreCase("exit")) {
					
					//Im not sure what this extra while loop is, its an awful way to do this but for some
					//reason I can only get the last message(the data we want back to the user)
					//this way only for some reason
					
						System.out.println(input = br.readLine());
						
						socket.close();
						System.exit(0);
					
					
				}
				pw.println(message); // message to send
			}

			System.out.println("Client finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}