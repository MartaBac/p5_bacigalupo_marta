package test_strutturali;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Test;
import trainSet.FileType;
import trainSet.OpenFile;

/**
 * Test of the OpenFile class
 */

public class OpenFileTest {
	private static OpenFile op;
	private static FileType fileType = FileType.TRAIN;

	@Test
	public final void testOpenFile() {
		op = new OpenFile(fileType);
	}

	/**
	 * Testing if the constructor has set the correct path
	 * 
	 */
	
	@Test
	public final void testGetPath() {
		testOpenFile();
		assertEquals("",op.getPath().toString());
	}

	/**
	 * Testing if the constructor has set the correct Name
	 * 
	 */
	
	@Test
	public final void testGetName() {
		testOpenFile();
		assertEquals("",op.getName().toString());
	}
	
	/**
	 * Testing toString function
	 * 
	 */

	@Test
	public final void testToString() {
		testOpenFile();
		assertNotNull(op.toString());		
	}

}
