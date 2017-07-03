package test_strutturali;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.Database;
import trainSet.FileManager;
import trainSet.FileType;
import trainSet.Listener;
import trainSet.Model;
import util.DatabaseCreator;

/**
 * Test of the FileManager class
 */

public class FileManagerTest {
	
	private static String nameDB="db_FM.sqlite";
	private static String dir="jdbc:sqlite:db"+ File.separator + nameDB;
	private static Database db;
	private static FileManager fm;
	private static int id=0;
	private static String name="nameTrain";
	private static String path="pathTrain";
	private static boolean click=true;
	private static Model e;
	private static FileType fileType = FileType.TRAIN;
	private static Listener l;
	private static DatabaseCreator d;
		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		d = new DatabaseCreator(nameDB);
		d.create();
		db = new Database(dir);
		fm = new FileManager(db);
		e = new Model(id, name, path, click, fileType);
		l=new Listener(fm, fileType);	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.closeConnection();
		d.delete();
	}

	/**
	 * Testing a function that changes the clicked state of the model of 
	 * file type "fileType" to the boolean value "val".
	 * 
	 * Invoking setChecked on a record, will increase the number of clicked
	 *  models and getClicked will return a  notNull value. If SetChecked 
	 *  is invoked on an index greater than the number of records present 
	 *  in FileManager nothing will be done.
	 * 
	 */
	
	@Test
	public final void testSetClicked() throws Exception {

		l.unsetLock();

		// And at least a record in it
		fm.insert("nametrain", "path", fileType);		
		assertNull("No " + fileType + " records should be clicked", 
											fm.getClicked(fileType));
		fm.setClicked(fileType, 0, true);
		assertNotNull(fm.getClicked(fileType));
		assertEquals(1,fm.getClicked(fileType).size());
		fm.setClicked(fileType, 0, false);
		assertNull(fm.getClicked(fileType));

		// Try to setClicked a record not in table, nothing will change.
		fm.setClicked(fileType, fm.getArraySize(fileType) + 1, true);
		assertNull(fm.getClicked(fileType));
		fm.remove("nametrain", fileType);
	}

	/**
	 * Will be returned an ArrayList containing all the models of the type 
	 * indicated that have been clicked by the user
	 * 
	 * The function returns null if no Models are checked, 
	 * otherwise an an ArrayList.
	 */
	
	@Test
	public final void testGetClicked() throws Exception {	
		String test ="name";
		fm.updateClicked("*", false, fileType);
		fm.insert(test, "path", fileType);

		// Table not empty but no record clicked
		assertNull("No record should be clicked in table"+ fileType,
				fm.getClicked(fileType));
		fm.remove(test, fileType);
		
		Model e0 = new Model(id, nameDB, path, true, fileType);	
		Model e1 = new Model(id, nameDB+"2", path, false, fileType);
		Model e2 = new Model(id, nameDB+"3", path, true, fileType);
		
		fm.add(e0);
		int t=fm.getClicked(fileType).size();

		// A clicked model has been added in table, getClicked will return it
		assertNotNull(fm.getClicked(fileType));
		assertEquals(fileType+" should have only the clicked record"+e0 ,
				e0,fm.getClicked(fileType).get(0));

		// Adding a not clicked record 
		fm.add(e1);
		assertEquals("Return element size should have not changed",t,
				fm.getClicked(fileType).size());

		// Adding a clicked record getClicked size will increase by 1.
		fm.add(e2);
		assertEquals(t+1, fm.getClicked(fileType).size());
		
		fm.remove(e0);
		fm.remove(e1);
		fm.remove(e2);
	}
	
	/**
	 * Test add a "e" Model object to the FileManager and control if this 
	 * really causes the insertion of "e".
	 */
	
	@Test
	public final void testAdd() {
		int t = fm.getArraySize(fileType);		
		fm.add(e);
		assertEquals(t+1, fm.getArraySize(fileType));
		assertTrue(fm.getClicked(fileType).contains(e));
	}

	/**
	 * The function under test removes ,thanks to the key value (Model),
	 *  a Model object from the ArrayList<Model> containing the models 
	 *  which share the same parameter's fileType.
	 * 
	 */
	
	@Test
	public final void testRemove() {
		fm = new FileManager(db);
		fm.add(e);
		int t= fm.getArraySize(fileType);			
		fm.remove(e);
		assertEquals(t-1,fm.getArraySize(fileType));
		assertNull(fm.getClicked(fileType));		
	}

	/**
	 * The function under test removes a Model object 
	 * from an ArrayList<ArrayList<Model>> giving as keys
	 * "fileType" and "index" .
	 * 
	 * After removing the record, the array will be smaller and
	 * filemanager.getArraySize(fileType) will return a minor integer;
	 * if the operation of removing fails because the value of "index" 
	 * too high, array will be not modified.
	 * 
	 */
	
	@Test
	public final void testRemoveFileTypeInt() {
		fm.add(e);				
		int t = fm.getArraySize(fileType);
		
		// Memorize the last element in fm(fileType),and then remove it
		Model temp = fm.getElement(fileType, t-1);
		fm.remove(fileType, fm.getArraySize(fileType)-1);
		assertEquals("ArraySize should have decreased due to the remotion "
				+ "of a record",t-1,fm.getArraySize(fileType));	
		Model e0 = new Model(id, "e0nameTrain", 
						"e0pathTrain", click, fileType);
		fm.add(e0);
		
		assertNotEquals(temp + "has been removed, it should not be returned "
				+ "by the getElement.",temp, fm.getElement(fileType, t-1));
		
		/* e0 has been added after deleting temp, 
		 * it will be in the position temp was.
		 */
		assertEquals(e0, fm.getElement(fileType, t-1));		
		t = fm.getArraySize(fileType);
		
		/* Try to remove giving an index > array dim; 
		 * the operation of removal will not be performed
		 */
		fm.remove(fileType, t+1);
		assertEquals(t, fm.getArraySize(fileType));	
	}

	/**
	 * Testing a function that returns an iterator to the ArrayList<Model> 
	 * containing the models of fileType "fileType"
	 * 
	 */
	
	@Test
	public final void testGetIterator() {

		// Before this add TRAIN_SET_TABLE is empty
		fm.add(e);

		/* 'iterator' has for sure at least one element (due to the 
		 * previous add) hasNext() should return true
		 */
		Iterator<?> i = fm.getIterator(fileType) ;
		assertTrue(i.hasNext());
		assertEquals("i.next() should be equals to the Model just added.",
																e,i.next());
		assertFalse(fileType + " has only an element, "
				+ "so i should have no next value",i.hasNext());	

		// Removing the only record in table
		fm.remove(e);
		i = fm.getIterator(fileType) ;
		assertFalse(fileType + " table is empty, "
				+ "so 'i' should have no next value",i.hasNext());	
	}

	/**
	 * Test a function that returns the number of models in the fileType table
	 * 
	 */
	
	@Test
	public final void testGetArraySize() {
		int i = fm.getArraySize(fileType);
		fm.add(e);
		assertEquals(i + 1, fm.getArraySize(fileType));
		fm = new FileManager(db);
		assertEquals(0, fm.getArraySize(fileType));
	}

	/**
	 * Test a method that returns the model of file type 
	 * "fileType" at index "index".
	 *  
	 * Will be returned the selected Model, if the index is > to the size of the
	 * array containing the models, no Model will be returned, but a null value. 
	 */
	
	@Test
	public final void testGetElement() {
		
		// Add an element, it will be in last position
		fm.add(e);
		assertEquals(e,fm.getElement(fileType, fm.getArraySize(fileType) - 1));
		
		// Index = 10 corresponds to no record
		assertNull(fm.getElement(fileType, 10));
	}
	
	/**
	 * Test a remove function with parameters String "name" and 
	 * FileType "fileType"
	 * 
	 * A record will be removed; filemanager.getArraySize(fileType) returns a
	 * minor integer
	 */
	
	@Test
	public final void testRemoveStringFileType() throws Exception {
		Model e0 = new Model(2,"removeTrain","removePath",false,fileType);
		fm.add(e);
		fm.add(e0);
		fm.insert("nameTrainRemove", path, fileType);
		fm.insert("nameTrainRemove2", path+"2", fileType);
		fm.updateModelData(fileType);
		
		int i = fm.getArraySize(fileType);
		
		int test = fm.getArraySize(fileType) - 2; 
		fm.remove("nameTrainRemove", fileType);
		fm.updateModelData(fileType);

		assertEquals(i-1, fm.getArraySize(fileType));
		
		assertThat(fm.getElement(fileType, test).getName(), 
				not(equalTo("nameTrainRemove")));
	}

	/**
	 * The function updates the "clicked" state of a record in the FileManager.
	 * 
	 * Updating the clicked state will modify getClickedModels(fileType).size()
	 * and checked record will be present in return 
	 * value of getClickedModels(fileType).
	 */
	
	@Test
	public final void testUpdateClicked() throws Exception {
		fm.insert("ClickTrain1", "pathTrain1", fileType);
		fm.insert("ClickTrain2", "pathTrain2", fileType);		
		fm.updateClicked("ClickTrain1", true, fileType);
		fm.updateModelData(fileType);
		
		ArrayList<Model> AL = fm.getClicked(fileType);
		fm.updateClicked("ClickTrain2", true, fileType);
		fm.updateModelData(fileType);
		
		assertEquals(AL.size() + 1, fm.getClicked(fileType).size());
		AL=fm.getClicked(fileType);
		
		/* Check if the inserted and clicked record has been returned by 
		 * getClickedModels		
		 */
		boolean check0=false;
		for(Model s : AL)
		{
			String g = s.getName();
			 if("ClickTrain2".equals(g)){ 
				 check0=true;
				 break;
			 }
		}
		
		assertTrue("ClickTrain2" + " should be returned by getClicked(" 
													+ fileType + ")",check0);			
		fm.updateClicked("ClickTrain2", false, fileType);
		fm.updateModelData(fileType);
		
		assertEquals("Due to the update of 'ClickTrain2' check status,"
				+ " getClicked should return one less member",
							AL.size() - 1, fm.getClicked(fileType).size());
		
		fm.remove("ClickTrain1", fileType);
		fm.remove("ClickTrain2", fileType);	
	}

	/**
	 * Test a function that extract the ID of a model named "name" of 
	 * fileType "fileType"
	 * 
	 * The function returns the (integer) 'ID' of the record 'name'; 
	 * if 'name' is not find it returns -1.
	 * 
	 */
	
	@Test
	public final void testGetIdByName() throws Exception {
		
		// Searching for a record not in table, it will return -1		
		assertEquals(-1,fm.getIdByName("testToFail",fileType));
		fm.insert("nameTrain", "trainPath", fileType);
		fm.insert("nameTrain2","trainPath2", fileType);
		assertTrue(fm.getIdByName("nameTrain2", fileType)!=(-1));
		
		/* updateModelData returns an ArrayList containing the record 
		 * just inserted		
		 */
		assertEquals(fm.updateModelData(fileType).get(
			fm.getArraySize(fileType)-1).getId(),fm.getIdByName(
											"nameTrain2", fileType));	
		fm.remove("nameTrain2", fileType);
	}
}
