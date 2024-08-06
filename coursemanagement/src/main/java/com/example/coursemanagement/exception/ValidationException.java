package com.example.coursemanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ValidationException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ValidationException.class);

    private final Map<String, String> errors = new HashMap<>();

    public ValidationException(String message) {
        super(message);
        logger.error(message);
        errors.put("error", message);
    }

    public ValidationException(List<String> messages) {
        super(String.join(", ", messages));
        logger.error(String.join(", ", messages));
        for (int i = 0; i < messages.size(); i++) {
            errors.put("error" + (i + 1), messages.get(i));
        }
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

