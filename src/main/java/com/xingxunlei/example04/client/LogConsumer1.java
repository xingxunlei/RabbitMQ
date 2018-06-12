package com.xingxunlei.example04.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class LogConsumer1 {

    private static final String EXCHANGE_NAME = "mq.exchange.direct";

    private static final String[] routingKeys = new String[]{"error"};

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queueName = channel.queueDeclare().getQueue();
        for(String severity : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
            System.out.println("LogConsumer1 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + severity);
        }
        System.out.println("LogConsumer1 Waiting for messages.");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("LogConsumer1 Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
