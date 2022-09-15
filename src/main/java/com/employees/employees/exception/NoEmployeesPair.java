package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class NoEmployeesPair extends RuntimeException {

    public NoEmployeesPair(String s) {
        super(s);
    }

    public NoEmployeesPair() {
    }
}
