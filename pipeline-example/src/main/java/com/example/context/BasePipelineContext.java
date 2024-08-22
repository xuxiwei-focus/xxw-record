package com.example.context;

import com.example.dto.IdleUserRightsCardDTO;
import com.example.enums.StageProcessedResultEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BasePipelineContext implements Serializable {
    /**
     * 扩展信息
     */
    private Map<String, String> extInfo;

    /**
     * 处理结果
     */
    private StageProcessedResultEnum result;


    private IdleUserRightsCardDTO card;

}
