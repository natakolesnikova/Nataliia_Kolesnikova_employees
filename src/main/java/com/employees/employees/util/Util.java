package com.employees.employees.util;

import com.employees.employees.controller.EmployeesController;
import com.employees.employees.exception.CSVException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Component
public class Util {

    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final String PLEASE_ENTER_CORRECT_CSV_FORMAT_FILE = "Please enter correct CSV format file";
    private static final String WAS_ENTERED_INCORRECT_CSV_FORMAT_FILE = "Was entered incorrect CSV format file ";

    public static File multipartToFile(final MultipartFile multipart) {
        final File convFile = new File(Objects.requireNonNull(multipart.getOriginalFilename()));
        final FileOutputStream fos;
        try {
            fos = new FileOutputStream( convFile );
            fos.write(multipart.getBytes() );
            fos.close();
            return convFile;
        } catch (final IOException e) {
            LOG.error(WAS_ENTERED_INCORRECT_CSV_FORMAT_FILE + e.getMessage());
            throw new CSVException(PLEASE_ENTER_CORRECT_CSV_FORMAT_FILE);
        }
    }
}
