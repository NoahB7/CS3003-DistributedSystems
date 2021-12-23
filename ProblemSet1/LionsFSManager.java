import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * File management system utilizing all the appropriate methods needed for such activities
 * 
 * @author noah_
 *
 */

/********************************
Name: Noah Buchanan
Username: dist103
Problem Set: PS1
Due Date: September 8, 2020
********************************/

public class LionsFSManager {


	public HashMap<String,UAFile> Files = new HashMap<>();
	public HashMap<String,UACategory> Categories = new HashMap<>();
	int task = 1;
	
	/**
	 * 
	 * @param args reads in one String from the command line, the path of a directory we will index
	 * @throws IOException
	 * @throws UAInvalidFileException
	 * @throws UAInvalidCategoryException
	 * @throws UACategoryExistsException
	 * @throws UANotEmptyException
	 */

	public static void main(String[] args)throws IOException, UAInvalidFileException, UAInvalidCategoryException, UACategoryExistsException, UANotEmptyException {
		
		LionsFSManager fileSystem = new LionsFSManager();
		fileSystem.createCategory("nocategory");
		for(UAFile i : fileSystem.Files.values()) {
			fileSystem.addFileToCategory(fileSystem.Files.get(i.getFileName()).getFileName(),"nocategory");
		}
		try {
			//1
			System.out.println("\nTask 1");
			System.out.println("-----------------------");
			System.out.println("Indexing given path");
			
			fileSystem.index(new File(args[0]).listFiles(), 0);
			//2
			fileSystem.task++;
			System.out.println("\nTask 2");
			System.out.println("-----------------------");
			
			fileSystem.listAll();
			//3
			fileSystem.task++;
			System.out.println("\nTask 3");
			System.out.println("-----------------------");
			
			fileSystem.createCategory("gradefiles");
			fileSystem.createCategory("datasets");
			
			fileSystem.listCategories();
			//4
			fileSystem.task++;
			System.out.println("\nTask 4");
			System.out.println("-----------------------");
			
			fileSystem.addFileToCategory("datagrades.txt", "gradefiles");
			fileSystem.addFileToCategory("grades.txt", "gradefiles");
			fileSystem.addFileToCategory("distgrades.txt", "gradefiles");
			fileSystem.addFileToCategory("prog2grades.txt", "gradefiles");
			
			fileSystem.listFilesByCategory("gradefiles");
			//5
			fileSystem.task++;
			System.out.println("\nTask 5");
			System.out.println("-----------------------");
			
			fileSystem.addFileToCategory("dataset1.txt", "datasets");
			fileSystem.addFileToCategory("dataset2.txt", "datasets");
			fileSystem.addFileToCategory("cars.txt", "datasets");
			fileSystem.addFileToCategory("courses.txt", "datasets");
			
			fileSystem.listFilesByCategory("datasets");
			//6
			fileSystem.task++;
			System.out.println("\nTask 6");
			System.out.println("-----------------------");
			
			fileSystem.editCategory("gradefiles", "grades");
			
			fileSystem.listFilesByCategory("grades");
			//7
			fileSystem.task++;
			System.out.println("\nTask 7");
			System.out.println("-----------------------");
			
			fileSystem.deleteCategory("datasets");
			
			fileSystem.listCategories();
			//8
			fileSystem.task++;
			System.out.println("\nTask 8");
			System.out.println("-----------------------");
			
			fileSystem.printAllFiles("grades");
			
			//9
			fileSystem.task++;
			System.out.println("\nTask 9");
			System.out.println("-----------------------");
			System.out.println("Appending to each file");
			
			fileSystem.appendToEachFile("grades", "amibcupdatestring");
			
			//10
			fileSystem.task++;
			System.out.println("\nTask 10");
			System.out.println("-----------------------");
			
			fileSystem.printAllFiles("grades");
			
		} catch(Exception e) {
			System.out.println("Error processing task " + fileSystem.task);
		}

		
		
	}
	
