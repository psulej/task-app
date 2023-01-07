package dev.psulej.taskapp.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public enum ValidationError {
    LOGIN_EXISTS,
    EMAIL_EXISTS
}

