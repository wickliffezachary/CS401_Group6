import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	ATMTests.class,
	TellerTests.class,
	CustomerAccountTests.class,
	BankAccountTests.class
})

public class BankingSystemTestSuite {}