package com.example.service.impl;

import com.example.context.IdleUserRightsContext;
import com.example.dto.IdleResult;
import com.example.dto.IdleUserRightsCardDTO;
import com.example.enums.StageProcessedResultEnum;
import com.example.service.IdleIMLimitWriteService;
import com.example.service.idle.IdleIMLPipelineFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class IdleIMLimitWriteServiceImpl implements IdleIMLimitWriteService {
    private final IdleIMLPipelineFactory idleIMLPipelineFactory;

    @Override
    public IdleResult<Boolean> consumeQuota(String bizCode, Long userId, Long oppositeUserId, String messageId, Long itemId, Long sessionId) {

        IdleUserRightsContext context = new IdleUserRightsContext();
        IdleUserRightsCardDTO card = new IdleUserRightsCardDTO();
        card.setUserId(userId);
        card.setBizCode(bizCode);
        card.setTemplateCode("setTemplateCode");
        context.setCard(card);
        context.ofResult(StageProcessedResultEnum.SUCCESS);

        log.info("IdleIMLimitWriteServiceImpl.consumeQuota start, context:{}", context);
        context = idleIMLPipelineFactory.execute(context, "IMLConsumeQuotaPipeline");
        log.info("IdleIMLimitWriteServiceImpl.consumeQuota end, context:{}, result:{}", context, context.getResult());

        return IdleResult.success(context.getResult().isSuccess());
    }
}