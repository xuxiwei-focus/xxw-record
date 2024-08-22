package com.example.dto;

import lombok.Data;

@Data
public class ExsetParams {
    private Long nx;
    private Long ex;

    public ExsetParams() {
    }

    public ExsetParams nx(Long nx) {
        this.nx = nx;
        return this;
    }

    public ExsetParams ex(Long pipeLineIdempotentExpireTime) {
        this.ex = pipeLineIdempotentExpireTime;
        return this;
    }
}
