import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tester {
	
	public static void main(String[] args) {
		
		
		
	}
	
	
	public static void fileByteSend(Socket socket, String filePath) throws IOException {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		int fourKBytePage = 4096;// sticking to default from lecture
		byte[] b = new byte[fourKBytePage];

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));

		while (bis.read(b) > 0) {
			out.write(b);
			System.out.println(b.toString());
		}
		// System.out.println("Transferred file");
		out.close();
		bis.close();
	}
	
	public static void fileByteReceive(Socket socket, int fileSize, String filePath) throws IOException {
		int fourKBytePage = 4096;// sticking to default from lecture
		byte[] b = new byte[fourKBytePage];

		// here is where the new socket variable would be however we are passing the
		// currently in use socket as a parameter

		DataInputStream in = new DataInputStream(socket.getInputStream());

		int bytesRead = 0;
		int offset = 0;
		offset = fileSize;

		// the fileName can be passed as filePath -> will just save to the area where
		// the server is saved
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		BufferedWriter br = new BufferedWriter(new FileWriter(filePath, true));

		while ((bytesRead = in.read(b, 0, Math.min(fourKBytePage, offset))) > 0) {
			// data read is stored in b
			bos.write(b); // saving file passed by client
			offset = offset - bytesRead;

			// appending the file content to file saved in the server - at start will create
			// new file
			char[] content = new String(b, StandardCharsets.UTF_8).toCharArray();
			for (int i = 0; i < content.length; i++) {
				br.append(content[i]);
			}
		}
		// System.out.println("File Received");
		br.close();
		in.close();
		bos.close();
	}
	
	
	
	public static int fileSize(String file) {
		// how to find the file size in bytes - checked: it works
		return (int) new File(file).length();
	}


}
