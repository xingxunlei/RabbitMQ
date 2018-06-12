package com.xingxunlei.example03.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class LogProducer {

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

        for (int i = 0; i < 10; i++) {
            // 初始化消息体
            String message = "I'm Log[" + i + "] provided by LogProducer";
            // 利用Exchange分发消息
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("LogProducer send '"+message+"'");
        }

        channel.close();
        connection.close();

    }
}
