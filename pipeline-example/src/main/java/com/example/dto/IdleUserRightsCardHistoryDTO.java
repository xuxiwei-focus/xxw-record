package com.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdleUserRightsCardHistoryDTO implements Serializable {
    private Long id;
    private String paidCntLeft;
    private String count;
}
