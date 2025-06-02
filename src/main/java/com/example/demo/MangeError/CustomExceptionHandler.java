package com.example.demo.MangeError;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleException (Exception e){
		ApiResponse apiError = new ApiResponse();
		apiError.setMessage(e.getMessage());
		apiError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		apiError.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(apiError ,HttpStatus.INTERNAL_SERVER_ERROR);
	}
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
	/*@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
	    ApiError apiError = new ApiError();
	    apiError.setMessage(e.getMessage());
	    apiError.setCode(HttpStatus.BAD_REQUEST.value());
	    apiError.setTimestamp(LocalDateTime.now());
	    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}*/
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                          .getFieldErrors()
                          .stream()
                          .map(error -> error.getField() + ": " + error.getDefaultMessage())
                          .collect(Collectors.joining("; "));

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Erreur de validation : " + errors);
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

}
