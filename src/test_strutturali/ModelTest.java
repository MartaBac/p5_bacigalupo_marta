package test_strutturali;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

import trainSet.FileType;
import trainSet.Model;

/**
 * Testing the class Model, that provides a representation for 
 * every file type used in the software.
 */

public class ModelTest {
	private static Model e;
	private static String dir ="db"+ File.separator;
	private static Integer id=0;
	private static String name="testing";
	private static String path="//test0";
	private static boolean clicked=false;
	private static FileType fileType=FileType.TRAIN;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		e = new Model(id,name,path,clicked,fileType);
	}
	
	/**
	 * Testing the function getId().
	 * Given a model 'e', e.getId() must return its ID 'id'
	 */

	@Test
	public final void testGetId() {
		assertEquals(id,e.getId(),0.0);
	}
	
	/**
	 * Testing the function getName() 
	 */

	@Test
	public final void testGetName() {
		assertTrue(e.getName().equals(name));
	}
	
	/**
	 * Testing the function getPath() 
	 */

	@Test
	public final void testGetPath() {
		assertTrue(e.getPath().equals(path));
	}
	
	/**
	 * Testing the function exits().
	 * It returns true if the model has a path that corresponds
	 * to an existing file. 
	 */
	
	@Test
	public final void testExist() throws IOException{	
		String s=dir+"name.txt";
		Path p=Paths.get(s);
		Model e1 = new Model(id,"name",s,clicked,fileType);
		PrintWriter writer = new PrintWriter(s, "UTF-8");
	    assertTrue(e1.exist());
	    
	    writer.close();	    
	    Files.deleteIfExists(p);
	    assertFalse(e1.exist());
	}

	/**
	 * Test getClicked()
	 */
	
	@Test
	public final void testGetClicked() {
		
		// Testing the initializing value
		assertEquals(clicked,e.getClicked());
		e.setClicked(true);
		assertEquals(true,e.getClicked());
		e.setClicked(false);
		assertEquals(false,e.getClicked());
	}
	
	/**
	 * Test the function getFileType()
	 */

	@Test
	public final void testGetFileType() {
		assertEquals(fileType,e.getFileType());
	}

	/**
	 * Test the Override function toString()
	 */
	
	@Test
	public final void testToString() {		
		e = new Model(id,name,path,clicked,fileType);
		assertEquals(fileType.name() + " [id = " + id + ", name =" + name + 
			", path = " + path + ", clicked = " + clicked + "]",e.toString());
	}
}
