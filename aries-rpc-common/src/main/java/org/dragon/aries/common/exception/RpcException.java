package org.dragon.aries.common.exception;

public class RpcException extends RuntimeException{
    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
