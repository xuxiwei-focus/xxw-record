package com.example.config;
import java.util.*;

public class SwitchConfigBiz {

    public static Map<String, List<String>> pipeLineBizExecuteChain = new HashMap<String, List<String>>() {{
        put("IMLConsumeQuotaPipeline", List.of(
                // 创建权益卡
                "CreateCard",
                // 创建流水记录
                "CreateCardHistory"
                // "其他操作"
                // ......
        ));
//      ...其他不同的pipeline配置
    }};
    public static Long pipeLineIdempotentExpireTime = 60 * 1000L;
}