	/**
	 * 
	 * @param category Category of files, whom contents we will be listing
	 * @return simply returns a concatenated string of the contents of all files listed
	 * @throws IOException
	 * @throws UAInvalidCategoryException
	 */
	
	public String printAllFiles(String category)throws IOException,UAInvalidCategoryException {
		

		String concat = "";
		System.out.println("ffdsa");

		for(UAFile j : Categories.get(category).getAssociatedFiles().values()) {
			BufferedReader br = new BufferedReader(new FileReader(j.getPathToFile()));
			String line = "";
			
			while((line = br.readLine()) != null) {
				System.out.println(j.getFileName() + "'s contents---->" + line);
				concat += j.getFileName() + "'s contents---->" + line + "\n";

			}

			br.close();
		}
		
		
		return concat;
	}
	
	/**
	 * 
	 * @param category Category of files, whom we will be appending a given String to
	 * @param contentToAppend the content of which we will be appending to each file in the given category
	 * @return returns previous content of files with the new content appended
	 * @throws IOException
	 * @throws UAInvalidCategoryException
	 */

	public String appendToEachFile(String category, String contentToAppend) throws IOException, UAInvalidCategoryException {
		
		for(UAFile j : Categories.get(category).getAssociatedFiles().values()) {
			FileWriter writer = new FileWriter(j.getPathToFile());
			writer.append(contentToAppend);
			writer.close();
		}
		
		return contentToAppend; 
	}
	
	/**
	 * removes a given file from a given category
	 * @param file file to be removed
	 * @param category category the file will be removed from
	 * @return returns true/false based on whether the removal was successful
	 * @throws UAInvalidCategoryException
	 */
	
