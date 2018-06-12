package com.xingxunlei.example07.server;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.xingxunlei.example07.model.RpcInvokeModel;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * 服务端（service）：服务端接收到了一个rpc调用后，执行调用代码，将结果返回到指定的回调队列中去。
 */
public class Service extends ServiceEndPoint implements Consumer {

    public Service(String endpointName) throws IOException, TimeoutException {
        super(endpointName);
    }

    public void start() throws IOException {

        super.consumerChannel.basicConsume(super.endPointName, true, this);

    }

    public void handleDelivery(String s, Envelope envelope, BasicProperties basicProperties, byte[] bytes) throws IOException {
        RpcInvokeModel rpcInvokeModel = SerializationUtils.deserialize(bytes);

        Integer[] integers = (Integer[]) rpcInvokeModel.getData();
        Arrays.sort(integers);

        rpcInvokeModel.setData(integers);

        byte[] body = SerializationUtils.serialize(rpcInvokeModel);
        String routingKey = basicProperties.getReplyTo();

        super.producerChannel.basicPublish("", routingKey, null, body);

    }

    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    public void handleRecoverOk(String s) {

    }

    public void handleConsumeOk(String consumerTag) {

    }

    public void handleCancelOk(String consumerTag) {

    }

    public void handleCancel(String consumerTag) throws IOException {

    }

}
