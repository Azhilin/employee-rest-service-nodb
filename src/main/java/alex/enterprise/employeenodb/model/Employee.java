package alex.enterprise.employeenodb.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Employee implements Comparable<Employee> {
    @NonNull private Integer id;
    @NonNull private String name;
    @NonNull private String passportNumber;
    @NonNull private String education;
    private Permission permission;

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }
}
