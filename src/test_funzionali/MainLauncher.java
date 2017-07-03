package test_funzionali;

import java.util.ArrayList;

import trainSet.Database;
import trainSet.FileType;
import trainSet.Main;
import trainSet.Model;

public class MainLauncher extends Thread {

	public void run(){
		try {
			String[] args = new String[1];
			args[0] = "debug";
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Model> getClicked(FileType fileType) 
								throws InterruptedException{
		return Main.getClicked(fileType);
	}
		
	public void testClickedTrainSet() throws Exception{
		Main.testClickedTrainSet();
	}
	
	public int getModelQuantity(FileType fileType) throws Exception{
		return Main.getModelQuantity(fileType);
	}
		
	public Database getDatabase(){
		return Main.getDatabase();
	}
	
	public void testAddTrainSet() throws Exception{
		Main.testAddTrainSet();
	}
	
	public void testRemoveTrainSet(int answer) throws Exception{
		Main.testRemoveTrainSet(answer);
	}
	
	public void closeDBConnection() throws Exception{
		Main.closeDBConnection();
	}
	
	public void setNotReady() {
		Main.setNotReady();
	}

}
