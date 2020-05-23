package com.testtask.contactbook.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
