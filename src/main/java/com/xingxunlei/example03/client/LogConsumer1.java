package com.xingxunlei.example03.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class LogConsumer1 {

    private static final String EXCHANGE_NAME = "mq.exchange.fanout";

    public static void main(String[] args) throws Exception{

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置工厂相关配置
        factory.setHost("192.168.114.128");

        // 创建新的连接
        Connection connection = factory.newConnection();

        // 创建新的频道
        Channel channel = connection.createChannel();
        // 设置Exchange(交换器)及ExchangeType(交换器类型)
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 获取RabbitMQ自动生成的queue名称
        String queueName = channel.queueDeclare().getQueue();
        // 将队列与交换机绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("LogConsumer1 Waiting for messages");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("LogConsumer1 Received '" + message + "'");
            }
        };

        // 消息消费完成确认
        channel.basicConsume(queueName, true, consumer);

    }
}
