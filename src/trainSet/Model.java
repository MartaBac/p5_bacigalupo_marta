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


import java.io.File;

/**
 * 
 * The Model class provides a representation for each file type
 * Used in the software (those specified in the source file "FileType.java")
 * 
 */


public class Model {
	
	private int id;
	private String name, path;
	private boolean clicked;
	private FileType fileType;

	public Model(int id, String name, String path, boolean clicked, FileType fileType) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.clicked = clicked;
		this.fileType = fileType;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public boolean getClicked() {
		return clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public FileType getFileType() {
		return fileType;
	}
	
	public boolean exist(){
		return new File(path).isFile();
	}

	@Override
	public String toString() {
		return fileType.name() + " [id = " + id + ", name =" + name + ", path = "
							   + path + ", clicked = " + clicked + "]";
	}
}
