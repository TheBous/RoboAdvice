package com.roboadvice.utils;

public class GenericResponse<T> {

    private T data;
    private int statusCode;
    private String message;


    public GenericResponse(T data, String message, int status) {
        this.statusCode = status;
        this.message = message;
        this.data = data;
    }

    public GenericResponse(T data, int status) {
        this.statusCode = status;
        this.message = "";
        this.data = data;
    }

    public GenericResponse(T data) {
        this.statusCode = 0;
        this.message = null;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

