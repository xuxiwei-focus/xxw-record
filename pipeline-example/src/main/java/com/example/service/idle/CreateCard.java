package com.example.service.idle;


import com.example.annotions.StageConfig;
import com.example.bo.IdleUserRightsService;
import com.example.context.IdleUserRightsContext;
import com.example.dto.IdleUserRightsCardDTO;
import com.example.enums.CommonCharEnum;
import com.example.enums.StageProcessedResultEnum;
import com.example.service.AbstractStageIdempotent;
import com.example.utils.TairKeysConstantsTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@StageConfig(name = "CreateCard")
@RequiredArgsConstructor
@Service
/**
 * 写流水记录
 */
public class CreateCard extends AbstractStageIdempotent<IdleUserRightsContext> {

    private final IdleUserRightsService userRightsService;

    /**
     * 提供一个用于子类处理业务逻辑的入口
     *
     * @param context 上下文
     * @return 执行结果
     */
    @Override
    protected IdleUserRightsContext executeBusinessLogic(IdleUserRightsContext context) {
        IdleUserRightsCardDTO cardDTO = context.getCard();
        // 写权益卡记录
        Long cardId = userRightsService.createCard(cardDTO);
        // 写权益卡id字段，用于下游使用
        cardDTO.setId(cardId);
        return context.ofResult(cardId == null ? StageProcessedResultEnum.WRITE_USER_RIGHTS_RECORD_DB_FAIL : StageProcessedResultEnum.SUCCESS);
    }

    /**
     * 获取幂等key，返回null代表不需要幂等
     *
     * @param context 上下文
     * @return 幂等key
     */
    @Override
    public String getIdempotentKey(IdleUserRightsContext context) {
        if(Objects.isNull(context)) {
            return null;
        }
        IdleUserRightsCardDTO card = context.getCard();
        // 幂等key：idle-local-CreateCard-{bizCode}-{userId}-{ppPayId}
        return card.getIdempotentKey() != null ? card.getIdempotentKey() :
                TairKeysConstantsTools.generateKey(CommonCharEnum.MINUS.getValue(), String.valueOf(card.getUserId()), card.getBizCode(), card.getTemplateCode());
    }
}