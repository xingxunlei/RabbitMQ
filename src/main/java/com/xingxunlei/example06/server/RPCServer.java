package com.xingxunlei.example06.server;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "mq.queue.rpc";

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;

        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.114.128");

            connection = factory.newConnection();

            channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            System.out.println("RPCServer Awaiting RPC requests");

            while (true) {
                String response = null;

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties basicProperties = delivery.getProperties();
                BasicProperties replyProperties = new BasicProperties.Builder().correlationId(basicProperties.getCorrelationId()).build();

                try {

                    String message = new String(delivery.getBody(), "UTF-8");
                    int n = Integer.parseInt(message);

                    System.out.println("RPCServer fib(" + message + ")");
                    response = Integer.toString(fib(n));

                } catch (Exception e) {
                    e.printStackTrace();
                    response = "";
                    break;
                } finally {
                    channel.basicPublish("", basicProperties.getReplyTo(), replyProperties, response.getBytes("UTF-8"));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(channel != null) {
                try {
                    channel.close();
                } catch (Exception ignore) {
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }

    }

    /**
     * 计算斐波那契数列的方法
     * @param n 数列中的第几项
     * @return int 计算结果
     */
    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

}
