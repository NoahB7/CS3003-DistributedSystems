import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author noah_
 * 
 *         Master Server for group 7's implementation Client connects to this
 *         server and this server then connects to one of the two secondary
 *         servers and passes the command and user along to it from the client
 *
 */

public class ServerMaster {

	// what are used to keep track of the server statuses
	public static boolean firstServerDown = false;
	public static boolean secondServerDown = false;

	/**
	 * 
	 * @param args
	 * @throws IOException Just listens for a connection from a client and passes it
	 *                     to a Thread when a connection is made
	 */

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

	/**
	 * 
	 * @author noah_ Thread for each connection, global variable socket for passing
	 *         the socket from main to here and the String "word" represents the
	 *         total command of [command file user] and is used to hold it inbetween
	 *         portions of the execution
	 *
	 */

	// runnable thread
	public static class UAClientThread implements Runnable {

		private Socket socket;
		public String word;

		/**
		 * Constructor for Thread class to pass socket from main
		 * 
		 * @param socket socket passed into Thread class from main
		 */

		public UAClientThread(Socket socket) {
			this.socket = socket;
		}

		/**
		 * code that executes for each thread that connects
		 */

		@Override
		public void run() {

			// these are initialized outside try catch so that I can access them in the
			// finally to send a message
			// back to the client in the case that one of the servers are down
			int port = 0;
			Socket socketSecondary = null;
			PrintWriter outClient = null;

			System.out.println("New connection established to client: " + socket);
			try {

				DataInputStream bytesIn = new DataInputStream(socket.getInputStream());
				int length = bytesIn.readInt();
				byte[] bytes = new byte[length];
				bytesIn.readFully(bytes, 0, bytes.length);

				BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outClient = new PrintWriter(socket.getOutputStream(), true);
				outClient.println("Welcome!");

				// reads data to client
				String input;

				while ((input = inClient.readLine()) != null) {

					port = 0;
					this.word = input;
					outClient.println("MESSAGE RECEIVED"); // send to client
					String[] command = word.split(" ");

					// tests if server rebooted

					System.out.println(command[2]);

					// sends to first server if it is not down or the second server is down
					// basically send it here if this one is up or the other one is down since we
					// only have two
					if ((command[2].hashCode() % 2 == 1 && !firstServerDown) || secondServerDown) {
						port = 32262;

						String host = "dist.cs.uafs.edu";
						socketSecondary = new Socket(host, port);
						System.out.println("Establishing connection to secondary Server: " + socketSecondary);

						DataOutputStream bytesOutSecondary = new DataOutputStream(socketSecondary.getOutputStream());
						bytesOutSecondary.writeInt(bytes.length);
						bytesOutSecondary.write(bytes);

						PrintWriter outSecondary = new PrintWriter(socketSecondary.getOutputStream(), true);
						BufferedReader inSecondary = new BufferedReader(
								new InputStreamReader(socketSecondary.getInputStream()));

						if (this.word.equalsIgnoreCase("exit")) {

							socketSecondary.close();
							System.exit(0);
						}
						outSecondary.println(this.word); // send message to server

						System.out.println("Sending message '" + this.word + "' to secondary Server");
						String line = "";

						while ((line = inSecondary.readLine()) != null) {

							outClient.println(line);
						}

						// same logic here for this if as the first server redirect just for the second
						// server
					} else if ((command[2].hashCode() % 2 == 0 && !secondServerDown) || firstServerDown) {
						port = 32263;

						String host = "192.168.50.207";
						socketSecondary = new Socket(host, port);
						System.out.println("Establishing connection to secondary Server: " + socketSecondary);

						DataOutputStream bytesOutSecondary = new DataOutputStream(socketSecondary.getOutputStream());
						bytesOutSecondary.writeInt(bytes.length);
						bytesOutSecondary.write(bytes);

						PrintWriter outSecondary = new PrintWriter(socketSecondary.getOutputStream(), true);
						BufferedReader inSecondary = new BufferedReader(
								new InputStreamReader(socketSecondary.getInputStream()));

						if (this.word.equalsIgnoreCase("exit")) {

							socketSecondary.close();
							System.exit(0);
						}
						outSecondary.println(this.word); // send message to server
						System.out.println("Sending message '" + this.word + "' to secondary Server");

						String line = "";

						while ((line = inSecondary.readLine()) != null) {

							outClient.println(line);
						}

					} else {
						System.out.println("Error selecting secondary server to send to..");
					}

				}
				
				socket.close();
				System.out.println("Client finished");

			} catch (Exception ex) {
				// nothing here
			} finally {

				// in the case that one of the servers is shutdown when we attempt to connect
				if (socketSecondary == null && port == 32262) {
					firstServerDown = true;
					outClient.println("Server 2A down, rerouting you now..");
				} else if (socketSecondary == null && port == 32263) {
					secondServerDown = true;
					outClient.println("Server 2B down, rerouting you now..");
				}
			}

		}

	}

}