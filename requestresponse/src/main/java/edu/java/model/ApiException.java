package edu.java.model;

public class ApiException extends Exception {
    public final int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
