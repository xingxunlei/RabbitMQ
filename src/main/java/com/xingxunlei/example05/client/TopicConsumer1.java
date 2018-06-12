package com.xingxunlei.example05.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class TopicConsumer1 {

    private static final String EXCHANGE_NAME = "mq.exchange.topic";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        // 声明一个匹配模式的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String queueName = channel.queueDeclare().getQueue();

        // 路由关键字
        String[] routingKeys = new String[]{"*.orange.*"};

        // 绑定路由关键字
        for (String bindingKey : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
            System.out.println("TopicConsumer1 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + bindingKey);
        }

        System.out.println("TopicConsumer1 Waiting for messages.");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("TopicConsumer1 Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
