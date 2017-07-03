package test_funzionali;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.FileType;
import trainSet.Model;
import test_funzionali.MainLauncher;
import util.DatabaseCreator;

/**
 * Test UC4: set of a TrainSet
 *
 */

public class UC4 {

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
		System.out.println("Avvio test UC4.");
		ml = new MainLauncher();
		ml.setNotReady();
		ml.start();
		
		// I obtain the number of TrainSet set in the ACO.
		ArrayList<Model> currentTrainSet = ml.getClicked(FileType.TRAIN);

		/* 
		 * I start the setting up procedure of a TrainSet.Because it is 
		 * possible to have more than a TrainSet set, after completing 
		 * this procedure there must be more TrainSet than there were before.	
		 */
		
		ml.testClickedTrainSet();
		
		// I obtain the number of TrainSet set in the ACO.
		ArrayList<Model> clickedTrainSet = ml.getClicked(FileType.TRAIN);
		
		// I verify that there are more TrainSet than before.
		assertTrue(clickedTrainSet.size() > currentTrainSet.size());
		System.out.println("Test UC4 completato.");
	}
}
