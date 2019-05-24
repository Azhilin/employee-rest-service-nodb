package alex.enterprise.employeenodb;

import alex.enterprise.employeenodb.exception.CustomRuntimeException;
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

    private List<Employee> expectedList;
    private List<Employee> actualList;
    private Employee expected;
    private Employee actual;

    private String baseUrl = "http://localhost:";

    @Before
    public void doSetup() {
        long threadId = Thread.currentThread().getId();
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        log.info(String.format("Started in thread: %s, in JVM: %s", threadId, processName));
    }

    @After
    public void cleanUp() {
        if (Objects.nonNull(expectedList)) {
            expectedList.clear();
        } else if (Objects.nonNull(actualList)) {
            actualList.clear();
        }

        restTemplate.delete(String.format("%s%s/employee", baseUrl, port));
    }

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {
        restTemplate.put(String.format("%s%s/employee/list", baseUrl, port), employees);
        expectedList = employees.stream().sorted().collect(Collectors.toList());
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

        actualList = Objects.requireNonNull(responseEntity.getBody()).stream().sorted().collect(Collectors.toList());
    }

    @Then("retrieved data is equal to added data")
    public void retrievedDataIsEqualToAddedData() {
        Assertions.assertThat(actualList).isEqualTo(expectedList);
    }

    @When("we send {string} request to the {string} endpoint with {int} id")
    public void weSendGETRequestToTheEmployeeEndpointWithId(String methodName, String endpoint, int id) {
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(
                String.format("%s%s/%s/%s", baseUrl, port, endpoint, id),
                HttpMethod.resolve(methodName),
                null,
                Employee.class
        );

        actual = Objects.requireNonNull(responseEntity.getBody());

        expected = expectedList.stream().filter(employee -> employee.getId() == id).findFirst()
                .orElseThrow(() -> new CustomRuntimeException(String.format("Expected employee not found with id = %s", id)));
    }

    @Then("retrieved data is equal to added data for specified id")
    public void retrievedDataIsEqualToAddedDataForSpecifiedId() {
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
