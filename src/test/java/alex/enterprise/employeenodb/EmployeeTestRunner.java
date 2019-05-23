package alex.enterprise.employeenodb;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", plugin = {"progress", "html:target/cucumber"})
public class EmployeeTestRunner extends SpringIntegrationTest {
}
