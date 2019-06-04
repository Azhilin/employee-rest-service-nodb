package alex.enterprise.employeenodb.employee;

import alex.enterprise.employeenodb.exception.CustomRuntimeException;
import alex.enterprise.employeenodb.model.Employee;
import alex.enterprise.employeenodb.spring.SpringIntegrationTest;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static alex.enterprise.employeenodb.util.session.SessionKey.ACTUAL_RESULT;
import static alex.enterprise.employeenodb.util.session.SessionKey.EXPECTED_RESULT;

@Slf4j
public class EmployeeDefStep extends SpringIntegrationTest {

    private List<Employee> expectedList;
    private List<Employee> actualList;

    @Before
    public void doSetup() {

        //Was added to test the parallel execution:
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

        restTemplate.delete(String.format("%s/employee", baseUrl));
        getSoftAssertions().assertAll();
        session.clear();
    }

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {

        restTemplate.put(String.format("%s/employee/list", baseUrl), employees);
        expectedList = employees.stream().sorted().collect(Collectors.toList());
    }


    @When("we send {string} request to the {string} endpoint")
    public void weSendGETRequestToTheEmployeeEndpoint(String methodName, String endpoint) {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(
                String.format("%s/%s", baseUrl, endpoint),
                HttpMethod.resolve(methodName),
                null,
                new ParameterizedTypeReference<List<Employee>>() {
                }
        );

        actualList = Objects.requireNonNull(responseEntity.getBody()).stream().sorted().collect(Collectors.toList());
    }

    @Then("retrieved data is equal to added data")
    public void retrievedDataIsEqualToAddedData() {
        getSoftAssertions().assertThat(actualList).isEqualTo(expectedList);
    }

    @When("we send {string} request to the {string} endpoint with {int} id")
    public void weSendGETRequestToTheEmployeeEndpointWithId(String methodName, String endpoint, int id) {
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(
                String.format("%s/%s/%s", baseUrl, endpoint, id),
                HttpMethod.resolve(methodName),
                null,
                Employee.class
        );

        Employee actual = Objects.requireNonNull(responseEntity.getBody());

        session.put(ACTUAL_RESULT, actual);

        Employee expected = expectedList.stream().filter(employee -> employee.getId() == id).findFirst()
                .orElseThrow(() -> new CustomRuntimeException(String.format("Expected employee not found with id = %s", id)));

        session.put(EXPECTED_RESULT, expected);
    }

    @Then("retrieved data is equal to added data for specified id")
    public void retrievedDataIsEqualToAddedDataForSpecifiedId() {
        getSoftAssertions()
                .assertThat(session.get(ACTUAL_RESULT, Employee.class))
                .isEqualTo(session.get(EXPECTED_RESULT, Employee.class));
    }
}
