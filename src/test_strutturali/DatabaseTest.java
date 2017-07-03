package test_strutturali;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import trainSet.Model;
import trainSet.Database;
import trainSet.FileType;
import util.DatabaseCreator;

/**
 * Test of Database class
 */

public class DatabaseTest {	
	private static String name="DB_test.sqlite";
	private static String dir="jdbc:sqlite:db"+ File.separator + name;
	private static Database database;
	private static DatabaseCreator databaseCreator;
    private String table = "TRAIN_SET_TABLE" ;
    private FileType fileType = FileType.TRAIN ;
    private Class<? extends Exception> expectedException = Exception.class;
    private String expectedExceptionMsg = "Unrecognized file type";

    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		databaseCreator = new DatabaseCreator(name);
		databaseCreator.create();
		database = new Database(dir);	
	}
	
	@AfterClass
	public static void tearDownAfileTypeerClass() throws Exception {
		database.closeConnection();
		databaseCreator.delete();
	}
	 
    @Rule
    public ExpectedException thrown = ExpectedException.none();
		    
	/**
	* Testing RetrieveFromTable(fileType), that returns an ArrayList<Model>
	*  containing all the elements from the appropriate fileType table
	* 
	* RetrieveFromTable(fileType) will always return a NotNull value; adding 
	* new elements to the table will increase the returned value of
	*  RetrieveFromTable(fileType).size()
	* 
	*/
   
	@Test
	public final void testRetrieveFromTable() throws Exception {
		ArrayList<Model> al = database.retrieveFromTable(fileType);
		assertNotNull(al);	
		database.insertIntoTable("RetrieveTrain", "pathTest", fileType);	
		assertEquals(al.size()+1,
				database.retrieveFromTable(fileType).size(),0);
		assertEquals("RetrieveTrain",database.retrieveFromTable(
				fileType).get(al.size()).getName());
		database.removeFromTable("RetrieveTrain", fileType);
	}

	/**
	 * Testing a function that inserts the element named "name", 
	 * located at path "path", into the appropriate FileType table.
	 * 
	 * 'database' will be updated with the new value; the Table increase 
	 * in dimension and contains the inserted record
	 */
	
	@Test
	public final void testInsertIntoTable() throws Exception {
		String name ="name";
		String path = "//path";
		int i= database.retrieveFromTable(fileType).size();
		database.insertIntoTable(name, path, fileType);
		assertNotNull(database.retrieveFromTable(fileType));

		assertEquals("Dimension should have increased",i+1,
				database.retrieveFromTable(fileType).size());
		assertEquals("Table must contain inserted record",name,
				database.retrieveFromTable(fileType).get(i).getName());
		database.removeFromTable(name, fileType);
	}
	
    /**
    * Testing remotion of an entry from a table.
    * 
    * The table will no more contain the removed record.
    * Exception will be thrown when set an invalid FileType
    */
	
	@Test
	public final void testRemoveFromTable() throws Exception {	
		database.insertIntoTable(name, "pathToTestTrain", fileType);	
		assertTrue(database.removeFromTable(name, fileType));	
	}
	
	/**
	 * GetTableName(fileType) will return the appropriate name of the table for
	 * the type indicated by "fileType".
	 * 
	 * The return value will correspond to the expected table name
	 * The functions throw an Exception if FileType is not recognized.
	 * 
	 */

	@Test
	public final void testGetTableName() throws Exception {
		assertEquals("Equals failure",table,Database.getTableName(fileType));	
		
		thrown.expect(expectedException);
		thrown.expectMessage(expectedExceptionMsg);
		Database.getTableName(FileType.EC);
	}
	
	/**
	 * Allow to check or uncheck a record specified by the key value 'name' 
	 * and 'fileType'
	 * 
	 * Column 'CHECKED' of record 'name' in table 'getTableName(fileType) 
	 * will be modified. if 'name' equals to '*' all the records of the 
	 * fileType indicated will be updated
	 */

	@Test
	public final void testUpdateClicked() throws Exception {
		String name="TestTrain";

		database.insertIntoTable(name,"pathWB",fileType);
		database.updateClicked(name, 1, fileType);		
		ArrayList<Model> al = database.getClickedModels(fileType);
		int t = al.size();
		database.updateClicked(name, 0, fileType);
		assertEquals(t-1,database.getClickedModels(fileType).size(),0);
		
		// No 'fileType' records set to clicked
		database.updateClicked("*", 0, fileType);
		assertEquals(0,database.getClickedModels(fileType).size());
		
		// All 'fileType' records set to clicked
		database.updateClicked("*", 1, fileType);
		assertEquals(database.retrieveFromTable(fileType).size(),
				database.getClickedModels(fileType).size(),0);
		database.removeFromTable(name, fileType);
	}
	
	/**
	 *  Testing a function that checks if a connection is valid.
	 */
	
	@Test
	public final void testConnectionValidator() throws SQLException{
		Connection connection = database.getConnection();
		connection.close();
		database.connectionValidator();
		Connection establishedConnection = database.getConnection();
		
		// Test the validity of the connection
		assertTrue(connection != establishedConnection);
		connection.close();		
		database.setConnection(DriverManager.getConnection("jdbc:sqlite:db" 
										+ File.separator + "database.sqlite"));
		Connection conn = database.getConnection();
		database.connectionValidator();
		assertEquals(conn, database.getConnection());
		conn.close();
	}
	
	/**
	 * Test of the function setConnection()
	 */
	
	@Test
	public void testSetConnection() throws SQLException {
		
		// Test the setter of the class database.
		Connection connection = DriverManager.getConnection("jdbc:sqlite:db"
										+ File.separator + "database.sqlite");		
		database.setConnection(connection);
		assertEquals(connection, database.getConnection());
	}	
}

