package com.xingxunlei.example07;

import com.xingxunlei.example07.client.Client;
import com.xingxunlei.example07.server.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RpcTest {

    public static void main(String[] args) throws IOException, TimeoutException {

        String queue = "mq.rpc.test";
        client(queue);
        service(queue);

    }

    private static void service(String queue) throws IOException, TimeoutException {
        Service service = new Service(queue);
        service.start();
    }

    private static void client(String queue) throws IOException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            Client client = new Client(queue);
            new Thread(client).start();
        }
    }

}
