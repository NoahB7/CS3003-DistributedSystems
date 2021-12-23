import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class userEncryption {
	// Contents of user.txt
	// user pass
	// client1 one
	// client2 two
	// client3 three

	public static void main(String[] args) throws IOException {
		System.out.println(checkUser("client2", "two")); // result: true
		System.out.println(checkUser("client1", "two")); // result: false
	}
	//will check if the current user and their password matches any of the pairs in the text file
	//return true: match found -> user can continue 
	//return false: match not found -> user can't continue
	public static boolean checkUser(String user, String pass) throws IOException {
		boolean validUser = false;
		// user name and pass are stored encrypted on text file
		BufferedReader br = new BufferedReader(new FileReader("user.txt"));
		String line = "";
		// gets the encrypted version of the plain text
		String u = encryptString(user);
		String p = encryptString(pass);
		// looks for matches -> if found: true, if not: remains false
		// in text file: each line is 1 user and user/pass separated by tabs
		while ((line = br.readLine()) != null) {
			String[] data = line.split("\t");
			if (data[0].equals(u) && data[1].equals(p)) {
				validUser = true;
			}
		}

		br.close();
		return validUser;
	}

///////////// code below found at: https://mackey.cs.uafs.edu/docs/encrypt.php /////////////
	/**
	 * @author amackey
	 */
	/**
	 * This method will accept a string to be encrypted as a parameter and return
	 * the one-way hashed value using the SHA-256 encryption algorithm.
	 * 
	 * @param valueToEncrypt
	 * @return
	 */

	public static String encryptString(String valueToEncrypt) {
		StringBuffer output = new StringBuffer();

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(valueToEncrypt.getBytes(StandardCharsets.UTF_8));

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);

				if (hex.length() == 1)
					output.append('0');

				output.append(hex);
			}
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}

		return output.toString();
	}
///////////// code above found at: https://mackey.cs.uafs.edu/docs/encrypt.php /////////////
}