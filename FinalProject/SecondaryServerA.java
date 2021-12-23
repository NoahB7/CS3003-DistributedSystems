import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * 
 * @author noah_
 * Server 1 that holds data for users
 *
 */

public class SecondaryServerA {

	private static HashMap<String, ArrayList<File>> userMap = new HashMap<>();
	
	/**
	 * This method writes logs of all commands so that in the case of server failure it can be retrieved
	 * @param user User to be stored in logs
	 * @param command Command to be stored in logs
	 * @param filename Name of file to be stored in logs
	 * @throws IOException
	 */
	
	public static void serverHistory(String user, String command, String filename) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter("serverHistoryA.txt", true));
		String text = command + " " + filename + " " + user + "";
		for (int i = 0; i < text.length(); i++) {
			br.append(text.charAt(i));
		}
		br.append("\n");
		br.close();
	}
	
	/**
	 * Listens for a connection from Master Server
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {

		if (args.length < 1) {
			System.out.println("Invalid syntax: java secondaryServerA_v4 portnum numthreads");
			System.exit(10);
		}

		int port = Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);
		int nthreads = Integer.parseInt(args[1]);

		System.out.println("Server2A is running on " + port);
		ExecutorService threadPool = Executors.newFixedThreadPool(nthreads);

		while (true) {
			Socket sock = server.accept();
			threadPool.execute(new UAClientThread(sock));
		}
	}
	
	/**
	 * Thread for each connection
	 * @author noah_
	 *
	 */

	public static class UAClientThread implements Runnable {
		private Socket socket;

		/**
		 * Constructor for passing the socket connection from main to this Thread
		 * @param socket socket passed from secondary servers main method
		 */
		public UAClientThread(Socket socket) {
			this.socket = socket;
		}
		
		/**
		 * Code that executes for each thread
		 */

		@Override
		public void run() {

			System.out.println("New connection established: " + socket);
			try {
				

				DataInputStream bytesIn = new DataInputStream(socket.getInputStream());

				int length = bytesIn.readInt();

				byte[] bytes = new byte[length];
				bytesIn.readFully(bytes, 0, bytes.length);

				System.out.println(bytes.length);


				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				// reads data to client
				String input;

				while ((input = br.readLine()) != null) {
					System.out.println("MESSAGE RECEIVED FROM MASTER SERVER: ");
					System.out.println(input);
					// send to client

					// Code Below is what processes the command for what to do with the data from
					// the client
					String[] command = input.split(" ");
					String user = command[2];
					
					serverHistory(command[2], command[0], command[1]);

					// adds empty list for user if user is new and has not been hashed yet
					if (userMap.get(user) == null) {
						userMap.put(user, new ArrayList<File>());
					}

					if (command[0].equals("add")) {

						// creating file specified and writing bytes from original file to new file
						// location is a placeholder THIS WILL NEED TO BE CHANGED WHEN PUT ON THE SERVER
						// just put it in like a specified file on the server

						File file = new File("ServerAlist/" + command[1]);
						try {
							OutputStream os = new FileOutputStream(file);
							os.write(bytes);
							os.close();

						} catch (Exception e) {
							e.printStackTrace();
						}

						synchronized (userMap) {
							userMap.get(user).add(file);
						}

						out.println("File added on server: " + command[1]);

					} else if (command[0].equals("remove")) {

						synchronized (userMap) {

							for (int i = 0; i < userMap.get(user).size(); i++) {

								if (userMap.get(user).get(i).getName().equals(command[1])) {
									userMap.get(user).get(i).delete();
									userMap.get(user).remove(i);
								}
							}

						}

						out.println("File removed on server: " + command[1]);
					} else if (command[0].equalsIgnoreCase("listAll")) {

						StringBuilder build = new StringBuilder();

						synchronized (userMap) {
							for (int i = 0; i < userMap.get(user).size(); i++) {
								build.append(userMap.get(user).get(i).getName() + ", ");
							}
						}

						out.println("Server list all: " + build.toString());

					} else {
						System.out.println("Error on server 2A");
					}

				}

				socket.close();
				System.out.println("The connection 2A was closed: " + socket);

			} catch (Exception ex) {

			}

		}

	}

}