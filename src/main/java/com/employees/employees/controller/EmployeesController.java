package com.employees.employees.controller;

import com.employees.employees.exception.CSVException;
import com.employees.employees.model.EmployeesViewEntity;
import com.employees.employees.service.CSVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EmployeesController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeesController.class);
    private static final String PLEASE_ENTER_CSV_FORMAT_FILE = "Please enter CSV format file";

    @Autowired
    private CSVService csvService;

    @PostMapping(value = "/file/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        EmployeesViewEntity employeesViewEntity;
        if (csvService.isCSVFormat(multipartFile)) {
            employeesViewEntity = csvService.pairEmployeesWorkedTogetherOnCommonProjectTheLongestPeriodOfTime(multipartFile);
        } else {
            throw new CSVException(PLEASE_ENTER_CSV_FORMAT_FILE);
        }
        return ResponseEntity.ok(employeesViewEntity);
    }

}
