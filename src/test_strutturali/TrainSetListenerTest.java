package test_strutturali;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.Database;
import trainSet.FileManager;
import trainSet.FileType;
import trainSet.Listener;
import trainSet.Model;
import trainSet.TrainSetListener;
import util.DatabaseCreator;

/**
 * Test of the trainSetListener class
 */

public class TrainSetListenerTest {
	private static String name0="db_TestList.sqlite";
	private static String dir="jdbc:sqlite:db"+ File.separator + name0;
	private static Database db;
	private static FileManager fm;
	private static FileType fileType = FileType.TRAIN;
	private static Listener listener;
	private static DatabaseCreator d;
	private String[] columnNames = {"Name", "Path", "Check"};
	private static TrainSetListener trainSetListener;
    private Object[][] data =
     {
     	{"Name0","path0",false},
     	{"Name1","path1",true},
    	{"Name2","path2",false}
     };
    private DefaultTableModel tableModel=
    		new DefaultTableModel(data,columnNames);
	private TableModelEvent e1 = new TableModelEvent(tableModel,1,2,2);
   
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		d = new DatabaseCreator(name0);
		d.create();
		db = new Database(dir);
		fm = new FileManager(db);
		trainSetListener = new TrainSetListener(fm);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.closeConnection();
		d.delete();
	}
	
	@Before
	public void setUp() throws Exception {
		listener=new Listener(fm, fileType);
	}
	
	/**
	 *	Test getSelectedElement().
	 *	The function will return a null value because the initialization of
	 * 'listener' initialize to null the selected element.
	 */
	
	@Test
	public final void testGetSelectedElement() {
		assertNull(listener.getSelectedElement());
	}
	
	/**
	 * Test the function setLock()
	 */
	
	@Test
	public final void testSetLock() {
		listener.setLock();
		assertFalse("Listener should be locked",listener.isLocked());
	}
	
	/**
	 * Test the function unsetLock()
	 */

	@Test
	public final void testUnsetLock() {
		listener.unsetLock();
		assertTrue("Listener should not be locked",listener.isLocked());
	}
		
	/**
	 * 	Testing tableChanged(tableModelEvent), a function that calls 
	 * tableChangedHandler if tableModelEvent is valid. Otherwise, catches
	 * an exception and print its trace.
	 * 
	 * This test checks if , given a valid tableModelEvent, no error 
	 * is printed in console, and if given an invalid one (null value) 
	 * the error is print.
	 */
	
	@Test(expected = Test.None.class)
	public void testTableChanged() throws Exception{
		TableModelEvent e = new TableModelEvent(tableModel,2,2,2);
		Model m = new Model(0, "trainName", "pathUnique", false, fileType);
		Model m1 = new Model(0, "trainName1", "pathUnique1", true, fileType);
		Model m2 = new Model(0, "trainName2", "pathUnique2", false, fileType);

		fm.add(m);
		fm.add(m1);
		fm.add(m2);
		
		trainSetListener.setLock();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setErr(new PrintStream(outContent));
		trainSetListener.tableChanged(e);
	    assertEquals(0, outContent.toString().length());
		
		
		trainSetListener.unsetLock();
		outContent = new ByteArrayOutputStream();
	    System.setErr(new PrintStream(outContent));
		trainSetListener.tableChanged(e);
	    assertEquals(0, outContent.toString().length());
		
		
		outContent = new ByteArrayOutputStream();
	    System.setErr(new PrintStream(outContent));
	    trainSetListener.tableChanged(null);
	    assertTrue(outContent.toString().contains(""
	    		+ "java.lang.NullPointerException"));
	}
	
	
	/**
	 * Testing a Listener function that retrieves the element just checked 
	 * and updates the fileManager and the database accordingly.
	 * 
	 * TableChangedHandler() will return false if Listener is locked,
	 * 			true if it is not
	 */			

	@Test
	public final void testTableChangedHandler() throws Exception {		
		listener.setLock();
		assertFalse(listener.tableChangedHandler(e1));
		listener.unsetLock();
		assertTrue(listener.tableChangedHandler(e1));
	}
	
}
