package com.example.context;

import lombok.Data;

@Data
public class IdleIMLContext extends BasePipelineContext {
    private Long userId;
    private String now;
    private String bizCode;
    private Long oppositeUserId;
    private String messageId;
    private Long itemId;
    private Long sessionId;

}
