package ru.Golov_Denis.NauJava.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.Golov_Denis.NauJava.exception.ApiException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiException handleAllExceptions(Exception e) {
        return ApiException.create(e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleResourceNotFound(ResourceNotFoundException e) {
        return ApiException.create(e);
    }
}
