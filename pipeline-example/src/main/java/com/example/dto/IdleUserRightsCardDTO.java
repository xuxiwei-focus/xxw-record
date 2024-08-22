package com.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdleUserRightsCardDTO implements Serializable {


    private Long id;
    private String idempotentKey;
    private Long userId;
    private String bizCode;
    private String templateCode;
}
