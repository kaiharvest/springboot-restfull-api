package indradwi_restfull.controller;

import indradwi_restfull.model.WebResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
		String errors = exception.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(", "));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(WebResponse.<String>builder().errors(errors).build());
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
		return ResponseEntity.status(exception.getStatusCode())
				.body(WebResponse.<String>builder().errors(exception.getReason()).build());
	}
}
