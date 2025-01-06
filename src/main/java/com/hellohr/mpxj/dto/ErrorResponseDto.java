package com.hellohr.mpxj.dto;

public class ErrorResponseDto {

    private String message;
    private String errorDetails;

    public ErrorResponseDto(String message, String errorDetails) {
        this.message = message;
        this.errorDetails = errorDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
