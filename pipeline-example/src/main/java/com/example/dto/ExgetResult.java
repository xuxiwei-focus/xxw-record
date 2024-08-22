package com.example.dto;

import java.io.Serializable;

public class ExgetResult<T> implements Serializable {

    private T value;
    public T getValue() {
        return value;
    }

    public ExgetResult(T value) {
        this.value = value;
    }
}
