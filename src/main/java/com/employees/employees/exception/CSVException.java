package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class CSVException extends RuntimeException {

    public CSVException(String s) {
        super(s);
    }

    public CSVException() {
    }
}
