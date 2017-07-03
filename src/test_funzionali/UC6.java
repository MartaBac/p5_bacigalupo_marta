package test_funzionali;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.FileType;
import test_funzionali.MainLauncher;
import util.DatabaseCreator;

/**
 * Testing UC6: TrainSet remotion
 */

public class UC6 {
	
	private static DatabaseCreator testDB;	
	private static MainLauncher ml; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDB = new DatabaseCreator("testDB");
		testDB.create();
		testDB.fillDatabase();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ml.closeDBConnection();
		testDB.delete();
	}

	@Test
	public void test() throws Exception {
		
		// Starts of the test UC6 - Main scenario
		System.out.println("Avvio test UC6.");
		ml = new MainLauncher();
		ml.setNotReady();
		ml.start();
		
		// I obtain the number of TrainSet in ACO.
		int loadedTrainSet = ml.getModelQuantity(FileType.TRAIN);
		
		// Starting point of the procedure to remove a TrainSet(confirming it) 
		ml.testRemoveTrainSet(JOptionPane.YES_OPTION); // Confirm the remotion
		
		/* 
		 * After the remotion, the number of TrainSet must be minor than
		 * previously.
		 */
		assertTrue(ml.getModelQuantity(FileType.TRAIN) < loadedTrainSet);
		
		// Starts of the test UC6 - alternate scenario 3a
		loadedTrainSet = ml.getModelQuantity(FileType.TRAIN);
		
		// Starts the procedure to remove a TrainSet (not confirming).
		ml.testRemoveTrainSet(JOptionPane.NO_OPTION);
		
		// The number of TrainSet must be the same, no deletion confirmed
		assertTrue(loadedTrainSet == ml.getModelQuantity(FileType.TRAIN));
		System.out.println("Test UC6 completato.");
	}
}
