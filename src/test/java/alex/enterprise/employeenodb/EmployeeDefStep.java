package alex.enterprise.employeenodb;

import alex.enterprise.employeenodb.model.Employee;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class EmployeeDefStep extends SpringIntegrationTest {

    private List<Employee> expected;
    private List<Employee> actual;

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {
        restTemplate.put("http://localhost:9090/employee/list", employees);
        expected = employees;
    }


    @When("we send {string} request to the {string} endpoint")
    public void weSendGETRequestToTheEmployeeEndpoint(String methodName, String endpoint) {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(
                "http://localhost:9090" + endpoint,
                HttpMethod.resolve(methodName),
                null,
                new ParameterizedTypeReference<List<Employee>>() {
                }
        );

        actual = responseEntity.getBody();
    }

    @Then("retrieved data is equal to added data")
    public void retrievedDataIsEqualToAddedData() {
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @After
    public void cleanUp() {
        expected.clear();
        actual.clear();
        restTemplate.delete("http://localhost:9090/employee");
    }
}
