package com.xingxunlei.example02.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TaskProvider {

    private static final String QUEUE_NAME = "mq.task";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        for (int i = 0; i < 10; i++) {
            String message = "I'm Task[" + i + "] provided by TaskProvider";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("TaskProvider send '"+message+"'");
        }

        channel.close();
        connection.close();
    }
}
