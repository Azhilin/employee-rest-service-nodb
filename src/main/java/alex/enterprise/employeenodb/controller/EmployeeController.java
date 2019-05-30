package alex.enterprise.employeenodb.controller;

import alex.enterprise.employeenodb.model.Employee;
import alex.enterprise.employeenodb.model.Permission;
import alex.enterprise.employeenodb.service.AuxiliaryService;
import alex.enterprise.employeenodb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuxiliaryService auxiliaryService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Integer id) {
        return employeeService.getById(id);
    }

    @PutMapping
    public String add(@RequestBody Employee employee) {
        enrichWithPermission(employee);
        employeeService.add(employee);
        return String.format("Employee with id = %s has been added!", employee.getId());
    }

    @PutMapping("/list")
    public List<Employee> addList(@RequestBody List<Employee> employees) {
        employees.forEach(this::enrichWithPermission);

        employeeService.addList(employees);
        return employeeService.getAll();
    }

    @DeleteMapping("/{id}")
    public String removeEmployeeById(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return String.format("Employee with id = %s has been successfully deleted!", id);
    }

    @DeleteMapping
    public String deleteAll() {
        employeeService.clear();
        return "All employees have been successfully deleted!";
    }

    private Employee enrichWithPermission(Employee e) {
        String permissionUrl = auxiliaryService.addId(e.getId());
        Boolean permissionFlag = auxiliaryService.getByUrl(permissionUrl);

        e.setPermission(Permission.of(permissionUrl, permissionFlag));
        return e;
    }
}
