import java.io.File;
import java.util.HashMap;

/**
 * Each type of UAFile maintains fileName, the name of the file, pathToFile, the path of the file, and 
 * associatedCategories, which is HashMap of type <String, UACategory> of associated categories with
 * this file, essentially which categories is this file a part of
 * @author noah_
 *
 */

/********************************
Name: Noah Buchanan
Username: dist103
Problem Set: PS1
Due Date: September 8, 2020
********************************/

public class UAFile {
	
	private String fileName;
	private String pathToFile;
	
	private HashMap<String,UACategory> associatedCategories = new HashMap<>();
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPathToFile() {
		return pathToFile;
	}
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}
	
	public UAFile() {
		
	}
	
	public UAFile(File file) {
		this.fileName = file.getName();
		this.pathToFile = file.getAbsolutePath();
	}
	
	public HashMap<String,UACategory> getAssociatedCategories(){
		return associatedCategories;
	}

}
