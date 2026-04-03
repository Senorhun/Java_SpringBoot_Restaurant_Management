package com.example.restaurant.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        validationErrors.forEach(validationError -> {
            log.error("Error in validation: " + validationError.getField() + ": " + validationError.getErrorMessage());
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        ValidationError validationError = new ValidationError("employeeId",
                "Employee not found with id: " + exception.getEmployeeId());
        log.error("Error in validation: " + validationError.getField() + ": " + validationError.getErrorMessage());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleRestaurantNotFoundException(RestaurantNotFoundException exception) {
        ValidationError validationError = new ValidationError("restaurant_id",
                "Restaurant not found with id: " + exception.getRestaurantId());
        log.error("Error in validation: " + validationError.getField() + ": " + validationError.getErrorMessage());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleMenuItemNotFoundException(MenuItemNotFoundException exception) {
        ValidationError validationError = new ValidationError("menuItem_id",
                "MenuItem not found with id: " + exception.getMenuItemId());
        log.error("Error in validation: " + validationError.getField() + ": " + validationError.getErrorMessage());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<ValidationError>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        if (exception.getRequiredType() != null && exception.getRequiredType().isEnum()) {
            String allowedValues = Arrays.toString(exception.getRequiredType().getEnumConstants());
            ValidationError error = new ValidationError(exception.getName(),
                    "Invalid value: " + exception.getValue() + ". Allowed values: " + allowedValues
            );
            log.warn("Invalid enum value '{}' for parameter '{}'", exception.getValue(), exception.getName());
            return new ResponseEntity<>(List.of(error), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                List.of(new ValidationError("request", "Invalid parameter")),
                HttpStatus.BAD_REQUEST
        );
    }

}
