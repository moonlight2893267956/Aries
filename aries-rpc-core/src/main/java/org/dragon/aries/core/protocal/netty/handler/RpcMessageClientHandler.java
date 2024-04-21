package org.dragon.aries.core.protocal.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.entity.bo.DefaultResponseFuture;
import org.dragon.aries.common.enumeration.ResponseCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RpcMessageClientHandler extends ChannelDuplexHandler {
    private final static Logger log = LogManager.getLogger(RpcMessageClientHandler.class);
    private final Map<String, DefaultResponseFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            if (request.getHeartBeat()) {
                super.write(ctx, msg, promise);
            }
            futureMap.put(request.getRequestId(), new DefaultResponseFuture());
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("[RpcClientHandler] channelActive Success]");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcResponse) {
            RpcResponse<Object> response = (RpcResponse<Object>) msg;
            if (response.getStatusCode().equals(ResponseCode.HEART.getCode())) {
                return;
            }
            log.info("[RpcClientHandler] 接受接口调用响应：{} ", response);
            DefaultResponseFuture future = this.futureMap.get(response.getRequestId());
            future.setResponse(response);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state().equals(IdleState.WRITER_IDLE)) {
            ctx.writeAndFlush(RpcRequest.heart());
        } else if (event.state().equals(IdleState.WRITER_IDLE)) {
            log.error("[RpcClientHandler] read idle timeout,channel prepare close");
            ctx.channel().close().sync();
        }
    }

    public RpcResponse<Object> getResponse(String requestId, Long timeout) {
        DefaultResponseFuture future = futureMap.get(requestId);
        if (future == null) {
            log.error("[RpcClientHandler] requestId:{} not found", requestId);
            return null;
        }
        RpcResponse<Object> response = future.getResponse(timeout, TimeUnit.MILLISECONDS);
        futureMap.remove(requestId);
        if (response == null) {
            log.error("[RpcClientHandler] requestId:{} timeout", requestId);
            return null;
        }
        return response;
    }
}
