package test_strutturali;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Structural test suite
 */

@RunWith(Suite.class)
@SuiteClasses({ DatabaseParameterizedTest.class, DatabaseTest.class, 
			FileManagerTest.class, ModelTest.class, OpenFileTest.class, 
				ResultContainerTest.class, TrainSetListenerTest.class })
public class AllTests {

}
