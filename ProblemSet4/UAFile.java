import java.io.File;
import java.util.HashMap;
import java.util.concurrent.locks.StampedLock;



public class UAFile {
	
	private String fileName;
	private String pathToFile;
	public StampedLock lock = new StampedLock();
	
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
