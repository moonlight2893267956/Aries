package org.dragon.aries.common.entity.bo;

import org.dragon.aries.common.entity.RpcResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultResponseFuture {
    private volatile RpcResponse<Object> response;
    private final ReentrantLock lock;
    private final Condition condition;

    public DefaultResponseFuture() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public RpcResponse<Object> getResponse(Long timeout, TimeUnit unit) {
        lock.lock();
        try {
            if (response == null) {
                condition.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return response;
    }

    public void setResponse(RpcResponse<Object> response) {
        lock.lock();
        this.response = response;
        condition.signalAll();
        lock.unlock();
    }
}
