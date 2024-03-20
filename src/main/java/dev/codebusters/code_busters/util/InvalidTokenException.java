package dev.codebusters.code_busters.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(final String message) {
        super(message);
    }

}
