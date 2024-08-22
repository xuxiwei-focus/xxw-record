package com.example.service;

import com.example.context.BasePipelineContext;

public interface Stage <T extends BasePipelineContext> {
    /**
     * 执行当前阶段的逻辑
     */
    T execute(T context);
}
