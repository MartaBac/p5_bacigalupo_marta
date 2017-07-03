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


import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

/**
 * 
 * The Main Class initializes the graphic interface and creates the 
 * objects to carry out the operations needed for the application to work.
 * 
 */

public class Main {
	private static Database database;
	private static FileManager fileManager;
	private static Window window;
	private static AtomicBoolean isReady = new AtomicBoolean(false);


	public static void main(String[] args) {
		isReady.set(false);
		
		String dbName = "database.sqlite";
		if(args.length > 0 && args[0].equals("debug"))
			dbName = "testDB";

		String path = "jdbc:sqlite:db" + File.separator + dbName ;
		try {
			database = new Database(path);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
			return;
		}

		fileManager = new FileManager(database);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main.window = new Window(fileManager);
					Main.window.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		isReady.set(true);
	}
    
    
    /**
     * This method retrieves the just clicked element and updates
     * the fileManager and database accordingly, it is invoked
     * when is called tableChanged
     *
     * @param fileType
     * @return ArrayList<Model>
     * @throws InterruptedException
     */
	
	public static ArrayList<Model> getClicked(FileType fileType) throws InterruptedException {
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		return fileManager.getClicked(fileType);
	}
    
    /**
     * This method is invoked by the test to obtain the ArrayList <Model> 
     * dimension that contains the models of the specified FileType.
     *
     * @param fileType
     * @return int
     * @throws Exception
     */
	
	public static int getModelQuantity(FileType fileType) throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		return fileManager.getArraySize(fileType);
	}
	
	public static Database getDatabase(){
		return database;
	}
    
    /*
     * This method is invoked by the test to select
     * a train set from the table
     */
    
	public static void testClickedTrainSet() throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		window.testClickedTrainSet();
		Thread.sleep(5000);
	}
    
    /*
     * This method is invoked by the test to add
     * a train set in the table
     */
	
	public static void testAddTrainSet() throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		window.testAddTrainSet();
	}
    
    /*
     * This method is invoked by the test to remove
     * a train set from the table
     */
	
	public static void testRemoveTrainSet(int answer) throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		window.testRemoveTrainSet(answer);
	}
    
    /*
     * This method is invoked by the test to close the database connection.
     * Closes the connection to the database
     */
	
	public static void closeDBConnection() throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		database.closeConnection();
	}
	
	/*
	 * Set to false 'isReady', used by functional test.
	 */
	
	public static void setNotReady() {
		isReady.set(false);
	}
}
