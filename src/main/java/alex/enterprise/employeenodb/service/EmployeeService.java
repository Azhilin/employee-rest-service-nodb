package alex.enterprise.employeenodb.service;

import alex.enterprise.employeenodb.exception.CustomRuntimeException;
import alex.enterprise.employeenodb.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Service
public class EmployeeService {

    private ConcurrentHashMap<Integer, Employee> employeeMap = new ConcurrentHashMap<>();

    public List<Employee> getAll() {
        return new ArrayList<>(employeeMap.values());
    }

    public Employee getById(Integer id) {
        if (isNull(id)) {
            throw new CustomRuntimeException("id cannot be null");
        }
        return employeeMap.getOrDefault(id, null);
    }

    public void add(Employee employee) {
        if (isNull(employee)) {
            throw new CustomRuntimeException("Employee cannot be null");
        } else if (employeeMap.containsKey(employee.getId())) {
            throw new CustomRuntimeException(String.format("Employee with such id = %s already exists", employee.getId()));
        } else {
            employeeMap.put(employee.getId(), employee);
        }
    }

//    public void addList(List<Employee> employees) {
//        employees.forEach(employee -> employeeDao.save(employee));
//    }
}
