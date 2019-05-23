package alex.enterprise.employeenodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Comparable<Employee> {
    private Integer id;
    private String name;
    private String passportNumber;
    private String education;

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }
}
