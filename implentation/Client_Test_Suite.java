import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ ATM_Test.class, Teller_Test.class })
public class Client_Test_Suite {

}
