1. 编写 .bmpn 文件
2. 对这个文件进行部署 ==》 部署ID 和 流程定义ID 进行返回
3. 根据流程定义ID ==》 创建流程实例[添加 BusinessKey 表示] ==》 就是发起一次流程

> Camunda 中的实例ID是唯一 的 而BusinessKey不是是唯一的 只是一个标识
>
流程审批步骤

1. 发起者 发起流程设置变量initiator 为自己的ID 方便后续流程者获取 并且对该任务进行标记 taskService.setAssignee(userId) 表明这是由我执行的
2. 委托任务=》 Service Task ==》 自行业务判断并且 添加 candidateUsers 列表 后期审核人员ID列表 符合的才可以审批
3. 审批者 获取 candidateUsers 变量的 可审批人员列表 如果审批者符合则进行操作 否则不允许操作 完成后清空 candidateUsers
   变量 ``runtimeService.removeVariable(processInstanceId, BpmConstants.CANDIDATE_USERS);``


一定要有约束