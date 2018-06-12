package com.xingxunlei.example07.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.xingxunlei.example07.model.RpcInvokeModel;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * 客户端（client）：客户端发起rpc调用，这当成一个消息，发送到rabbitmq服务器。
 *              这个消息会携带两个特殊（额外）的信息，一个是调用序号，一个是回调队列名称。
 *              调用序号需要服务端原样返回，而回调队列名称是用于服务端将结果放入这个队列中，以便客户端取回结果。
 */
public class Client extends ClientEndPoint implements Consumer, Runnable {

    private final String callBackQueue;

    private final Map<Long, Object> callResult = new HashMap<Long, Object>();

    private volatile long invokeId = 0;

    public Client(String endPointName) throws IOException, TimeoutException {
        super(endPointName);

        DeclareOk declareOk = super.consumerChannel.queueDeclare();
        callBackQueue = declareOk.getQueue();

        super.consumerChannel.basicConsume(callBackQueue, true, this);
    }

    public void handleDelivery(String s, Envelope envelope, BasicProperties basicProperties, byte[] bytes) {
        RpcInvokeModel rpcInvokeModel = SerializationUtils.deserialize(bytes);

        synchronized (callResult) {
            callResult.put(rpcInvokeModel.getInvokeId(), rpcInvokeModel.getData());
            callResult.notifyAll();
        }

    }

    public void handleConsumeOk(String s) {

    }

    public void handleCancelOk(String s) {

    }

    public void handleCancel(String s) {

    }

    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    public void handleRecoverOk(String s) {

    }

    public void run() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getResultByFlagKey(Long flagKey) {
        Object result;

        while (true) {
            synchronized (callResult) {
                result = callResult.remove(flagKey);
                if (result == null) {
                    try {
                        callResult.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    return result;
                }
            }
        }
    }

    private void start() throws IOException {
        Random random = new Random();

//        for (int i = 0; i < 10; i++) {
            Integer[] integers = new Integer[10];
            for (int j = 0; j < integers.length; j++) {
                integers[j] = random.nextInt(90) + 10;
            }

            System.out.println("[" + Thread.currentThread().getName() + "]调用rpc服务：" + Arrays.toString(integers));

            RpcFuture<Integer[]> result = invokeSort(integers);
            Integer[] backs = result.get();

            System.out.println("[" + Thread.currentThread().getName() + "]返回rpc结果：" + Arrays.toString(backs));

//        }
    }

    private RpcFuture<Integer[]> invokeSort(Integer[] integers) throws IOException {
        RpcInvokeModel rpcInvokeModel = new RpcInvokeModel();

        rpcInvokeModel.setInvokeId(++invokeId);
        rpcInvokeModel.setData(integers);

        byte[] body = SerializationUtils.serialize(rpcInvokeModel);

        BasicProperties basicProperties = new BasicProperties.Builder().replyTo(callBackQueue).build();

        super.producerChannel.basicPublish("", super.endPointName, basicProperties, body);

        return new RpcFuture<Integer[]>(this, rpcInvokeModel.getInvokeId());
    }

}
