package dev.psulej.taskapp.common.error;

import java.util.List;

public class ValidationErrorResponse {
    private final List<ValidationError> errors;

    public ValidationErrorResponse(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
