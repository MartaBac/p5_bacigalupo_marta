package test_strutturali;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import trainSet.Database;
import trainSet.FileType;
import trainSet.Model;
import util.DatabaseCreator;

/**
 * Test of the Database class through parameterization.
 * The methods tested are getClickedModels and removeFromTable.
 * 
 */

@RunWith(value = Parameterized.class)
public class DatabaseParameterizedTest {
	private static String name_DB="db_DBRT.sqlite";
	private static String dir="jdbc:sqlite:db"+ File.separator + name_DB;
	private static Database db;
	private static DatabaseCreator d;
		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		d = new DatabaseCreator(name_DB);
		d.create();
		db = new Database(dir);	
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.closeConnection();
		d.delete();
	}
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
        		{"EC", "", null,Exception.class},
                {"TRAIN","", FileType.TRAIN,null},
                {"train_test","path/", FileType.TRAIN,null}
        });
    }
    @Parameter(value = 0)
    public String name;
    @Parameter(value = 1)
    public String path;
    @Parameter(value = 2)
    public FileType fileType;
    @Parameter(value = 3)
    public Class<? extends Exception> expectedException;   
	
   @Rule
   public ExpectedException thrown = ExpectedException.none();
    
   /**
    * Testing a function that performs the remotion of an entry 
    * from the table fileType. Exception will be thrown when set 
    * an invalid FileType
    */
   
	@Test
	public final void testRemoveFromTable() throws Exception {		
		if(expectedException!=null){
			thrown.expect(expectedException);
		}
		
		db.insertIntoTable(name, path, fileType);	
		assertTrue(db.removeFromTable(name, fileType));	
	}
		
   /**
    * Testing GetClickedModels(FileType).
    * 
    * The function will return an ArrayList<Model> with all the clicked models.
    * Updating all the Models to '0' will return an empty ArrayList; 
    * checking a model you will increase the size of ArrayList.
    */	
	
	@Test
	public final void testGetClickedModels() throws Exception {
		if(expectedException!=null){
			thrown.expect(expectedException);
		}
		assertNotNull(db.getClickedModels(fileType));
		db.insertIntoTable(name, path, fileType);
		db.updateClicked(name, 0, fileType);
		int i= db.getClickedModels(fileType).size();
		
		// Setting to clicked 'name' I increase the number of clickedmodels
		db.updateClicked(name, 1, fileType);
		ArrayList<Model> res = db.getClickedModels(fileType);
		assertEquals(i+1,res.size());
		
		/* name (fileType) is set to clicked, so it's returned by 
		 * getClickedModels(fileType).
		 */
		assertEquals(name,res.get(0).getName());
		db.removeFromTable(name, fileType);		
	}

}
