package com.lms.api.validation;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//This would help all controller to validate their request payload
@RestControllerAdvice
public class HandleBadRequest {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(NotificationStatusCode.CLIENT_BAD_REQUEST.getCode());
        apiResponse.setStatusMessage(NotificationStatusCode.CLIENT_BAD_REQUEST.getDescription());
        apiResponse.setData(errors);

        APIError error = new APIError();
        error.setStatus(NotificationStatusCode.CLIENT_BAD_REQUEST.getHttpStatus());
        error.setStatusMessage("Read the message for fields that are omitted");

        apiResponse.setError(error);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


}