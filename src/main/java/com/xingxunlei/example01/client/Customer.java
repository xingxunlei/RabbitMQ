package com.xingxunlei.example01.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Customer {

    private final static String QUEUE_NAME = "mq.test";

    public static void main(String[] args) throws Exception{

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 设置RabbitMQ地址
        factory.setHost("192.168.114.128");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        factory.setPort(5672);

        // 创建新连接
        Connection connection = factory.newConnection();

        // 创建一个通道
        Channel channel = connection.createChannel();

        // 声明要关注的队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("Customer Waiting Received messages");

        // DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
            }
        };

        // 自动回复队列应答 -- RabbitMQ中的消息确认机制
        // 将Consumer与消息队列绑定
        // 参数一：要绑定的队列名称
        // 参数二：自动确认标志，如果为true，表示Consumer接受到消息后，会自动发确认消息(Ack消息)给消息队列，消息队列会将这条消息从消息队列里删除
        // 参数三：Consumer对象，用于处理接收到的消息，当消费者收到消息时，会调用Consumer对象的handleDelivery方法。
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }
}
