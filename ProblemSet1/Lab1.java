import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Lab1 {

	public static HashMap<String, ArrayList<String>> map = new HashMap<>();

	public static void main(String[] args) {

		File dir = new File(args[0]);
		File[] list = dir.listFiles();

		traversal(list, 0);

		System.out.println("filename\t#ofpaths\tlongestStringPath");
		System.out.println("--------\t--------\t-----------------");

		for (int i = 0; i < map.size(); i++) {
			if (map.get(list[i].getName()) != null) {
				System.out.println((map.get(list[i].getName())).get(0) + "\t" + (map.get(list[i].getName()).size() - 1)	+ "\t\t" + longest(map.get(list[i].getName())));
			}
		}

		System.out.println("\n");
		for (int i = 0; i < map.size(); i++) {
			if (map.get(list[i].getName()) != null) {
				for (int j = 0; j < map.get(list[i].getName()).size(); j++) {
					if(map.get(list[i].getName()).get(j).contains("\\") || map.get(list[i].getName()).get(j).contains("/"))
						System.out.println(map.get(list[i].getName()).get(j));

				}
			}
		}

	}

	public static void traversal(File[] list, int i) {

		if (i >= list.length) {

			return;
		}

		if (!list[i].isDirectory()) {

			if (map.get(list[i].getName()) != null) {

				map.get(list[i].getName()).add(list[i].getAbsolutePath());

			} else {

				ArrayList<String> a = new ArrayList<>();
				a.add(list[i].getName());
				a.add(list[i].getAbsolutePath());
				map.put(list[i].getName(), a);


			}
			

		} else if (list[i].isDirectory()) {

			traversal(list[i].listFiles(), 0);

		}

		traversal(list, ++i);

	}

	public static String longest(ArrayList<String> x) {

		String s = "";
		for (int i = 0; i < x.size(); i++) {
			if (x.get(i).length() > s.length()) {
				s = x.get(i);
			}
		}

		return s;
	}

	public static String fileName(ArrayList<String> x) {

		String s = "";
		for (int i = x.size() - 1; i > 0; i--) {
			if (x.get(0).charAt(i) == ' ') {
				s = x.get(0).substring(i, x.size());
			}
		}
		System.out.println("FILENAME: " + s);

		return s;
	}

}
