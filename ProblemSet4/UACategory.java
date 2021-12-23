import java.util.HashMap;

/**
 * Contains the fields category name to maintain order of categories and a HashMap of type <String, UAFile>
 * of all associated files with this category, or better worded, files CONTAINED in this category
 * @author noah_
 *
 */


public class UACategory {
	
	private String categoryName;
	
	private HashMap<String,UAFile> associatedFiles = new HashMap<>();

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public UACategory() {
		
	}
	
	public UACategory(String name) {
		this.categoryName = name;
	}
	
	public HashMap<String,UAFile> getAssociatedFiles(){
		return associatedFiles;
	}

}
