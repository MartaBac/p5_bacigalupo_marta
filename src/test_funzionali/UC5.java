package test_funzionali;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.FileType;
import test_funzionali.MainLauncher;
import util.DatabaseCreator;

/**
 * Testing UC5: add of a train set
 */

public class UC5 {
	
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
		
		// Start of test UC4 - main scenario
		System.out.println("Avvio test UC5.");
		ml = new MainLauncher();
		ml.setNotReady();
		ml.start();
		
		// I obtain the number of TrainSet set in the ACO.
		int loadedTrainSet = UC5.ml.getModelQuantity(FileType.TRAIN);
		
		// Starting point of the procedure to add a new TrainSet.
		ml.testAddTrainSet();
		
		/* 
		 * After adding a new TrainSet, the number of TrainSet in ACO
		 * must be bigger than before the start of the add procedure.
		 */ 
		
		assertTrue(ml.getModelQuantity(FileType.TRAIN) > loadedTrainSet);
		
		// Start of test UC5 - alternative scenario 2a		
		loadedTrainSet = UC5.ml.getModelQuantity(FileType.TRAIN);
		
		// Starts the procedure to add the same TrainSet added previously.
		ml.testAddTrainSet();
		
		/* 
		 * Due to the fact that it should not be possible to add more than 
		 * once the same TrainSet, the previous procedure must not modify the
		 * number of TrainSet present. So, the number should be equals to the
		 * one saved at the beginning of this alternative scenario. 
		 */
		
		assertTrue(ml.getModelQuantity(FileType.TRAIN) == loadedTrainSet);
		System.out.println("Test UC5 completato.");
	}
}
