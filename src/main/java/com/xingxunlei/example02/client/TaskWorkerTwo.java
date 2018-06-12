package com.xingxunlei.example02.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class TaskWorkerTwo {

    private static final String QUEUE_NAME = "mq.task";

    public static void main(String[] args) throws Exception{
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        Connection connection = factory.newConnection();

        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("Worker2 Waiting for messages");

        //每次从队列获取的数量
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Worker2 Received '" + message + "'");

                try {
                    doWork();
                }catch (Exception e){
                    channel.abort();
                }finally {
                    System.out.println("Worker2 Done");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork() throws InterruptedException{
        Thread.sleep(1000);
    }

}
