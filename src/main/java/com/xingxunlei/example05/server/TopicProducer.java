package com.xingxunlei.example05.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicProducer {

    private static final String EXCHANGE_NAME = "mq.exchange.topic";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.114.128");

            connection = factory.newConnection();
            channel = connection.createChannel();

            // 声明一个Topic模式的交换器
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            // 待发送的消息
            String[] routingKeys = new String[]{"quick.orange.rabbit", "lazy.orange.elephant",
                    "quick.orange.fox", "lazy.brown.fox", "quick.brown.fox", "quick.orange.male.rabbit",
                    "lazy.orange.male.rabbit"};

            // 发送消息
            for(String severity :routingKeys){
                String message = "From "+severity+" routingKey' s message!";
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
                System.out.println("TopicProducer Sent '" + severity + "':'" + message + "'");
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception ignore) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }

    }
}
