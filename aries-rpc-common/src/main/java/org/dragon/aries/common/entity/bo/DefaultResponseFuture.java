package org.dragon.aries.common.entity.bo;

import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.exception.RpcError;
import org.dragon.aries.common.exception.RpcException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultResponseFuture {
    private RpcResponse<?> response;
    private volatile boolean done;
    private ReentrantLock lock;
    private Condition condition;
    public DefaultResponseFuture() {
        this.done = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public RpcResponse<?> getResponse(Long timeout, TimeUnit unit) {
        lock.lock();
        if (!done) {
            try {
                condition.await(timeout, unit);
            } catch (InterruptedException e) {
                lock.unlock();
                throw new RuntimeException(e);
            }
        }
        lock.unlock();
        return response;
    }

    public void setResponse(RpcResponse<?> response) {
        lock.lock();
        this.response = response;
        condition.signalAll();
        lock.unlock();
    }
}
