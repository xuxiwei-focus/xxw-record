package com.example.bo;

import com.alibaba.fastjson.JSON;
import com.example.dto.IdleUserRightsCardDTO;
import com.example.dto.IdleUserRightsCardHistoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
@Slf4j
public class IdleUserRightsService {
    public Long createCard(IdleUserRightsCardDTO cardDTO) {
        cardDTO  = Optional.ofNullable(cardDTO).orElse(new IdleUserRightsCardDTO());
        cardDTO.setId(RandomGenerator.getDefault().nextLong());
        // 数据库保存注册
        log.info("IdleUserRightsService createCard:{}", JSON.toJSONString(cardDTO));
        return cardDTO.getId();
    }

    public IdleUserRightsCardHistoryDTO buildHistory(IdleUserRightsCardDTO card) {
        IdleUserRightsCardHistoryDTO idleUserRightsCardHistoryDTO = new IdleUserRightsCardHistoryDTO();
        idleUserRightsCardHistoryDTO.setPaidCntLeft("1");
        idleUserRightsCardHistoryDTO.setCount("1");
        idleUserRightsCardHistoryDTO.setId(1L);
        log.info("IdleUserRightsService buildHistory:{}", JSON.toJSONString(idleUserRightsCardHistoryDTO));
        return idleUserRightsCardHistoryDTO;
    }

    public Long createHistory(IdleUserRightsCardHistoryDTO historyDTO) {
        // 数据库保存流水
        log.info("IdleUserRightsService createHistory:{}", JSON.toJSONString(historyDTO));
        return historyDTO.getId();
    }
}
