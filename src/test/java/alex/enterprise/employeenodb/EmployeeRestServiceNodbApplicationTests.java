package alex.enterprise.employeenodb;

import alex.enterprise.employeenodb.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeRestServiceNodbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeRestServiceNodbApplicationTests {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;

    @Before
    public void init() {
        Employee employee = new Employee(105, "Alex", "AL321654", "High");

        restTemplate.put("http://localhost:" + port + "/employee/add", employee);
    }

    @Test
    public void testRetrieveEmployeeAllList() {

        String response = restTemplate.getForObject("http://localhost:" + port + "/employee/all", String.class);

        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/employee/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Employee>>() {
                }
        );

        String expected = "[{\"id\":105,\"name\":\"Alex\",\"passportNumber\":\"AL321654\",\"education\":\"High\"}]";

        List<Employee> employees = responseEntity.getBody();

//        Assertions.assertThat(response).isEqualTo(expected);
//
//        log.info("Response body: {}", response);
    }

}