	public boolean removeFileFromCategory(String file, String category) throws UAInvalidCategoryException{

		try {
			int listSize = Files.get(file).getAssociatedCategories().size();
			if(listSize == 1 && Categories.get(category).getCategoryName().equals("nocategory")) {
				Categories.get("nocategory").getAssociatedFiles().put(file, new UAFile(new File(file)));
			}
			if(Categories.get(category).getCategoryName().equals("nocategory")) {
				System.out.println("cannot remove category association to 'nocategory'");
				return false;
			} else {
				Categories.get(category).getAssociatedFiles().remove(file);
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @param file given file to find the categories for
	 * @return returns the categories a specified file is associated with in an HashMap
	 * @throws UAInvalidFileException
	 */
	
	public HashMap<String,UACategory> getCategories(String file) throws UAInvalidFileException{

		HashMap<String,UACategory> list = new HashMap<>();
		for(UACategory j : Files.get(file).getAssociatedCategories().values()) {
			list.put(j.getCategoryName(),j);
			System.out.println(j.getCategoryName());
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param category lists the associated files contained in a category
	 * @return returns the associated files contained in the given category in a HashMap
	 * @throws UAInvalidCategoryException
	 */
	
	public HashMap<String,UAFile> listFilesByCategory(String category) throws UAInvalidCategoryException{

		HashMap<String,UAFile> list = new HashMap<>();
		for(UAFile j : Categories.get(category).getAssociatedFiles().values()) {
			list.put(j.getFileName(), j);
			System.out.println(j.getFileName());

		}
		return list;
	}
	
	/**
	 * 
	 * @param file file to be added to specified category
	 * @param category category specified file will be added to
	 * @return returns true/false based on if the file insertion was successful
	 * @throws UAInvalidCategoryException
	 * @throws UAInvalidFileException
	 */
	
	public boolean addFileToCategory(String file, String category)throws UAInvalidCategoryException,UAInvalidFileException {
		
		try {
			
			File path = new File(file);
			UAFile fileToAdd = new UAFile(path);
			
			if(Categories.get("nocategory").getAssociatedFiles().get(file) == null) {
			
			Files.put(fileToAdd.getFileName(), fileToAdd);

			Categories.get(category).getAssociatedFiles().remove(fileToAdd.getFileName());
			
			path.createNewFile();
			fileToAdd.getAssociatedCategories().put(fileToAdd.getFileName(), new UACategory(category));
			Categories.get(category).getAssociatedFiles().put(fileToAdd.getFileName(), fileToAdd);
			} else {
				
				Categories.get(category).getAssociatedFiles().remove(fileToAdd.getFileName());
				fileToAdd.getAssociatedCategories().put(fileToAdd.getFileName(), new UACategory(category));
				Categories.get(category).getAssociatedFiles().put(fileToAdd.getFileName(), fileToAdd);
			}
			
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * @param category given name of category to be created
	 * @return returns the Object of the new category created
	 */
	
	public UACategory createCategory(String category) {
		
		UACategory c = new UACategory(category);
		Categories.put(category, c);
		return c;
		
	}
	
	/**
	 * 
	 * @param category given category to be deleted
	 * @return returns the Object of the category deleted
	 * @throws UACategoryExistsException
	 * @throws UANotEmptyException
	 */
	
	public UACategory deleteCategory(String category)throws UACategoryExistsException, UANotEmptyException {

		return Categories.remove(category);
		
	}
	
	/**
	 * 
	 * @param oldName previous name of category we want to change
	 * @param newName name to replace the old category name
	 * @return returns the new renamed category
	 * @throws UACategoryExistsException
	 * @throws UAInvalidCategoryException
	 */
	
	public UACategory editCategory(String oldName, String newName)throws UACategoryExistsException, UAInvalidCategoryException {

		
		UACategory temp = Categories.get(oldName);
		Categories.remove(oldName);
		Categories.put(newName, temp);
		return Categories.get(newName);
	}
	
	/**
	 * 
	 * @param list a given list of files to be index
	 * @param i i for recursion purposes
	 */

	public void index(File[] list, int i) {

		if (i >= list.length) {
			return;
		} else {
			if (list[i].isDirectory()) {
				index(list[i].listFiles(), 0);
			} else {
				UAFile file = new UAFile();
				file.setFileName(list[i].getName());
				file.setPathToFile(list[i].getAbsolutePath());
				Files.put(file.getFileName(), file);
			}
			index(list, ++i);
		}

	}
	
	/**
	 * 
	 */
	
	public void listCategories() {

		for(UACategory i : Categories.values()) {
			System.out.println(i.getCategoryName());
		}
	}
	
	/**
	 * 
	 * @return returns the list that it just printed in ascending order, unaltered, as I was
	 * not sure if you wanted us to alter the list or just print it
	 */
	
	public HashMap<String,UAFile> listAll() {
		
		String[] x = new String[Files.size()];
		int num = 0;
		for(UAFile i : Files.values()) {
			x[num] = Files.get(i.getFileName()).getFileName();
			num++;
		}
		mergeSort(x,0,x.length-1);
		for(int i = 0; i < x.length; i++) {
			System.out.println(x[i]);
		}
		
		return Files;
	}
	
	/**
	 * 
	 * @param A Array to be sorted
	 * @param p beginning index of array
	 * @param r ending index of array
	 */
	
	public static void mergeSort(String[] A, int p, int r) {

		if (p < r) {
			int q = (p + r) / 2;
			mergeSort(A, p, q); 
			mergeSort(A, q + 1, r); 
			merge(A, p, q, r); 
		}
	}
	
	/**
	 * 
	 * @param A Array to merge
	 * @param p beginning index of array
	 * @param q middle index of array
	 * @param r ending index of array
	 */

	public static void merge(String[] A, int p, int q, int r) {

		int n1 = q - p + 1;
		int n2 = r - q;

		String[] L = new String[n1 + 1];
		String[] R = new String[n2 + 1];

		for (int i = 0; i < n1; i++) {
			L[i] = A[p + i];
		}
		for (int j = 0; j < n2; j++) {
			R[j] = A[q + j + 1];
		}

		L[n1] = "zzz";
		R[n2] = "zzz";

		int i = 0, j = 0;

		for (int k = p; k <= r; k++) {

			if (L[i].compareTo(R[j]) < 0) {
				A[k] = L[i];
				i++;
			} else {
				A[k] = R[j];
				j++;
			}
		}
	}

}
