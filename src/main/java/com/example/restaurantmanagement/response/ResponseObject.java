package com.example.restaurantmanagement.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResponseObject {

    private boolean isSuccess = false;
    private Object data;
    private String errorCode = "ERROR"; // default error
    private String message;
    private String requestId;

    public ResponseObject() {
        this.requestId = UUID.randomUUID().toString();
    }

    public ResponseObject(Object data) {
        this.isSuccess = true;
        this.errorCode = "SUCCESS";
        this.message = "Success";
        this.data = data;
        this.requestId = UUID.randomUUID().toString();
    }

    public ResponseObject(String errorCode, String message) {
        this.isSuccess = false;
        this.errorCode = errorCode;
        this.message = message;
        this.requestId = UUID.randomUUID().toString();
    }

    public ResponseObject(Page<?> pageData) {
        this.isSuccess = true;
        this.errorCode = HttpStatus.OK.getReasonPhrase();
        this.message = "Success";
        this.requestId = UUID.randomUUID().toString();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
