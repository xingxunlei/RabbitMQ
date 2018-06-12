package com.xingxunlei.example06.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.UUID;

public class RPCClient {

    private Connection connection;
    private Channel channel;
    private String replyQueueName;
    private QueueingConsumer consumer;

    private static final String requestQueueName = "mq.queue.rpc";

    public RPCClient() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();

        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(int n) throws Exception {
        String response;
        String correlationId = UUID.randomUUID().toString();

        BasicProperties basicProperties = new BasicProperties.Builder().correlationId(correlationId).replyTo(replyQueueName).build();

        channel.basicPublish("", requestQueueName, basicProperties, Integer.toString(n).getBytes("UTF-8"));

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if(delivery.getProperties().getCorrelationId().equals(correlationId)) {
                response = new String(delivery.getBody(), "UTF-8");
                break;
            }
        }

        return response;
    }

    public void close() throws Exception{
        connection.close();
    }

    public static void main(String[] args) {
        RPCClient rpcClient = null;

        try {
            rpcClient = new RPCClient();

            System.out.println("RPCClient Requesting fib(30)");
            String response = rpcClient.call(30);
            System.out.println("RPCClient Got '" + response + "'");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rpcClient != null) {
                try {
                    rpcClient.close();
                } catch (Exception ignore) {
                }
            }
        }

    }

}
