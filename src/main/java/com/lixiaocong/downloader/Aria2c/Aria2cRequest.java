package com.lixiaocong.downloader.Aria2c;

import java.util.HashMap;
import java.util.UUID;

public class Aria2cRequest {
    private final String jsonrpc = "2.0";

    private String id;
    private String method;

    /**
     * params[0] is token
     * params[1] is url or something like that
     * params[2] is options
     */
    private Object[] params;

    public Aria2cRequest(String token, String method) {
        this.id = UUID.randomUUID().toString();
        this.method = method;
        this.params = new Object[3];
        params[0]="token:"+token;
        //params[1] config by factory
        params[2]=new HashMap<>();
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
