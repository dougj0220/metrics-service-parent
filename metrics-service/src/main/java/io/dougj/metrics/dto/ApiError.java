package io.dougj.metrics.dto;

import java.io.Serializable;

public class ApiError implements Serializable {

    private String errorCode;
    private String errorMessage;

    public ApiError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {return errorCode;}

    public void setErrorCode(String errorCode) {this.errorCode = errorCode;}

    public String getErrorMessage() {return errorMessage;}

    public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}
}
