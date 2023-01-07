package dev.psulej.taskapp.common.error;

import java.util.List;

public class ValidationException extends RuntimeException {

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }

    final List<ValidationError> errors;
}
