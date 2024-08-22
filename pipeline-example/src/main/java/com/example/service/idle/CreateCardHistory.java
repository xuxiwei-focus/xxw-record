package com.example.service.idle;


import com.example.annotions.StageConfig;
import com.example.bo.IdleUserRightsService;
import com.example.context.IdleUserRightsContext;
import com.example.dto.IdleUserRightsCardDTO;
import com.example.dto.IdleUserRightsCardHistoryDTO;
import com.example.enums.CommonCharEnum;
import com.example.enums.StageProcessedResultEnum;
import com.example.service.AbstractStageIdempotent;
import com.example.utils.TairKeysConstantsTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@StageConfig(name = "CreateCardHistory")
@RequiredArgsConstructor
@Service
public class CreateCardHistory extends AbstractStageIdempotent<IdleUserRightsContext> {

    private final IdleUserRightsService userRightsService;

//    private final IdleIMLimitQuotaDataService quotaDataService;

    /**
     * 提供一个用于子类处理业务逻辑的入口
     *
     * @param context 上下文
     * @return 执行结果
     */
    @Override
    protected IdleUserRightsContext executeBusinessLogic(IdleUserRightsContext context) {
        IdleUserRightsCardDTO card = context.getCard();
        IdleUserRightsCardHistoryDTO historyDTO = userRightsService.buildHistory(card);
        historyDTO.setPaidCntLeft(historyDTO.getPaidCntLeft() + historyDTO.getCount());
        Long historyId = userRightsService.createHistory(historyDTO);
        return context.ofResult(historyId == null ? StageProcessedResultEnum.WRITE_USER_RIGHTS_FLOW_DB_FAIL : StageProcessedResultEnum.SUCCESS);
    }

    /**
     * 获取幂等key，返回null代表不需要幂等
     *
     * @param context 上下文
     * @return 幂等key
     */
    @Override
    public String getIdempotentKey(IdleUserRightsContext context) {
        IdleUserRightsCardDTO card = context.getCard();
        // 幂等key：idle-local-CreateCardHistory-{bizCode}-{userId}-{cardId}
        return card.getIdempotentKey() != null ? card.getIdempotentKey() : TairKeysConstantsTools.generateKey(CommonCharEnum.MINUS.getValue(), card.getBizCode(),
                String.valueOf(card.getUserId()), String.valueOf(card.getId()));
    }
}