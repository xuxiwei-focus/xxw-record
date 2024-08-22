package com.example.context;

import com.example.dto.IdleUserRightsCardDTO;
import com.example.enums.StageProcessedResultEnum;
import lombok.Data;

/**
 * 具体业务context
 */
@Data
public class IdleUserRightsContext extends BasePipelineContext {


    /**
     * 构建返回结果
     */
    public IdleUserRightsContext ofResult(StageProcessedResultEnum result) {
        super.setResult(result);
        return this;
    }
}
