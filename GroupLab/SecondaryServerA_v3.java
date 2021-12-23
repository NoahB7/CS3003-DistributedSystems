import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Note: this is a static implementation of the list, I'm not sure if this is what he wanted but the data
//persists through connections this way so it works.

public class SecondaryServerA_v3 {

	private static ArrayList<String> fileList = new ArrayList<>();

	//Nothing new here just waiting for main server to connect
	public static void main(String[] args) throws IOException {
		
		if (args.length < 1) {
			System.out.println("Invalid syntax: java UAServer prtnum numthreads");
			System.exit(10);
		}
		int port = Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);
		int nthreads = Integer.parseInt(args[1]);

		System.out.println("Server2 is running on " + port);
		ExecutorService threadPool = Executors.newFixedThreadPool(nthreads);

		while (true) {
			Socket sock = server.accept();
			threadPool.execute(new UAClientThread(sock));
		}
	}

	public static class UAClientThread implements Runnable {
		private Socket socket;

		// need as a requirement to connect
		public UAClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			System.out.println("New connection established: " + socket);
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


				// reads data to client
				String input;

				while ((input = br.readLine()) != null) {
					System.out.println("MESSAGE RECEIVED FROM MASTER SERVER: ");
					System.out.println(input);
					 // send to client

					// Code Below is what processes the command for what to do with the data from the client
					String[] command = input.split(" ");
					

					if (command[0].equals("add")) {
						
						fileList.add(command[1]);
						
						out.println("File added on server 2A: " + command[1]);

					} else if (command[0].equals("remove")) {
						
						for (int i = 0; i < fileList.size(); i++) {
							if (fileList.get(i).equalsIgnoreCase(command[1])) {
								fileList.remove(i);
							}
						}

						
						out.println("File removed on server 2A: " + command[1]);
					} else if (command[0].equalsIgnoreCase("listAll")) {
						
						out.println("Server 2A list all: " + fileList);

					} else {
						System.out.println("Error 2A");
					}

				}

				socket.close();
				System.out.println("The connection 2A was closed: " + socket);

			} catch (Exception ex) {
				
			}

		}

	}

}