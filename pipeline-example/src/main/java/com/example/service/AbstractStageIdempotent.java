package com.example.service;

import com.example.config.SwitchConfigBiz;
import com.example.context.BasePipelineContext;
import com.example.dto.ExgetResult;
import com.example.dto.ExsetParams;
import com.example.enums.CommonCharEnum;
import com.example.enums.StageProcessedResultEnum;
import com.example.utils.TairKeysConstantsTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
@Slf4j
public abstract class AbstractStageIdempotent<T extends BasePipelineContext> implements Stage<T>, Idempotent<T> {
    public static final String APP_NAME = "***";

    /**
     * 【RDB】用于幂等缓存的实现
     */
    @Autowired
    private TairStringInterface tairStringInterface;
    /**
     * 提供一个用于子类处理业务逻辑的入口
     * @param context 上下文
     * @return 执行结果
     */
    protected abstract T executeBusinessLogic(T context);

    @Override
    public T execute(T context)
    {
        // 拿到当前执行的Stage名称
        String simpleName = this.getClass().getSimpleName();
        String idempotentKey = getIdempotentKey(context);
        String key = TairKeysConstantsTools.generateKey(CommonCharEnum.MINUS.getValue(), APP_NAME, simpleName, idempotentKey);
        try {
            // 如果已经处理过，则无需执行业务逻辑，直接跳过当前流程
            if (idempotentKey != null && getMark(key, context)) {
                log.info(simpleName + " is already processed, the idempotent key:{}", key);
                return context;
            }
            // 执行业务逻辑
            context = executeBusinessLogic(context);
            // 标记为处理过（仅当业务执行成功时）
            if (idempotentKey != null && context.getResult() != null && context.getResult().isSuccess()) {
                if(!marked(key, context)) {
                    // 执行失败，则抛出异常
                    log.error(simpleName + " marked error, the idempotent key:{}", key);
                    context.setResult(StageProcessedResultEnum.IDEMPOTENT_FAIL);
                }
                log.info(simpleName + " execute success, marked idempotent key:{}", key);
            }
        } catch (Exception e) {
            log.error(simpleName + " execute error, the idempotent key:{}, context:{}", key, e, context);
            context.setResult(StageProcessedResultEnum.IDEMPOTENT_FAIL);
        }
        return context;
    }
    /**
     * 检查是否存在标记值
     * @param key 幂等key
     * @return 是否存在
     */
    private boolean getMark(String key, T context) {
        ExgetResult<String> result = tairStringInterface.exget(key);
        if (result != null && StringUtils.isNotEmpty(result.getValue())) {
            return "1".equals(result.getValue());
        }
        return false;
    }
    /**
     * 标记
     * @param key 幂等key
     * @return 标记结果
     */
    private boolean marked(String key, T context) {
        // 带上nx、ex参数
        ExsetParams params = new ExsetParams().nx(1L).ex(SwitchConfigBiz.pipeLineIdempotentExpireTime);
        String reply = tairStringInterface.exset(key, "1", params);
        return "OK".equals(reply);
    }
}