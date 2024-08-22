package com.example.service;

import com.example.context.BasePipelineContext;

/**
 * 幂等接口
 * @param <T>
 */
public interface Idempotent<T extends BasePipelineContext>{
    /**
     * 获取幂等key，返回null代表不需要幂等
     * @param context 上下文
     * @return 幂等key
     */
    String getIdempotentKey(T context);
}
