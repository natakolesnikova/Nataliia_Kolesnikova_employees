package com.employees.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleNotCSVFileException(final CSVException csvException) {
        CustomExceptionResponse response = new CustomExceptionResponse(csvException.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleIncorrectDate(final IncorrectDate incorrectDate) {
        CustomExceptionResponse response = new CustomExceptionResponse(incorrectDate.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleNoEmployeesPair(final NoEmployeesPair incorrectDate) {
        CustomExceptionResponse response = new CustomExceptionResponse(incorrectDate.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
