package com.example.service;


import com.example.annotions.StageConfig;
import com.example.context.BasePipelineContext;
import com.example.enums.StageProcessedResultEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractPipelineFactory<T extends BasePipelineContext> {

    @Resource(name = "pipelineMaps")
    private Map<String, List<Stage<T>>> pipelineMaps;

    /**
     * 执行PipeLine策略
     * @return 执行结果
     */
    public T execute(T context, String bizPipelineType) {
        List<Stage<T>> executeChains = pipelineMaps.get(bizPipelineType);
        if(CollectionUtils.isEmpty(executeChains)) {
            log.error("PipelineFactory execute error executeChains is null, bizPipelineType:{}", bizPipelineType);
            return null;
        }

        log.info("PipelineFactory execute, bizPipelineType:{}, executeChains:{}", bizPipelineType, executeChains);
        // aroundAspectFunc()增强下
        List<Stage<T>> enhancedFunctionList = executeChains
                .stream()
                .map(this::aroundAspectFunc)
                .collect(Collectors.toList());

        // 获取执行结果
        return getPipeLineResult(context, enhancedFunctionList).orElse(context);
    }

    /**
     * Pipe执行器
     *
     * @param context      入参，类型I
     * @param functionList pipeLine方法列表，每个函数的输入类型为I，输出类型为O
     * @return 执行结果，类型O
     */
    private Optional<T> getPipeLineResult(T context, List<Stage<T>> functionList) {
        if (CollectionUtils.isEmpty(functionList)) {
            return Optional.empty();
        }

        // 执行每一个stage
        for (Stage<T> f : functionList) {
            if(Objects.isNull(context)) {
                return Optional.empty();
            }

            // 一些特殊ResultEnum处理，例如SKIP_ALL，直接跳过所有的流程，立即结束
            if(context.getResult() != null && context.getResult().equals(StageProcessedResultEnum.SKIP_ALL)) {
                break;
            }

            context = f.execute(context);
        }

        return Optional.ofNullable(context);
    }

    /**
     * Pipe环绕切面，apply -> function
     *
     * @param func 当前方法
     * @return 增强后的新方法
     */
    private Stage<T> aroundAspectFunc(Stage<T> func) {
        return req -> {
            StageConfig annotation = func.getClass().getAnnotation(StageConfig.class);
            String methodName = annotation.name();
            // 用于业务自定义的前置检查逻辑
            if(!preContextCheck(methodName, req)) {
                return null;
            }
            // 正式执行
            T result = func.execute(req);

            // 用于业务自定义的后置通知
            afterResult(methodName, result);
            return result;
        };
    }

    /**
     * 前置通知
     *
     * @param methodName 方法名
     * @param context 上下文
     * @return 是否通过
     */
    protected boolean preContextCheck(String methodName, T context) {
        if(context == null) {
            log.error("PipelineFactory.execute [{}] error, context is null", methodName);
            return false;
        }
        if(context.getResult() != null && !context.getResult().isSuccess()) {
            log.error("UserRightsPipelineFactory.execute [{}] error, pre method is failed with resultEnum:{}", methodName, context.getResult());
            return false;
        }
        log.info("PipelineFactory.execute [{}] start, context:{}", methodName, context);
        return true;
    }

    /**
     * 后置通知
     *
     * @param methodName 方法名
     * @param context 上下文
     */
    protected void afterResult(String methodName,T context) {
        log.info("PipelineFactory.execute [{}] end, context:{}", methodName, context);
    }
}