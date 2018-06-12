package com.xingxunlei.example07.model;

import java.io.Serializable;

/**
 * 数据模型
 */
public class RpcInvokeModel implements Serializable {

    /**
     * 真实传输的数据
     */
    private Object data;

    /**
     * 调用序号
     */
    private Long invokeId;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Long invokeId) {
        this.invokeId = invokeId;
    }

}
