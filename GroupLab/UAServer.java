import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UAServer {
	
	public static StringBuilder concat = new StringBuilder();

	public static void main(String[] args) throws Exception {
//try catch
		if (args.length < 2) {
			System.out.println("Invalid syntax: java UAServer port# #ofthreads");
			System.exit(10);
		}
//port to use for server
		int port = Integer.parseInt(args[0]);
//max num of threads
		int nThreads = Integer.parseInt(args[1]);

		System.out.println("Server is running on " + port);

		ServerSocket server = new ServerSocket(port);

		ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);

		while (true) {
			Socket sock = server.accept();
			threadPool.execute(new UAClientThread(sock));
		}

	}

	public static class UAClientThread implements Runnable {

		private Socket socket;

		public UAClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("New connection established: " + socket);
			try {

				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				out.println("Welcome!");

//reads data from client
				String input;
				while ((input = br.readLine()) != null) {
					
					synchronized(concat) {
						System.out.println(concat.append(input));
					}
					
					out.println("MESSAGE RECEIVED"); // send to client
				}

//send to client

				socket.close();
				System.out.println("The connection was closed: " + socket);

			} catch (Exception ex) {

			}

		}

	}

}