package com.employees.employees.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employees {
    private Long empId;
    private Long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employees employees = (Employees) o;
        return empId.equals(employees.empId) && projectId.equals(employees.projectId) && dateFrom.equals(employees.dateFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, projectId, dateFrom);
    }
}
