package com.hrcentral.nphc.helper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.id.IdentifierGenerationException;

//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger();

	private ResponseEntity<Object> createResponseEntity(ResponseObject obj, HttpStatus status) {
		return new ResponseEntity<Object>(obj, status);
	}

	public @ResponseBody ResponseEntity<ResponseObject> handleException(RuntimeException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(ex.getMessage()));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {

		if (ex.getMessage().toLowerCase().contains(ResponseMessage.MSG_ERR_MISSING_REQUEST_BODY.toLowerCase())) {
			return createResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_MISSING_REQUEST_BODY),
					HttpStatus.BAD_REQUEST);
		}

		else if (ex.getMessage().toLowerCase().contains("json parse error: unexpected character")) {
			return createResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_REQUEST_INVALID),
					HttpStatus.BAD_REQUEST);
		}
		else if (ex.getMessage().toLowerCase()
				.contains("json parse error: unexpected end-of-input: expected close marker for object")) {
			return createResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_REQUEST_INVALID),
					HttpStatus.BAD_REQUEST);
		} else if (ex.getMessage().toLowerCase().contains("json parse error: cannot deserialize value of type")) {
			return createResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_REQUEST_INVALID),
					HttpStatus.BAD_REQUEST);
		} else if (ex.getMessage().toLowerCase().contains("json parse error: illegal unquoted character")) {
			return createResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_REQUEST_INVALID),
					HttpStatus.BAD_REQUEST);
		}

		logger.error(ex.getMessage(), ex);

		return handleExceptionInternal(ex, null, headers, status, request);
	}

//	@ExceptionHandler(Exception.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	protected ResponseEntity<Object> handleAllException(Exception ex) {
//		logger.error(ex.getMessage(), ex);
//		return createResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_UNKNOWN), HttpStatus.BAD_REQUEST);
//	}

}


