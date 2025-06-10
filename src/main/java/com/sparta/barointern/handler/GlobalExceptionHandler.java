package com.sparta.barointern.handler;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustom(CustomException ex) {
		ExceptionCode code = ex.getExceptionCode();
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(code.getCode(), code.getMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		ExceptionCode code = ExceptionCode.ACCESS_DENIED;
		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(new ErrorResponse(code.getCode(), code.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthError(AuthenticationException ex) {
		ExceptionCode code = ExceptionCode.INVALID_TOKEN;
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(code.getCode(), code.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String details = ex.getBindingResult().getFieldErrors().stream()
			.map(e -> e.getField() + ": " + e.getDefaultMessage())
			.collect(Collectors.joining(", "));
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse("INVALID_INPUT", details));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleParseError(HttpMessageNotReadableException ex) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse("MALFORMED_JSON", "잘못된 JSON 형식입니다."));
	}

	public static record ErrorResponse(
		String code,
		String message) {}
}
