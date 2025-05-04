import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class BankingSystemTestRunner {
	
	public static void main(String[] args) {
		
		// run the tests for DVD and DVDCollection
		Result testResults = JUnitCore.runClasses(BankingSystemTestSuite.class);
		
		// print out a list of JUnit tests that failed
		System.out.println("FAILED UNIT TESTS:");
		for (Failure testFailure : testResults.getFailures()) {
			System.out.println(testFailure.toString());
		}
	
	}
	
}
