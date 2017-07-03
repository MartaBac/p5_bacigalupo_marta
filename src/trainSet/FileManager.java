package trainSet;
/*
 * Autore: Bacigalupo Marta
 *
 * Data: 02/07/2017
 *
 * The purpose of the project is to provide a tool for speeding up and simplifying
 * the analysis of the performance of automatic classifiers
 * serializing them with different input configurations, train
 * Sets and test sets that will be specified by the user.
 * In particular this part of implementation deals with the the management of 
 * the train set that the user wishes to use and manages adding, setting, and 
 * removing them.
 * 
 */


import java.util.ArrayList;
import java.util.Iterator;

/**
 * The class 'FileManager' offers an interface for communication
 * between the implementation logic with the database and the graphic part.
 * 
 */

public class FileManager {
	private Database database;
	private ArrayList<ArrayList<Model>> array;

	public FileManager(Database database) {
		this.database = database;
		array = new ArrayList<ArrayList<Model>>();
		for (FileType fileType : FileType.values()) {
			array.add(fileType.ordinal(), new ArrayList<Model>());
		}
	}
	
	/**
	 * This method allows you to add the Model "model"
     * To the array containing all the templates stored in the FileManager.
	 * 
	 * @param model
	 */
	
	public void add(Model model) {
		array.get(model.getFileType().ordinal()).add(model);
	}
	
	/**
	 * This method eliminates the Model "model" from the array
     * Containing all the templates stored in the FileManager
	 * 
	 * @param model
	 */

	public void remove(Model model) {
		array.get(model.getFileType().ordinal()).remove(model);
	}
	
    /**
     * This method allows you to delete the model you have
     * FileType "fileType" and characterized by an index of "index".
     *
     * @param fileType
     * @param index
     */

	public void remove(FileType fileType, int index) {
		if (index > array.get(fileType.ordinal()).size())
			return;
		array.get(fileType.ordinal()).remove(index);
	}
	
    /**
     * This method allows to get an iterator to scroll sequentially
     * the ArrayList <Model> containing the FileType Model "fileType".
     *
     * @param fileType
     * @return Iterator<Model>
     */

	public Iterator<Model> getIterator(FileType fileType) {
		return array.get(fileType.ordinal()).iterator();
	}
	
	/**
	 * This method allows to get the size 
	 * of ArrayList <Model> containing the fileType "fileType"
     *
	 * @param fileType
	 * @return Iterator<Model>
	 */

    public int getArraySize(FileType fileType) {
        return array.get(fileType.ordinal()).size();
    }

	/**
	 * This method allows you to get the template
	 * Of the file type "fileType" to the index "index"
     *
	 * @param fileType
	 * @param index
	 * @return null if no model at index "index" is available,
	 * the found model otherwise
	 */

	public Model getElement(FileType fileType, int index) {
		if (index > array.get(fileType.ordinal()).size())
			return null;
		return array.get(fileType.ordinal()).get(index);
	}
	
	/**
	 * This method allows you to change the status of
     * 'click' of the fileType model "fileType" with the respective "val" boolean
	 *
	 * @param fileType
	 * @param index
	 * @param val
	 */
	
	public void setClicked(FileType fileType, int index, boolean val) {
		if (index > array.get(fileType.ordinal()).size())
			return;

		array.get(fileType.ordinal()).get(index).setClicked(val);
	}
	
	
	/** 
	 * This method allows to get an ArrayList containing all FileType models
     * "fileType" that are "clicked" by the user
	 * 
	 * @param fileType
	 * @return ArrayList<Model>
	 */
	
	public ArrayList<Model> getClicked(FileType fileType) {
		ArrayList<Model> clickedModel = new ArrayList<Model>();
		ArrayList<Model> allModels = array.get(fileType.ordinal());
		
		for (Model m : allModels) {
			if (m.getClicked()) {
				clickedModel.add(m);
			}
		}
		
		if (clickedModel.size() == 0)
			return null;
		
		return clickedModel;
	}
	

	/**
	 * This method allows to extract values ​​from the table indicated by "fileType"
     * and insert them into an ArrayList of the same type of "fileType".
	 * 
	 * @param fileType
	 * @return ArrayList<Model>
	 * @throws Exception
	 */
	
	public ArrayList<Model> updateModelData(FileType fileType) throws Exception {
		ArrayList<Model> result = database.retrieveFromTable(fileType);
			array.set(fileType.ordinal(), result);		
		return result;
	}
	
	/**
     * Wrapper of the class Database that allows the insertion of a
     * model named "name", with path "path" in the table fileType "fileType"
	 * 
	 * @param name
	 * @param path
	 * @param fileType
	 * @throws Exception
	 */
	
	public void insert(String name, String path, FileType fileType) throws Exception {
		database.insertIntoTable(name, path, fileType);
		this.updateModelData(fileType);
	}
	
	/**
	 * Wrapper of the class Database that allows the deletion of a
     * model named "name" from the table fileType "fileType".
	 * 
	 * @param name
	 * @param path
	 * @param fileType
	 * @throws Exception
	 */
	
	public void remove(String name, FileType fileType) throws Exception {
		database.removeFromTable(name, fileType);
		this.updateModelData(fileType);
	}
	
	/**
	 * Wrapper of the class Database that allows to update a the status
	 * of "click" of a model named "name" from the table fileType "fileType".
	 * 
	 * @param selectedName
	 * @param checked
	 * @param fileType
	 * @throws Exception
	 */
	
	public void updateClicked(String selectedName, boolean checked, FileType fileType) throws Exception {
		database.updateClicked(selectedName, (checked) ? 1 : 0, fileType);
	}
	
	/**
	 * This method allows to get the ID of the "name" file from 
	 * the fileType "fileType"
	 * 
	 * @param name, fileType
	 * @return int
	 * @throws Exception
	 */
	
	public int getIdByName(String name, FileType fileType) throws Exception {
		for (Model m : array.get(fileType.ordinal()))
			if (m.getName().equals(name))
				return m.getId();
		
		return -1;
	}
}
