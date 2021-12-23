import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ClientTest_v2 {
	
	public static byte[] bytes = "Hello World".getBytes();


	public static void main(String[] args) {
		

		String host = "dist.cs.uafs.edu";
		int port = 32261;
		System.out.println("Starting Client");
		
		

		try {
			Socket socket = new Socket(host, port);
			
			DataOutputStream bytesOut = new DataOutputStream(socket.getOutputStream());
			
			bytesOut.writeInt(bytes.length);
			bytesOut.write(bytes);
			
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