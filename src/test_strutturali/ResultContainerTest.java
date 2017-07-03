package test_strutturali;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.Database;
import trainSet.ResultContainer;
import util.DatabaseCreator;

/**
 * Test of the ResultContainer class
 */

public class ResultContainerTest {
	private static ResultContainer resultContainer;
	private static Database database;
	private static String name="db_Result.sqlite";
	private static String dir="jdbc:sqlite:db"+ File.separator + name;
	private static DatabaseCreator databaseCreator;
	private static Connection con;
	private static PreparedStatement preparedStatement;
	private static Statement stmt;	
	private static ResultSet rs;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		databaseCreator = new DatabaseCreator(name);
		databaseCreator.create();
		database = new Database(dir);	
		resultContainer = new ResultContainer(null,null);
		
		con = database.getConnection();		
		preparedStatement = con.prepareStatement(""
				+ "INSERT INTO TRAIN_SET_TABLE"
				+ " VALUES(null, " + 0 + ",'" + "string" + "'," + "1" +  ")");
		
		stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
            							ResultSet.CONCUR_READ_ONLY);
		rs = stmt.executeQuery("SELECT * FROM EC_TABLE");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		database.closeConnection();
		databaseCreator.delete();
	}
	
	/**
	 * Test the function getRs().
	 * The returned value of a resultContainer initialized to (null,null)
	 * will be a null value. If initialized to 'rs', will return 'rs'.
	 */
	@Test
	public final void testGetRs() throws SQLException {
		resultContainer = new ResultContainer(null,null);
		
		assertNull(resultContainer.getRs());
		resultContainer = new ResultContainer(preparedStatement,rs);
		assertEquals(rs,resultContainer.getRs());
	}

	/**
	 * Test the function close()
	 */
	
	@Test
	public final void testClose() throws SQLException {
		resultContainer = new ResultContainer(preparedStatement,rs);
		assertFalse(preparedStatement.isClosed());
		resultContainer.close();
		assertTrue(preparedStatement.isClosed());
	}	
}
