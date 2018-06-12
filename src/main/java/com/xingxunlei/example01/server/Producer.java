package com.xingxunlei.example01.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者
 */
public class Producer {

    private final static String QUEUE_NAME = "mq.test";

    public static void main(String[] args) throws Exception{

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 设置RabbitMQ相关信息
        factory.setHost("192.168.114.128");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        factory.setPort(5672);

        // 创建新连接
        Connection connection = factory.newConnection();

        // 创建通道
        Channel channel = connection.createChannel();

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello RabbitMQ!";

        // publish消息到队列
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));

        System.out.println("Producer Send +'" + message + "'");

        // 关闭通道和连接
        channel.close();
        connection.close();

    }

}
