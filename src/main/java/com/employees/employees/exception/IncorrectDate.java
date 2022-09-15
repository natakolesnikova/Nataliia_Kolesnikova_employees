package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class IncorrectDate extends RuntimeException {

    public IncorrectDate(String s) {
        super(s);
    }

    public IncorrectDate() {
    }
}
