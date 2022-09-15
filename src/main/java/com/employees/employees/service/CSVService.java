package com.employees.employees.service;

import com.employees.employees.model.EmployeesViewEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CSVService {
    boolean isCSVFormat(final MultipartFile multipartFile);
    EmployeesViewEntity pairEmployeesWorkedTogetherOnCommonProjectTheLongestPeriodOfTime(final MultipartFile multipartFile);
}
