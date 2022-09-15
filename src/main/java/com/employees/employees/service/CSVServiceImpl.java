package com.employees.employees.service;

import com.employees.employees.exception.CSVException;
import com.employees.employees.exception.IncorrectDate;
import com.employees.employees.exception.NoEmployeesPair;
import com.employees.employees.model.Employees;
import com.employees.employees.model.EmployeesViewEntity;
import com.employees.employees.model.ProjectTime;
import com.employees.employees.util.Util;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.List.of;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Service
public class CSVServiceImpl implements CSVService {

    private static final String TYPE = "text/csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public EmployeesViewEntity pairEmployeesWorkedTogetherOnCommonProjectTheLongestPeriodOfTime(final MultipartFile multipartFile) {
        final File fileEmployees = Util.multipartToFile(multipartFile);
        final List<Employees> parsedEmployees = parseCSVData(fileEmployees);
        final Map<Long, List<ProjectTime>> employeesProjectTime = mapByEmployeeId(parsedEmployees);
        return findEmployeesPairWithMaxWorkingTimeOnCommonProjects(employeesProjectTime);
    }

    private EmployeesViewEntity findEmployeesPairWithMaxWorkingTimeOnCommonProjects(final Map<Long, List<ProjectTime>> employeesProjectTime) {
        Long maxCommonProjectDays = -1L;
        Long maxEmplId1 = 0L;
        Long maxEmplId2 = 0L;

        final Set<Long> keySet = employeesProjectTime.keySet();
        final List<Long> emplId = new ArrayList<>(keySet);

        if (emplId.size() < 2) {
            throw new NoEmployeesPair("There is no employees pair");
        }

        for (int i = 0; i < emplId.size() - 1; i++) {
            for (int j = i + 1; j < emplId.size(); j++) {

                final List<ProjectTime> projectTimesList1 = employeesProjectTime.get(emplId.get(i));
                final List<ProjectTime> projectTimesList2 = employeesProjectTime.get(emplId.get(j));
                final Long commonProjectDays = getCommonProjectsDays(projectTimesList1, projectTimesList2);
                if (commonProjectDays > maxCommonProjectDays) {
                    maxCommonProjectDays = commonProjectDays;
                    maxEmplId1 = emplId.get(i);
                    maxEmplId2 = emplId.get(j);
                }
            }
        }
        return new EmployeesViewEntity(maxEmplId1, maxEmplId2, maxCommonProjectDays);
    }

    private Map<Long, List<ProjectTime>> mapByEmployeeId(final List<Employees> parsedEmployees) {
        final Map<Long, List<ProjectTime>> employeesProjectTime = new HashMap<>();

        for (final Employees employee: parsedEmployees) {
            var projectTime = new ProjectTime(employee.getProjectId(), employee.getDateFrom(), employee.getDateTo());

            List<ProjectTime> projectTimes = employeesProjectTime.get(employee.getEmpId());
            if (isNull(projectTimes)) {
                employeesProjectTime.put(employee.getEmpId(), of(projectTime));
            } else {
                projectTimes.add(projectTime);
            }
        }
        return employeesProjectTime;
    }

    private Long getCommonProjectsDays(final List<ProjectTime> projectTimesList1, final  List<ProjectTime> projectTimesList2) {
        Long commonProjectsDays = 0L;

        for (final ProjectTime projectTime1: projectTimesList1) {
            for (final ProjectTime projectTime2: projectTimesList2) {
                if (projectTime1.getProjectId().equals(projectTime2.getProjectId())) {
                    commonProjectsDays += compareAndGetCommonDays(projectTime1.getDateFrom(),
                            projectTime1.getDateTo(), projectTime2.getDateFrom(), projectTime2.getDateTo());
                }
            }
        }
        return commonProjectsDays;
    }

    private Long compareAndGetCommonDays(final LocalDate dateFrom1, final LocalDate dateTo1, final LocalDate dateFrom2, final LocalDate dateTo2) {
        if (dateFrom1.isAfter(dateTo1)) {
            throw new IncorrectDate("Start date " + dateFrom1.format(FORMATTER) + " is after End date " + dateTo1.format(FORMATTER));
        }
        if (dateFrom2.isAfter(dateTo2)) {
            throw new IncorrectDate("Start date " + dateFrom2.format(FORMATTER) + " is after End date " + dateTo2.format(FORMATTER));
        }

        final LocalDate from = dateFrom1.isAfter(dateFrom2) ? dateFrom1 : dateFrom2;
        final LocalDate to = dateTo1.isBefore(dateTo2) ? dateTo1 : dateTo2;
        long commonDays = ChronoUnit.DAYS.between(from, to);
        return (commonDays > 0) ? commonDays : 0;
    }

    private List<Employees> parseCSVData(final File fileEmployees) {
        final List<Employees> parsedEmployees;

        try (CSVReader csvReader = new CSVReaderBuilder(
                new FileReader(fileEmployees)).withSkipLines(1).build()) {

            parsedEmployees = csvReader.readAll()
                    .stream()
                    .map(mapCSVDataToEmployees())
                    .collect(Collectors.toList());

            return parsedEmployees;

        } catch (IOException e) {
            throw new CSVException("Please enter correct CSV format file");
        }
    }

    private Function<String[], Employees> mapCSVDataToEmployees() {
        return data -> {
            Employees csvObject = new Employees();
            try {
                csvObject.setEmpId(Long.parseLong(data[0]));
                csvObject.setProjectId(Long.parseLong(data[1]));
                csvObject.setDateFrom(LocalDate.parse(data[2], FORMATTER));

                if (isNoneBlank(data[3])) {
                    csvObject.setDateTo(LocalDate.parse(data[3], FORMATTER));
                } else {
                    csvObject.setDateTo(LocalDate.now());
                }
            } catch (Exception e) {
                throw new CSVException("Invalid input data from CSV " + e.getMessage());
            }
            return csvObject;
        };
    }

    public boolean isCSVFormat(final MultipartFile multipartFile) {
        return TYPE.equals(multipartFile.getContentType());
    }
}
