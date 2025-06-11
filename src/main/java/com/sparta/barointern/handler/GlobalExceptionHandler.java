package com.sparta.barointern.handler;

import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResult> handleCustom(CustomException ex) {
		ExceptionCode code = ex.getExceptionCode();

		ErrorResponse response =
			new ErrorResponse(code.getCode(), code.getMessage());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResult(response));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResult> handleAccessDenied(AccessDeniedException ex) {
		ExceptionCode code = ExceptionCode.ACCESS_DENIED;

		ErrorResponse response =
			new ErrorResponse(code.getCode(), code.getMessage());

		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(new ErrorResult(response));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResult> handleAuthError(AuthenticationException ex) {
		ExceptionCode code = ExceptionCode.INVALID_TOKEN;

		ErrorResponse response =
			new ErrorResponse(code.getCode(), code.getMessage());

		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResult(response));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		org.springframework.web.context.request.WebRequest request) {

		String details = ex.getBindingResult().getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining(", "));

		ErrorResponse response =
			new ErrorResponse("INVALID_INPUT", details);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResult(response));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		org.springframework.web.context.request.WebRequest request) {

		ErrorResponse response =
			new ErrorResponse("MALFORMED_JSON", "잘못된 JSON 형식입니다.");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResult(response));
	}

	public static record ErrorResponse(
		String code,
		String message
	) {}

	public static record ErrorResult(
		ErrorResponse error
	) {}
}