package com.employees.employees.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeesViewEntity {
    private Long empId1;
    private Long empId2;
    private Long projectDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeesViewEntity that = (EmployeesViewEntity) o;
        return Objects.equals(projectDays, that.projectDays) && Objects.equals(empId1, that.empId1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectDays, empId1);
    }
}
