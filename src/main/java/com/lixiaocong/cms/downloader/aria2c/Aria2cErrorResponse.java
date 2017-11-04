package com.lixiaocong.cms.downloader.aria2c;

public class Aria2cErrorResponse {
    private String id;
    private String jsonrpc;
    private Aria2cError error;

    private class Aria2cError {
        private long code;
        private String message;

        public long getCode() {
            return code;
        }

        public void setCode(long code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Aria2cError getError() {
        return error;
    }

    public void setError(Aria2cError error) {
        this.error = error;
    }

    public String getMessage(){
        return this.error.getMessage();
    }
}
