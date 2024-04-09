package org.dragon.aries.core.protocal.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.dragon.aries.common.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcMessageHandler extends SimpleChannelInboundHandler {
    private final static Logger log = LoggerFactory.getLogger(RpcMessageHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            if (request.getHeartBeat()) {
                log.info("心跳包接收成功，当前时刻：{}", );
            }
        }
    }
}
