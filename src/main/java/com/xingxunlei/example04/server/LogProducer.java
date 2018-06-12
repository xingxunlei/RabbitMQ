package com.xingxunlei.example04.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class LogProducer {

    private static final String EXCHANGE_NAME = "mq.exchange.direct";

    private static final String[] routingKeys = new String[]{"info", "warning", "error"};

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        for(String severity :routingKeys) {
            String message = "Send the message level:" + severity;
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
            System.out.println("LogProducer Sent '" + severity + "':'" + message + "'");
        }

        channel.close();
        connection.close();

    }
}
