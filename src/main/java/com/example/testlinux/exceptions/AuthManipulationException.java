package com.example.testlinux.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthManipulationException extends RuntimeException {
    public AuthManipulationException() {
        super();
    }
    public AuthManipulationException(String message, Throwable cause) {
        super(message, cause);
    }
    public AuthManipulationException(String message) {
        super(message);
    }
    public AuthManipulationException(Throwable cause) {
        super(cause);
    }
}