package alex.enterprise.employeenodb;

import alex.enterprise.employeenodb.model.Employee;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class EmployeeDefStep extends SpringIntegrationTest {

    private List<Employee> expected;
    private List<Employee> actual;

    private String baseUrl = "http://localhost:";

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {
        restTemplate.put(String.format("%s%s/employee/list", baseUrl, port), employees);
        expected = employees.stream().sorted().collect(Collectors.toList());
    }


    @When("we send {string} request to the {string} endpoint")
    public void weSendGETRequestToTheEmployeeEndpoint(String methodName, String endpoint) {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(
                String.format("%s%s/%s", baseUrl, port, endpoint),
                HttpMethod.resolve(methodName),
                null,
                new ParameterizedTypeReference<List<Employee>>() {
                }
        );

        actual = Objects.requireNonNull(responseEntity.getBody()).stream().sorted().collect(Collectors.toList());
    }

    @Then("retrieved data is equal to added data")
    public void retrievedDataIsEqualToAddedData() {
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Before
    public void doSetup() {
        long threadId = Thread.currentThread().getId();
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        log.info(String.format("Started in thread: %s, in JVM: %s", threadId, processName));
    }

    @After
    public void cleanUp() {
        expected.clear();
        actual.clear();
        restTemplate.delete(String.format("%s%s/employee", baseUrl, port));
    }
}
