package com.example.dto;

public class IdleResult<T> {

    private String code;
    private String message;

    private T result;

    public IdleResult(String code, T result) {
        this.code = code;
        this.result =result;
    }

    public static IdleResult<Boolean> success(Boolean success) {
        return new IdleResult("0000", success);
    }
}
