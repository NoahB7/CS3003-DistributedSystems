import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMaster_v3 {

	public static void main(String[] args) throws IOException {

		if (args.length < 1) {
			System.out.println("Invalid suntax: java UAServer prtnum numthreads");
			System.exit(10);
		}
		// port to use for server
		int port = Integer.parseInt(args[0]);
		// max number of threads
		int nthreads = Integer.parseInt(args[1]);

		ServerSocket server = new ServerSocket(port);

		System.out.println("Master Server is running on " + port);
		ExecutorService threadPool = Executors.newFixedThreadPool(nthreads);

		// starts waiting for a server to connect...
		while (true) {
			Socket sock = server.accept();
			threadPool.execute(new UAClientThread(sock));
		}
	}

	// runnable thread
	public static class UAClientThread implements Runnable{

		private Socket socket;
		public String word;

		public UAClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			int port = 0;

			System.out.println("New connection established to client: " + socket);
			try {

				BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter outClient = new PrintWriter(socket.getOutputStream(), true);
				outClient.println("Welcome!");

				// reads data to client
				String input;

				while ((input = inClient.readLine()) != null) {

					port = 0;

					this.word = input;
					outClient.println("MESSAGE RECEIVED"); // send to client

					String[] command = word.split(" ");

					// changes port to one of the two secondary servers
					if (command[1].hashCode() % 2 == 1) {
						port = 32262;
						
						//*****************************************************************
						String host = "127.0.0.1";
						Socket socketSecondary = new Socket(host, port);
						System.out.println("Establishing connection to secondary Server: " + socketSecondary);

						PrintWriter outSecondary = new PrintWriter(socketSecondary.getOutputStream(), true);
						BufferedReader inSecondary = new BufferedReader(new InputStreamReader(socketSecondary.getInputStream()));

						if (this.word.equalsIgnoreCase("exit")) {
							
							socketSecondary.close();
							System.exit(0);
						}
						outSecondary.println(this.word); // send message to server

						System.out.println("Sending message '" + this.word + "' to secondary Server");
						
						String line = "";
						
						while((line = inSecondary.readLine()) != null) {

							outClient.println(line);
						}

						//*******************************************************************
					} else if (command[1].hashCode() % 2 == 0) {
						port = 32263;
						
						//*****************************************************************
						String host = "127.0.0.1";
						Socket socketSecondary = new Socket(host, port);
						System.out.println("Establishing connection to secondary Server: " + socketSecondary);

						PrintWriter outSecondary = new PrintWriter(socketSecondary.getOutputStream(), true);
						BufferedReader inSecondary = new BufferedReader(new InputStreamReader(socketSecondary.getInputStream()));

						if (this.word.equalsIgnoreCase("exit")) {
							
							socketSecondary.close();
							System.exit(0);
						}
						outSecondary.println(this.word); // send message to server

						System.out.println("Sending message '" + this.word + "' to secondary Server");
						
						String line = "";
						
						while((line = inSecondary.readLine()) != null) {

							outClient.println(line);
						}

					
						//*******************************************************************
					} else {
						System.out.println("Error selecting secondary server to send to..");
					}

				}
				
				socket.close();
				System.out.println("Client finished");
				//CONNECTS TO SECONDARY SERVER BELOW, ABOVE IS CLIENT CONNECTING TO MASTER SERVER
				//CANT HAVE TWO TRY CATCHES OR THE DATA FLOW BACKWARDS DOESNT WORK
				


			} catch (Exception ex) {

			}

		}

	}

}