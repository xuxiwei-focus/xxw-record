1.通过一个IdleIMLPipelineFactory执行工厂来提供对业务的访问入口；
2.通过配置的PipelineMaps来选择对应的执行链；
3.通过对每个Stage进行前置、后置增强来打一些日志、当前执行结果判断等操作；
4.通过在业务执行方法前加上幂等校验的方法来实现部分Stage失败时的重试；
5.暴露一个executeBusinessLogic方法用于业务操作的实现；

![img.png](img.png)