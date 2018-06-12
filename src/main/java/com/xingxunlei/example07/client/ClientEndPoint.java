package com.xingxunlei.example07.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Represents a connection with a queue
 */
public abstract class ClientEndPoint {

    protected Channel producerChannel;

    protected Channel consumerChannel;

    protected Connection connection;

    protected String endPointName;

    public ClientEndPoint(String endPointName) throws IOException, TimeoutException {
        this.endPointName = endPointName;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.128");

        connection = factory.newConnection();

        producerChannel = connection.createChannel();
        consumerChannel = connection.createChannel();

        producerChannel.queueDeclare(endPointName, false, false, false, null);

    }

    public void close() throws IOException, TimeoutException {
        this.producerChannel.close();
        this.consumerChannel.close();
        this.connection.close();
    }
}
