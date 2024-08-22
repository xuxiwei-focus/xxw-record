package com.example.service;

import com.example.dto.IdleResult;

public interface IdleIMLimitWriteService {

    public IdleResult<Boolean> consumeQuota(String bizCode, Long userId, Long oppositeUserId, String messageId, Long itemId, Long sessionId);
}
