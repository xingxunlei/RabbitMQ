package com.xingxunlei.example07.client;

public class RpcFuture<T> {

    private final Client client;
    private final Long flagKey;

    public RpcFuture(Client client, Long flagKey) {
        this.client = client;
        this.flagKey = flagKey;
    }

    public T get() {
        return (T) client.getResultByFlagKey(flagKey);
    }

}
