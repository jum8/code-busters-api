package dev.codebusters.code_busters.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongAnswerException extends RuntimeException {

    public WrongAnswerException() {
        super();
    }

    public WrongAnswerException(final String message) {
        super(message);
    }

}
