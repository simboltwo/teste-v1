package com.naapi.naapi.controllers.handlers;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends StandardError {
    
    private final List<FieldMessage> errors = new ArrayList<>();

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
