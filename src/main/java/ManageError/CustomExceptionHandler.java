package ManageError;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	/*@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
	    ApiError apiError = new ApiError();
	    apiError.setMessage(e.getMessage());
	    apiError.setCode(HttpStatus.BAD_REQUEST.value());
	    apiError.setTimestamp(LocalDateTime.now());
	    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}*/

}
