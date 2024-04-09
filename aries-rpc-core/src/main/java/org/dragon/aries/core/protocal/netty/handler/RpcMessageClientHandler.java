package org.dragon.aries.core.protocal.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.RpcRequest;

public class RpcMessageClientHandler extends ChannelInboundHandlerAdapter {
    private final static Logger log = LogManager.getLogger(RpcMessageClientHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[RpcClientHandler] channelActive Success]");
        ctx.writeAndFlush(RpcRequest.heart());
    }
}
