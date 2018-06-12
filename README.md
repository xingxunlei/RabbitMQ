# rabbitMQ-Practice
* RabbitMQ 练习代码
* Maven项目

## example01
Hello RabbitMQ!

第一个RabbitMQ示例，生产者发送"Hello RabbitMQ!"至队列，消费者从队列接收消息。

## example02
Task Queues

任务队列，将信息发送到多个消费者。

## example03
Exchange

交换器，（fanout）类型，分发模式。实现消息广播。

## example04
Exchange

交换器，（direct）类型，直连模式。实现消息路由。

## example05
Exchange

交换器，（topic）类型，主题模式（匹配模式）。实现模糊匹配。

## example06
Remote procedure call (RPC)

使用工作队列让多个消费者来执行耗时的任务。比如我们需要通过远程服务器帮我们计算某个结果。这种模式通常被称之为远程方法调用或RPC。

## example07
Remote procedure call (RPC)

加入了线程，模拟多个client发送计算请求。