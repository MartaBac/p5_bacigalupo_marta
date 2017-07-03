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
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

/**
 * The class OpenFile stores the needed information  about the user 
 * selection and   manages the graphic element JFileChooser instantiating 
 * and displaying it to the user. .
 */

public class OpenFile {
	private JFileChooser fileChooser;
	private StringBuilder path;
	private StringBuilder name;

	public OpenFile(FileType fileType) {
		fileChooser = new JFileChooser();
		path = new StringBuilder();
		name = new StringBuilder();
	}
	
    /**
     * Questo metodo permette di aprire un file dialog in cui l'utente puo
     * selezionare un file localmente
     *
     * @throws FileNotFoundException
     */
	
	public void pickMe() throws FileNotFoundException {
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			path.append(file.getAbsolutePath());
			name.append(file.getName());
			if (name.indexOf(".") > 0)
				name = new StringBuilder(name.substring(0, name.lastIndexOf(".")));
		}
	}
			
	public String getPath() {
		return path.toString();
	}
	
	public String getName() {
		return name.toString();
	}

	@Override
	public String toString() {
		return "OpenFile [fileChooser=" + fileChooser + ", path=" + path + ", name=" + name + "]";
	}
}
