package dev.psulej.taskapp.error;

import java.util.List;

public class ValidationException extends RuntimeException {

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }

    final List<ValidationError> errors;
}
