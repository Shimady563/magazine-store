package com.shimady.magazineaggregator.controller.advise;

import com.shimady.magazineaggregator.exception.AccessDeniedException;
import com.shimady.magazineaggregator.exception.ResourceNotFoundException;
import com.shimady.magazineaggregator.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "sign-up";
    }
}
